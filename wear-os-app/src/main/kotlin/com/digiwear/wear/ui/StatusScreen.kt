package com.digiwear.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.digiwear.core.engine.EvolutionEngine
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import com.digiwear.core.model.SpriteFrame
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.input.HardwareInputManager
import com.digiwear.wear.input.VPetButton
import com.digiwear.wear.render.SpriteCanvasRenderer

@Composable
fun StatusScreen(
    repository: PetRepository,
    onBack: () -> Unit
) {
    val petState by repository.petStateFlow.collectAsState(initial = PetState())
    val displayConfig = WatchDisplayConfig.current()
    val species = remember(petState.speciesId) { PetSpecies.getById(petState.speciesId) }
    val nextEvo = remember(petState) { EvolutionEngine.evaluateEvolution(petState) }
    val listState = rememberScalingLazyListState()
    val scope = rememberCoroutineScope()
    var fusionFeedback by remember { mutableStateOf<String?>(null) }
    var showCutInPortrait by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        HardwareInputManager.buttonEvents.collect { button ->
            if (button == VPetButton.C || button == VPetButton.B) {
                onBack()
            }
        }
    }

    val ageHours = petState.ageSeconds / 3600
    val ageDays = ageHours / 24
    val remHours = ageHours % 24

    val winPct = if (petState.totalBattles > 0) {
        (petState.victories * 100) / petState.totalBattles
    } else 0

    val attrColor = when (species.attribute.displayName) {
        "Vaccine" -> Color(0xFF00E5FF)
        "Data" -> Color(0xFFFFCA28)
        "Virus" -> Color(0xFFFF5252)
        else -> Color(0xFF69F0AE)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D131A)),
            state = listState,
        contentPadding = displayConfig.contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "V-PET ANALYZER",
                fontSize = displayConfig.headerFontSize,
                fontWeight = FontWeight.Black,
                color = Color(0xFF00E676),
                fontFamily = FontFamily.Monospace
            )
        }

        // Framed Partner Header Card with Static LCD Preview
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                    .background(Color(0xFF16202B), RoundedCornerShape(8.dp))
                    .border(0.75.dp, Color(0x5580CBC4), RoundedCornerShape(8.dp))
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF15241E), RoundedCornerShape(6.dp))
                        .border(0.75.dp, Color(0xFF4CAF50), RoundedCornerShape(6.dp))
                        .clickable { showCutInPortrait = true },
                    contentAlignment = Alignment.Center
                ) {
                    SpriteCanvasRenderer(
                        speciesId = petState.speciesId,
                        isAmbient = false,
                        overrideFrame = SpriteFrame.IDLE_A,
                        size = 38.dp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = species.name.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Color(0x3380CBC4), RoundedCornerShape(3.dp))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = species.stage.displayName.uppercase(),
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF80CBC4),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF263238), RoundedCornerShape(3.dp))
                                .border(0.5.dp, Color(0xFFFFD54F), RoundedCornerShape(3.dp))
                                .clickable { showCutInPortrait = true }
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "⚡ CUT-IN",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD54F),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(attrColor.copy(alpha = 0.2f), RoundedCornerShape(3.dp))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = species.attribute.displayName.uppercase(),
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = attrColor,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // Biological Gauges Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                    .background(Color(0xFF141C24), RoundedCornerShape(8.dp))
                    .padding(6.dp)
            ) {
                StatusRow("HUNGER", "♥".repeat(petState.hunger) + "♡".repeat(4 - petState.hunger), Color(0xFFFF5252))
                StatusRow("STRENGTH", "★".repeat(petState.strength) + "☆".repeat(4 - petState.strength), Color(0xFFFFD700))
                StatusRow("EFFORT", "★".repeat(petState.effort) + "☆".repeat(4 - petState.effort), Color(0xFFCE93D8))
                StatusRow("HP / POWER", "${petState.currentHp}/${petState.maxHp} • ${species.basePower}P", Color(0xFF80D8FF))
            }
        }

        // Vitals & Care History Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                    .background(Color(0xFF141C24), RoundedCornerShape(8.dp))
                    .padding(6.dp)
            ) {
                StatusRow("AGE / WT", "${ageDays}d ${remHours}h • ${petState.weight}g", Color.White)
                StatusRow("MISTAKES", "${petState.careMistakes} Care / ${petState.overfeedCount} Overfeed", Color(0xFFFFCC80))
                StatusRow("RECORD", "${petState.victories}W / ${petState.totalBattles}B ($winPct%)", Color(0xFF69F0AE))
                StatusRow(
                    "NEXT EVO",
                    nextEvo?.name?.uppercase() ?: if (species.evolutionRules.isEmpty()) "APEX STAGE" else "TRAINING...",
                    if (nextEvo != null) Color(0xFF00E676) else Color.Gray
                )
            }
        }

        // Evolution Targets & Lineage Guide Card
        item {
            val lineagePath = remember(species.id) { EvolutionEngine.findEggLineagePath(species.id).map { PetSpecies.getById(it) } }
            val jogressRecipes = remember(species.id) { com.digiwear.core.fusion.JogressEngine.getCompatibleRecipesFor(species.id) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                    .background(Color(0xFF161D28), RoundedCornerShape(8.dp))
                    .border(0.5.dp, Color(0x44FFD54F), RoundedCornerShape(8.dp))
                    .padding(6.dp)
            ) {
                Text(
                    text = "EVO GUIDE & LINEAGE",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD54F),
                    fontFamily = FontFamily.Monospace
                )
                if (lineagePath.isNotEmpty()) {
                    Text(
                        text = lineagePath.joinToString("→") { it.name.take(6) },
                        fontSize = 7.sp,
                        color = Color(0xFF80CBC4),
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                if (species.evolutionRules.isEmpty()) {
                    Text(
                        text = "Apex Stage Reached!",
                        fontSize = 7.sp,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    species.evolutionRules.forEach { rule ->
                        val target = PetSpecies.getById(rule.targetSpeciesId)
                        val isReady = nextEvo?.id == target.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 1.5.dp)
                                .background(
                                    if (isReady) Color(0xFF142E24) else Color(0xFF0F151D),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SpriteCanvasRenderer(
                                speciesId = target.id,
                                isAmbient = false,
                                overrideFrame = SpriteFrame.IDLE_A,
                                size = 20.dp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "→ ${target.name} ${if (isReady) "[READY]" else ""}",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isReady) Color(0xFF00E676) else Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = EvolutionEngine.describeRule(rule),
                                    fontSize = 6.5.sp,
                                    color = Color(0xFFB0BEC5),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
                if (jogressRecipes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚡ DNA JOGRESS / DIGIXROS READY (${jogressRecipes.size})",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFEA80FC),
                        fontFamily = FontFamily.Monospace
                    )
                    fusionFeedback?.let { msg ->
                        Text(
                            text = msg,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    jogressRecipes.forEach { recipe ->
                        val partnerId = if (recipe.primarySpeciesId == species.id) recipe.secondarySpeciesId else recipe.primarySpeciesId
                        val partner = PetSpecies.getById(partnerId)
                        val target = PetSpecies.getById(recipe.targetSpeciesId)
                        val isSelfModeChange = recipe.primarySpeciesId == recipe.secondarySpeciesId
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .background(Color(0xFF1F1630), RoundedCornerShape(6.dp))
                                .border(0.5.dp, Color(recipe.fusionType.badgeColorHex), RoundedCornerShape(6.dp))
                                .padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    SpriteCanvasRenderer(
                                        speciesId = partner.id,
                                        isAmbient = false,
                                        overrideFrame = SpriteFrame.IDLE_A,
                                        size = 18.dp
                                    )
                                    Text(" → ", fontSize = 7.sp, color = Color(0xFFFFD54F), fontWeight = FontWeight.Black)
                                    SpriteCanvasRenderer(
                                        speciesId = target.id,
                                        isAmbient = false,
                                        overrideFrame = SpriteFrame.IDLE_A,
                                        size = 20.dp
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Column {
                                        Text(
                                            text = if (isSelfModeChange) "MODE: ${target.name.take(10)}" else "+${partner.name.take(7)} → ${target.name.take(8)}",
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = recipe.fusionType.displayName,
                                            fontSize = 6.sp,
                                            color = Color(recipe.fusionType.badgeColorHex),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        scope.launch {
                                            val fused = com.digiwear.core.fusion.JogressEngine.executeFusion(
                                                primary = petState,
                                                secondarySpeciesId = partner.id,
                                                secondaryVictories = 15,
                                                secondaryEffort = 4
                                            )
                                            repository.savePetState(fused)
                                            fusionFeedback = "⚡ Awakened ${target.name}!"
                                        }
                                    },
                                    modifier = Modifier.height(22.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(recipe.fusionType.badgeColorHex))
                                ) {
                                    Text("⚡ FUSE", fontSize = 6.5.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                } else {
                    // Show upcoming Jogress/DigiXros in immediate evolution branch
                    val nextBranchJogress = remember(species.id) {
                        species.evolutionRules.flatMap { r ->
                            com.digiwear.core.fusion.JogressEngine.getCompatibleRecipesFor(r.targetSpeciesId)
                                .map { recipe -> PetSpecies.getById(r.targetSpeciesId) to recipe }
                        }.take(2)
                    }
                    if (nextBranchJogress.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "⚡ NEXT STAGE JOGRESS / XROS UNLOCKS:",
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA80FC),
                            fontFamily = FontFamily.Monospace
                        )
                        nextBranchJogress.forEach { (branchSp, recipe) ->
                            val targetSp = PetSpecies.getById(recipe.targetSpeciesId)
                            Text(
                                text = "• Evolve to ${branchSp.name.take(8)} → ${targetSp.name.take(10)} (${recipe.fusionType.displayName.take(8)})",
                                fontSize = 6.sp,
                                color = Color(0xFFCE93D8),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(horizontal = displayConfig.horizontalPadding),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF263238))
            ) {
                Text("BACK [B / C]", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
    }

    if (showCutInPortrait) {
        CutInOverlay(
            speciesId = petState.speciesId,
            title = "OFFICIAL ALBUM CUT-IN",
            autoDismissMs = null,
            onDismiss = { showCutInPortrait = false }
        )
    }
    }
}

@Composable
private fun StatusRow(label: String, value: String, valueColor: Color = Color.White, horizontalPadding: Dp = 0.dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 1.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 8.sp, color = Color(0xFF90A4AE), fontFamily = FontFamily.Monospace)
        Text(text = value, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = valueColor, fontFamily = FontFamily.Monospace)
    }
}
