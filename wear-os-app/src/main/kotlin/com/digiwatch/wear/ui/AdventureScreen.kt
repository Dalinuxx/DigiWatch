package com.digiwatch.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import com.digiwatch.core.adventure.AdventureEngine
import com.digiwatch.core.battle.DeterministicBattleSolver
import com.digiwatch.core.model.AdventureProgress
import com.digiwatch.core.model.BattleFighter
import com.digiwatch.core.model.BossEncounter
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetState
import com.digiwatch.wear.data.PetRepository
import com.digiwatch.wear.haptic.HapticManager
import com.digiwatch.wear.input.HardwareInputManager
import com.digiwatch.wear.input.VPetButton
import com.digiwatch.wear.render.SpriteCanvasRenderer
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AdventureScreen(
    repository: PetRepository,
    hapticManager: HapticManager,
    onBack: () -> Unit
) {
    val progress by repository.adventureStateFlow.collectAsState(initial = AdventureProgress())
    val petState by repository.petStateFlow.collectAsState(initial = PetState())
    val campaignJson by repository.campaignJsonFlow.collectAsState()
    val story = remember(campaignJson) { repository.getActiveCampaign() }
    val playerSpecies = remember(petState.speciesId) { PetSpecies.getById(petState.speciesId) }
    val displayConfig = WatchDisplayConfig.current()
    val scope = rememberCoroutineScope()
    val listState = rememberScalingLazyListState()

    val currentNodeIndex = progress.currentNodeIndex.coerceAtMost((story.nodes.size - 1).coerceAtLeast(0))
    val currentNode = story.nodes[currentNodeIndex]
    var eventMessage by remember { mutableStateOf<String?>(null) }
    var wildEncounterMonsterId by remember { mutableStateOf<Int?>(null) }

    val actLabel = when {
        currentNodeIndex < 10 -> "ACT I • FILE ISLAND"
        currentNodeIndex < 20 -> "ACT II • SERVER CONT."
        currentNodeIndex < 25 -> "ACT III • SPIRAL MTN"
        else -> "ACT IV • GENESIS"
    }
    val stepRatio = (progress.accumulatedStepsCurrentNode.toFloat() / currentNode.requiredSteps.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    val executeAdventureCombat: suspend (Int, Int?, Int?, BossEncounter?) -> Unit = { enemySpeciesId, hpOverride, powerOverride, bossToClear ->
        val enemySpecies = PetSpecies.getById(enemySpeciesId)
        val enemyHp = hpOverride ?: enemySpecies.baseHp
        val enemyPower = powerOverride ?: enemySpecies.basePower

        val fighterA = BattleFighter(
            id = "player",
            speciesId = petState.speciesId,
            name = playerSpecies.name,
            stage = playerSpecies.stage,
            attribute = playerSpecies.attribute,
            maxHp = petState.maxHp,
            currentHp = petState.maxHp,
            basePower = playerSpecies.basePower,
            victories = petState.victories,
            effort = petState.effort,
            battleSeed = Random.nextInt(0xFFFF)
        )

        val fighterB = BattleFighter(
            id = "adventure_enemy",
            speciesId = enemySpecies.id,
            name = enemySpecies.name,
            stage = enemySpecies.stage,
            attribute = enemySpecies.attribute,
            maxHp = enemyHp,
            currentHp = enemyHp,
            basePower = enemyPower,
            victories = currentNodeIndex,
            effort = (currentNodeIndex / 8).coerceIn(0, 4),
            battleSeed = Random.nextInt(0xFFFF)
        )

        val result = DeterministicBattleSolver.resolveBattle(fighterA, fighterB)
        val won = result.winnerIndex == 0
        repository.recordBattleResult(won)

        if (bossToClear != null) {
            if (won) {
                hapticManager.playVictoryFanfare()
                repository.recordBossVictory(bossToClear)
                eventMessage = "🏆 Boss ${enemySpecies.name} Defeated (${result.turns.size} turns)! Gate Unlocked!"
            } else {
                hapticManager.playAttentionAlert()
                eventMessage = "💀 Boss ${enemySpecies.name} held the gate (HP: ${result.fighterBFinalHp}). Train & retry!"
            }
        } else {
            wildEncounterMonsterId = null
            if (won) {
                hapticManager.playVictoryFanfare()
                eventMessage = "⚔ Defeated Wild ${enemySpecies.name} (${result.turns.size} turns)! +1 Win"
            } else {
                hapticManager.playAttackImpact()
                eventMessage = "Wild ${enemySpecies.name} fled after a fierce clash!"
            }
        }
    }

    LaunchedEffect(currentNode, progress, wildEncounterMonsterId, petState) {
        HardwareInputManager.buttonEvents.collect { button ->
            when (button) {
                VPetButton.C -> onBack()
                VPetButton.B -> {
                    val activeWild = wildEncounterMonsterId
                    val boss = currentNode.bossEncounter
                    if (activeWild != null) {
                        executeAdventureCombat(activeWild, null, null, null)
                    } else if (boss != null && !progress.clearedFlags.contains(boss.completionFlag)) {
                        executeAdventureCombat(boss.bossMonsterId, boss.bossHp, boss.bossPower, boss)
                    } else {
                        val result = repository.addAdventureSteps(100)
                        val monsterId = result.randomEncounterMonsterId
                        if (result.nodeCleared) {
                            hapticManager.playStepMilestone()
                            eventMessage = "Sector Gate Cleared!"
                        } else if (monsterId != null) {
                            wildEncounterMonsterId = monsterId
                            val encSpecies = PetSpecies.getById(monsterId)
                            hapticManager.playAttackImpact()
                            eventMessage = "Wild ${encSpecies.name} appeared!"
                        }
                    }
                }
                else -> {}
            }
        }
    }

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1410)),
        state = listState,
        contentPadding = displayConfig.contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "DIGIVICE QUEST",
                fontSize = displayConfig.headerFontSize,
                fontWeight = FontWeight.Black,
                color = Color(0xFF26A69A),
                fontFamily = FontFamily.Monospace
            )
        }

        item {
            Box(
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .background(Color(0xFF142820), RoundedCornerShape(4.dp))
                    .border(0.5.dp, Color(0xFF26A69A), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "$actLabel • SEC ${currentNodeIndex + 1}/${story.nodes.size}",
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF80CBC4),
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        item {
            Text(
                text = currentNode.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // Narrative box
        item {
            Text(
                text = currentNode.narrativeText,
                fontSize = 8.sp,
                color = Color(0xFFB0BEC5),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding)
            )
        }

        // Environmental condition indicator
        item {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "⚡ ENV: ${currentNode.environmentEffect.uppercase()}",
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFB74D),
                fontFamily = FontFamily.Monospace
            )
        }

        // Steps Progress Card with Visual Progress Bar
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding, vertical = 4.dp)
                    .background(Color(0xFF12221B), RoundedCornerShape(8.dp))
                    .border(0.75.dp, Color(0x5526A69A), RoundedCornerShape(8.dp))
                    .padding(6.dp)
            ) {
                Text(
                    text = "${progress.accumulatedStepsCurrentNode} / ${currentNode.requiredSteps} STEPS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF64FFDA),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color(0xFF08100C), RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(stepRatio)
                            .height(6.dp)
                            .background(Color(0xFF00E676), RoundedCornerShape(3.dp))
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Expedition Total: ${progress.totalAdventureSteps} steps",
                    fontSize = 7.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Wild Encounter Card (when random encounter occurs)
        wildEncounterMonsterId?.let { wildId ->
            val wildSpecies = PetSpecies.getById(wildId)
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                        .background(Color(0xFF1A2536), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFFFD54F), RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Text(
                        text = "⚡ WILD ${wildSpecies.name.uppercase()}! ⚡",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD54F),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "${wildSpecies.stage.displayName} • ${wildSpecies.attribute.displayName} • HP ${wildSpecies.baseHp}",
                        fontSize = 7.sp,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace
                    )
                    SpriteCanvasRenderer(
                        speciesId = wildId,
                        isAmbient = false,
                        size = displayConfig.battleSpriteSize
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                executeAdventureCombat(wildId, null, null, null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(28.dp).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00))
                    ) {
                        Text("⚔ ENGAGE WILD [B]", fontSize = 8.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // Boss Battle Encounter Gate (if steps met and boss present)
        if (currentNode.bossEncounter != null && !progress.clearedFlags.contains(currentNode.bossEncounter!!.completionFlag)) {
            val boss = currentNode.bossEncounter!!
            val bossSpecies = PetSpecies.getById(boss.bossMonsterId)
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                        .background(Color(0xFF2A0F14), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Text(
                        text = "☠ BOSS GATE: ${bossSpecies.name.uppercase()}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF5252),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "HP ${boss.bossHp} • PWR ${boss.bossPower} • ${bossSpecies.attribute.displayName}",
                        fontSize = 7.sp,
                        color = Color(0xFFFFCDD2),
                        fontFamily = FontFamily.Monospace
                    )
                    SpriteCanvasRenderer(
                        speciesId = boss.bossMonsterId,
                        isAmbient = false,
                        size = displayConfig.battleSpriteSize
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                executeAdventureCombat(boss.bossMonsterId, boss.bossHp, boss.bossPower, boss)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(28.dp).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC62828))
                    ) {
                        Text("⚔ CHALLENGE BOSS [B]", fontSize = 8.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // Campaign Completed Banner
        if (progress.isCompleted) {
            item {
                Text(
                    text = "🎉 DIGITAL WORLD RESTORED! 🎉",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD54F),
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Step Simulator / Walk Button
        item {
            Spacer(modifier = Modifier.height(2.dp))
            Button(
                onClick = {
                    scope.launch {
                        val result = repository.addAdventureSteps(100)
                        val monsterId = result.randomEncounterMonsterId
                        if (result.nodeCleared) {
                            hapticManager.playStepMilestone()
                            eventMessage = "Sector Gate Cleared!"
                        } else if (monsterId != null) {
                            wildEncounterMonsterId = monsterId
                            val encSpecies = PetSpecies.getById(monsterId)
                            hapticManager.playAttackImpact()
                            eventMessage = "Wild ${encSpecies.name} Encountered!"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00897B))
            ) {
                Text("+100 Steps (Walk)", fontSize = 9.sp)
            }
        }

        eventMessage?.let { msg ->
            item {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = msg,
                    fontSize = 9.sp,
                    color = Color(0xFF64FFDA),
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF37474F))
            ) {
                Text("BACK", fontSize = 10.sp)
            }
        }
    }
}
