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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableIntStateOf
import com.digiwatch.core.adventure.AdventureEngine
import com.digiwatch.core.battle.DeterministicBattleSolver
import com.digiwatch.core.model.AdventureProgress
import com.digiwatch.core.model.BattleFighter
import com.digiwatch.core.model.BattleMode
import com.digiwatch.core.model.BattleResult
import com.digiwatch.core.model.BossEncounter
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetState
import com.digiwatch.core.model.SpriteFrame
import com.digiwatch.wear.data.PetRepository
import com.digiwatch.wear.haptic.HapticManager
import com.digiwatch.wear.input.HardwareInputManager
import com.digiwatch.wear.input.VPetButton
import com.digiwatch.wear.render.SpriteCanvasRenderer
import kotlinx.coroutines.delay
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

    // Live Adventure Combat Arena State
    var activeBattleResult by remember { mutableStateOf<BattleResult?>(null) }
    var currentTurnIndex by remember { mutableIntStateOf(0) }
    var isBattling by remember { mutableStateOf(false) }
    var qteActive by remember { mutableStateOf(false) }
    var criticalBoostActive by remember { mutableStateOf(false) }

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            HardwareInputManager.activeQteListener = null
        }
    }

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
        val seedA = Random.nextInt(0xFFFF)
        val seedB = Random.nextInt(0xFFFF)

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
            battleSeed = seedA
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
            battleSeed = seedB
        )

        val baseResult = DeterministicBattleSolver.resolveBattle(
            fighterA = fighterA,
            fighterB = fighterB,
            environmentEffect = currentNode.environmentEffect,
            attackerCritBoost = false,
            battleMode = BattleMode.ADVENTURE
        )
        val critResult = DeterministicBattleSolver.resolveBattle(
            fighterA = fighterA,
            fighterB = fighterB,
            environmentEffect = currentNode.environmentEffect,
            attackerCritBoost = true,
            battleMode = BattleMode.ADVENTURE
        )

        var chosenResult = baseResult
        activeBattleResult = chosenResult
        isBattling = true
        currentTurnIndex = 0

        var idx = 0
        while (idx < chosenResult.turns.size) {
            currentTurnIndex = idx
            val turn = chosenResult.turns[idx]
            criticalBoostActive = false
            if (turn.attackerIndex == 0) {
                qteActive = true
                HardwareInputManager.activeQteListener = { btn ->
                    if (btn == VPetButton.B || btn == VPetButton.A) {
                        criticalBoostActive = true
                        chosenResult = critResult
                        activeBattleResult = critResult
                        hapticManager.playAttackImpact()
                        true
                    } else false
                }
                delay(420L)
                HardwareInputManager.activeQteListener = null
                qteActive = false
                if (criticalBoostActive) {
                    chosenResult = critResult
                    activeBattleResult = critResult
                }
            }
            hapticManager.playAttackImpact()
            delay(560L)
            idx++
        }

        val won = chosenResult.winnerIndex == 0
        repository.recordBattleResult(won)
        isBattling = false
        activeBattleResult = null

        if (bossToClear != null) {
            if (won) {
                hapticManager.playVictoryFanfare()
                repository.recordBossVictory(bossToClear)
                eventMessage = "🏆 Boss ${enemySpecies.name} Defeated (${chosenResult.turns.size} turns)! Gate Unlocked!"
            } else {
                hapticManager.playAttentionAlert()
                eventMessage = "💀 Boss ${enemySpecies.name} held the gate (HP: ${chosenResult.fighterBFinalHp}). Train & retry!"
            }
        } else {
            wildEncounterMonsterId = null
            if (won) {
                hapticManager.playVictoryFanfare()
                eventMessage = "⚔ Defeated Wild ${enemySpecies.name} (${chosenResult.turns.size} turns)! +1 Win"
            } else {
                hapticManager.playAttackImpact()
                eventMessage = "Wild ${enemySpecies.name} fled after a fierce clash!"
            }
        }
    }

    LaunchedEffect(currentNode, progress, wildEncounterMonsterId, petState, isBattling) {
        HardwareInputManager.buttonEvents.collect { button ->
            if (isBattling) return@collect
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

    // Live Face-to-Face Adventure Combat Overlay
    if (activeBattleResult != null && isBattling) {
        val currentTurn = activeBattleResult!!.turns.getOrNull(currentTurnIndex)
        val playerRemHp = if (currentTurn == null) {
            activeBattleResult!!.fighterA.maxHp
        } else if (currentTurn.attackerIndex == 1) {
            currentTurn.defenderRemainingHp
        } else {
            activeBattleResult!!.turns.take(currentTurnIndex).lastOrNull { it.attackerIndex == 1 }?.defenderRemainingHp ?: activeBattleResult!!.fighterA.maxHp
        }
        val oppRemHp = if (currentTurn == null) {
            activeBattleResult!!.fighterB.maxHp
        } else if (currentTurn.attackerIndex == 0) {
            currentTurn.defenderRemainingHp
        } else {
            activeBattleResult!!.turns.take(currentTurnIndex).lastOrNull { it.attackerIndex == 0 }?.defenderRemainingHp ?: activeBattleResult!!.fighterB.maxHp
        }
        val playerHpFrac = (playerRemHp.toFloat() / activeBattleResult!!.fighterA.maxHp.coerceAtLeast(1)).coerceIn(0f, 1f)
        val oppHpFrac = (oppRemHp.toFloat() / activeBattleResult!!.fighterB.maxHp.coerceAtLeast(1)).coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A1410)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = displayConfig.horizontalPadding)
            ) {
                Text(
                    text = "COLOSSEUM • SEC ${currentNodeIndex + 1}",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF64FFDA),
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "R${currentTurn?.round ?: 1}/10 • ENV: ${currentNode.environmentEffect.uppercase()}",
                    fontSize = 7.sp,
                    color = Color(0xFFFFD54F),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(playerSpecies.name.take(8), fontSize = 7.5.sp, color = Color(0xFF80D8FF), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        SpriteCanvasRenderer(
                            speciesId = petState.speciesId,
                            isAmbient = false,
                            overrideFrame = if (currentTurn?.attackerIndex == 0) SpriteFrame.ATTACK_RELEASE else SpriteFrame.IDLE_A,
                            size = displayConfig.battleSpriteSize,
                            flipHorizontal = true
                        )
                        Box(modifier = Modifier.width(48.dp).height(4.dp).background(Color(0xFF263238), RoundedCornerShape(2.dp))) {
                            Box(modifier = Modifier.fillMaxWidth(playerHpFrac).height(4.dp).background(Color(0xFF00E676), RoundedCornerShape(2.dp)))
                        }
                        Text("$playerRemHp HP", fontSize = 6.5.sp, color = Color(0xFFB9F6CA), fontFamily = FontFamily.Monospace)
                    }

                    Text(
                        text = when {
                            currentTurn?.attackType == "MEGA_HIT" -> "⚡MEGA⚡"
                            currentTurn?.attackerIndex == 0 -> "●━▶"
                            currentTurn?.attackerIndex == 1 -> "◀━●"
                            else -> "VS"
                        },
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD54F),
                        fontFamily = FontFamily.Monospace
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(activeBattleResult!!.fighterB.name.take(8), fontSize = 7.5.sp, color = Color(0xFFFF8A80), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        SpriteCanvasRenderer(
                            speciesId = activeBattleResult!!.fighterB.speciesId,
                            isAmbient = false,
                            overrideFrame = if (currentTurn?.attackerIndex == 1) SpriteFrame.ATTACK_RELEASE else SpriteFrame.IDLE_A,
                            size = displayConfig.battleSpriteSize,
                            flipHorizontal = false
                        )
                        Box(modifier = Modifier.width(48.dp).height(4.dp).background(Color(0xFF263238), RoundedCornerShape(2.dp))) {
                            Box(modifier = Modifier.fillMaxWidth(oppHpFrac).height(4.dp).background(Color(0xFFFF5252), RoundedCornerShape(2.dp)))
                        }
                        Text("$oppRemHp HP", fontSize = 6.5.sp, color = Color(0xFFFFCDD2), fontFamily = FontFamily.Monospace)
                    }
                }

                currentTurn?.let { turn ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${turn.attackerName.take(8)} [${turn.attackType}] -${turn.damage}HP!",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (turn.isAdvantage) Color(0xFF00E676) else Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    if (qteActive) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .background(Color(0xFFFF1744), RoundedCornerShape(4.dp))
                                .border(0.75.dp, Color.White, RoundedCornerShape(4.dp))
                                .clickable {
                                    criticalBoostActive = true
                                    hapticManager.playAttackImpact()
                                    qteActive = false
                                }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("⚡ STRIKE [B] / TAP! ⚡", fontSize = 7.5.sp, fontWeight = FontWeight.Black, color = Color.White, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
        return
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
