package com.digiwatch.companion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwatch.companion.data.PhoneDatabase
import com.digiwatch.companion.data.StoredDigimonEntity
import com.digiwatch.companion.data.SyncedPetEntity
import com.digiwatch.companion.datalayer.PhoneWearableManager
import com.digiwatch.companion.render.SpriteCanvasRenderer
import com.digiwatch.companion.widget.DigiWatchAppWidgetProvider
import com.digiwatch.core.fusion.JogressEngine
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetStage
import com.digiwatch.core.model.PetState
import com.digiwatch.core.model.SpriteFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class FrameMeta(val index: Int, val row: Int, val col: Int, val name: String, val category: String)

val SPRITE_GRID_FRAMES = listOf(
    // Row 0
    FrameMeta(0, 0, 0, "Idle Stance A", "Row 0 (Idle/Walk)"),
    FrameMeta(1, 0, 1, "Idle Stance B", "Row 0 (Idle/Walk)"),
    FrameMeta(2, 0, 2, "Walking Motion A", "Row 0 (Idle/Walk)"),
    // Row 1
    FrameMeta(3, 1, 0, "Walking Motion B", "Row 1 (Feed/Joy)"),
    FrameMeta(4, 1, 1, "Feeding Start", "Row 1 (Feed/Joy)"),
    FrameMeta(5, 1, 2, "Chewing / Joy", "Row 1 (Feed/Joy)"),
    // Row 2
    FrameMeta(6, 2, 0, "Sleeping Frame A", "Row 2 (Sleep/Refuse)"),
    FrameMeta(7, 2, 1, "Sleeping Frame B", "Row 2 (Sleep/Refuse)"),
    FrameMeta(8, 2, 2, "Refusal / Displeasure", "Row 2 (Sleep/Refuse)"),
    // Row 3
    FrameMeta(9, 3, 0, "Attack Charge", "Row 3 (Combat/Status)"),
    FrameMeta(10, 3, 1, "Attack Release", "Row 3 (Combat/Status)"),
    FrameMeta(11, 3, 2, "Injury / Illness", "Row 3 (Combat/Status)")
)

private val PREVIEW_MODES = listOf("IDLE", "WALK", "FEED", "SLEEP", "ATTACK", "STATUS")

@Composable
fun SpriteStudioScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    var selectedSpeciesId by remember { mutableIntStateOf(201) } // Agumon
    var selectedFrameIndex by remember { mutableIntStateOf(0) }
    var previewMode by remember { mutableStateOf("IDLE") } // IDLE, WALK, FEED, SLEEP, ATTACK, STATUS
    var animFrame by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedStageFilter by remember { mutableStateOf<PetStage?>(null) }
    var syncMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val selectedSpecies = remember(selectedSpeciesId) { PetSpecies.getById(selectedSpeciesId) }

    val filteredRoster = remember(searchQuery, selectedStageFilter) {
        PetSpecies.SPECIES_REGISTRY.values.filter { sp ->
            val matchesSearch = sp.name.contains(searchQuery, ignoreCase = true) ||
                    sp.id.toString().contains(searchQuery.trim())
            val matchesStage = selectedStageFilter == null || sp.stage == selectedStageFilter
            matchesSearch && matchesStage
        }
    }

    val preEvolutions = remember(selectedSpeciesId) {
        PetSpecies.SPECIES_REGISTRY.values.filter { sp ->
            sp.evolutionRules.any { it.targetSpeciesId == selectedSpeciesId }
        }
    }

    val relatedFusions = remember(selectedSpeciesId) {
        JogressEngine.RECIPES.filter {
            it.primarySpeciesId == selectedSpeciesId ||
                    it.secondarySpeciesId == selectedSpeciesId ||
                    it.targetSpeciesId == selectedSpeciesId
        }
    }

    // Live Animation Loop
    LaunchedEffect(previewMode) {
        while (true) {
            when (previewMode) {
                "IDLE" -> {
                    animFrame = 0
                    delay(500L)
                    animFrame = 1
                    delay(500L)
                }
                "WALK" -> {
                    animFrame = 2
                    delay(350L)
                    animFrame = 3
                    delay(350L)
                }
                "FEED" -> {
                    animFrame = 4
                    delay(400L)
                    animFrame = 5
                    delay(600L)
                }
                "SLEEP" -> {
                    animFrame = 6
                    delay(600L)
                    animFrame = 7
                    delay(600L)
                }
                "ATTACK" -> {
                    animFrame = 9
                    delay(500L)
                    animFrame = 10
                    delay(500L)
                }
                "STATUS" -> {
                    animFrame = 8
                    delay(650L)
                    animFrame = 11
                    delay(650L)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWatchPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DigiSectionHeader(
            tag = "DIGIDEX // COLOR SPRITE STUDIO",
            title = "${PetSpecies.SPECIES_REGISTRY.size}-Species Encyclopedia",
            subtitle = "64x64px Digimon Color / 0nl1ne Matrix (FilterQuality.None)",
            accentColor = DigiWatchPalette.CrestGold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search ${PetSpecies.SPECIES_REGISTRY.size} Digimon by name or ID...", color = Color.Gray, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Stage Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StageFilterChip("All (${PetSpecies.SPECIES_REGISTRY.size})", selectedStageFilter == null) {
                selectedStageFilter = null
            }
            PetStage.entries.forEach { stage ->
                StageFilterChip(stage.displayName, selectedStageFilter == stage) {
                    selectedStageFilter = if (selectedStageFilter == stage) null else stage
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filtered Species Selector Row with Miniature Sprites (Virtualized LazyRow for 350 species)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredRoster, key = { it.id }) { sp ->
                val selected = selectedSpeciesId == sp.id
                Row(
                    modifier = Modifier
                        .background(
                            if (selected) Color(0xFF2E220D) else DigiWatchPalette.CardSurface,
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (selected) DigiWatchPalette.CrestGold else DigiWatchPalette.CardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { selectedSpeciesId = sp.id }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${sp.id} ${sp.name}",
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) DigiWatchPalette.CrestGold else Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Real-time Animation Preview Stage + Species Metadata inside VPetBrickShellFrame
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "#${selectedSpecies.id} ${selectedSpecies.name}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "HP ${selectedSpecies.baseHp} • PWR ${selectedSpecies.basePower} • WT ${selectedSpecies.baselineWeight}G",
                            fontSize = 10.sp,
                            color = Color(0xFF80CBC4),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        StageBadge(selectedSpecies.stage.displayName)
                        AttributeBadge(selectedSpecies.attribute.displayName)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                VPetBrickShellFrame(
                    selectedIconIndex = PREVIEW_MODES.indexOf(previewMode).coerceAtLeast(0),
                    statusLabel = "FRAME $animFrame • $previewMode",
                    screenHeight = 140.dp,
                    onButtonA = {
                        val nextIdx = (PREVIEW_MODES.indexOf(previewMode) + 1) % PREVIEW_MODES.size
                        previewMode = PREVIEW_MODES[nextIdx]
                    },
                    onButtonB = {
                        animFrame = (animFrame + 1) % 12
                        selectedFrameIndex = animFrame
                    },
                    onButtonC = {
                        previewMode = "IDLE"
                        animFrame = 0
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SpriteCanvasRenderer(
                            speciesId = selectedSpeciesId,
                            frame = SpriteFrame.fromIndex(animFrame),
                            size = 106.dp
                        )
                        Text(
                            text = SPRITE_GRID_FRAMES[animFrame].name.uppercase(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF81C784),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 6-Mode Animation Controls (scrollable row)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PREVIEW_MODES.forEach { mode ->
                        val active = previewMode == mode
                        Button(
                            onClick = { previewMode = mode },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (active) Color(0xFF00897B) else Color(0xFF263238)
                            )
                        ) {
                            Text(mode, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Evolution & DNA Jogress Encyclopedia Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ EVOLUTION, STARTER EGG LINEAGE & JOGRESS GUIDE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWatchPalette.CrestGold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Starter Egg Lineage Path
                val eggLineage = remember(selectedSpeciesId) {
                    com.digiwatch.core.engine.EvolutionEngine.findEggLineagePath(selectedSpeciesId).map { PetSpecies.getById(it) }
                }
                if (eggLineage.isNotEmpty()) {
                    Text(
                        text = "STARTER EGG LINEAGE PATH (CLICK TO JUMP):",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E5FF),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        eggLineage.forEachIndexed { idx, stepSp ->
                            val isCurrent = stepSp.id == selectedSpeciesId
                            Row(
                                modifier = Modifier
                                    .background(
                                        if (isCurrent) Color(0xFF14363D) else DigiWatchPalette.CardInner,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isCurrent) Color(0xFF00E5FF) else Color(0xFF263238),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedSpeciesId = stepSp.id }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SpriteCanvasRenderer(speciesId = stepSp.id, frame = SpriteFrame.IDLE_A, size = 22.dp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${if (idx > 0) "→ " else "🥚 "}${stepSp.name} (${stepSp.stage.displayName})",
                                    fontSize = 9.5.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) Color(0xFF80D8FF) else Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Pre-evolutions
                if (preEvolutions.isNotEmpty()) {
                    Text("EVOLVES FROM (${preEvolutions.size} SOURCES):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF90CAF9), fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        preEvolutions.forEach { pre ->
                            val rule = pre.evolutionRules.firstOrNull { it.targetSpeciesId == selectedSpeciesId }
                            Row(
                                modifier = Modifier
                                    .background(DigiWatchPalette.CardInner, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF263238), RoundedCornerShape(8.dp))
                                    .clickable { selectedSpeciesId = pre.id }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SpriteCanvasRenderer(speciesId = pre.id, frame = SpriteFrame.IDLE_A, size = 24.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text("← ${pre.name} (${pre.stage.displayName})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    rule?.let {
                                        Text(
                                            text = com.digiwatch.core.engine.EvolutionEngine.describeRule(it),
                                            fontSize = 8.5.sp,
                                            color = Color(0xFFB0BEC5),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Next evolutions
                Text("EVOLVES INTO:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF81C784), fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                if (selectedSpecies.evolutionRules.isEmpty()) {
                    Text("Apex Stage — No standard evolution branches (check DNA Jogress below).", fontSize = 10.sp, color = Color.Gray)
                } else {
                    selectedSpecies.evolutionRules.forEach { rule ->
                        val target = PetSpecies.getById(rule.targetSpeciesId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(DigiWatchPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF222E3C), RoundedCornerShape(8.dp))
                                .clickable { selectedSpeciesId = target.id }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SpriteCanvasRenderer(speciesId = target.id, frame = SpriteFrame.IDLE_A, size = 34.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "→ #${target.id} ${target.name} (${target.stage.displayName} • ${target.attribute.displayName})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = com.digiwatch.core.engine.EvolutionEngine.describeRule(rule),
                                        fontSize = 9.sp,
                                        color = Color(0xFF80CBC4),
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            Text("VIEW ▶", fontSize = 9.sp, color = DigiWatchPalette.DigiviceCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // DNA Digivolution / DigiXros Recipes
                if (relatedFusions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("DNA DIGIVOLUTION / DIGIXROS:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DigiWatchPalette.CrestGold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(4.dp))
                    relatedFusions.forEach { recipe ->
                        val spA = PetSpecies.getById(recipe.primarySpeciesId)
                        val spB = PetSpecies.getById(recipe.secondarySpeciesId)
                        val target = PetSpecies.getById(recipe.targetSpeciesId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(Color(0xFF152230), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(recipe.fusionType.badgeColorHex), RoundedCornerShape(8.dp))
                                .clickable { selectedSpeciesId = target.id }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "⚡ ${spA.name} + ${spB.name} = ${target.name}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(recipe.fusionType.badgeColorHex)
                                )
                                Text(text = recipe.description, fontSize = 9.sp, color = Color.LightGray)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            SpriteCanvasRenderer(speciesId = target.id, frame = SpriteFrame.IDLE_A, size = 36.dp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3x4 Grid Layout Inspector (12 full-color frames)
        Text(
            text = "▸ SPRITE SHEET MATRIX (3 COLUMNS x 4 ROWS)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = DigiWatchPalette.DigiviceCyan,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (row in 0..3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (col in 0..2) {
                    val frameIndex = row * 3 + col
                    val frameMeta = SPRITE_GRID_FRAMES[frameIndex]
                    val isSelected = selectedFrameIndex == frameIndex

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {
                                selectedFrameIndex = frameIndex
                                animFrame = frameIndex
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF16324A) else DigiWatchPalette.CardSurface
                        ),
                        border = BorderStroke(1.dp, if (isSelected) DigiWatchPalette.DigiviceCyan else DigiWatchPalette.CardBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "[$row,$col] #${frameIndex}",
                                fontSize = 8.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            SpriteCanvasRenderer(
                                speciesId = selectedSpeciesId,
                                frame = SpriteFrame.fromIndex(frameIndex),
                                size = 48.dp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = frameMeta.name,
                                fontSize = 8.sp,
                                maxLines = 1,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Functional Action Buttons: Morph Active Partner or Deposit to Vault
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        val morphedState = PetState(
                            speciesId = selectedSpecies.id,
                            currentHp = selectedSpecies.baseHp,
                            maxHp = selectedSpecies.baseHp,
                            hunger = 4,
                            strength = 4,
                            weight = selectedSpecies.baselineWeight,
                            effort = 4,
                            victories = 10,
                            totalBattles = 12
                        )
                        db.phoneDao().insertSnapshot(
                            SyncedPetEntity(
                                syncedTimestampUtc = System.currentTimeMillis() / 1000L,
                                speciesId = morphedState.speciesId,
                                speciesName = selectedSpecies.name,
                                currentHp = morphedState.currentHp,
                                maxHp = morphedState.maxHp,
                                hunger = morphedState.hunger,
                                strength = morphedState.strength,
                                weight = morphedState.weight,
                                ageSeconds = morphedState.ageSeconds,
                                careMistakes = morphedState.careMistakes,
                                effort = morphedState.effort,
                                overfeedCount = morphedState.overfeedCount,
                                totalBattles = morphedState.totalBattles,
                                victories = morphedState.victories
                            )
                        )
                        val sentToWatch = wearableManager.sendPetStateToWatch(morphedState)
                        DigiWatchAppWidgetProvider.updateAllWidgets(context)
                        syncMessage = if (sentToWatch) {
                            "Morphed active partner to ${selectedSpecies.name} & synced to Watch!"
                        } else {
                            "Set ${selectedSpecies.name} on Phone Dashboard & Widget (Watch offline)"
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Morph", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("MORPH PARTNER", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        db.phoneDao().insertVaultPet(
                            StoredDigimonEntity(
                                speciesId = selectedSpecies.id,
                                speciesName = selectedSpecies.name,
                                stageName = selectedSpecies.stage.displayName,
                                attributeName = selectedSpecies.attribute.displayName,
                                currentHp = selectedSpecies.baseHp,
                                maxHp = selectedSpecies.baseHp,
                                hunger = 4,
                                strength = 4,
                                weight = selectedSpecies.baselineWeight,
                                ageSeconds = 3600L,
                                careMistakes = 0,
                                effort = 4,
                                overfeedCount = 0,
                                totalBattles = 10,
                                victories = 8,
                                notes = "Synthesized via DigiDex Sprite Studio"
                            )
                        )
                        syncMessage = "Added ${selectedSpecies.name} to DigiVault Archive!"
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Archive, contentDescription = "Vault", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ADD TO VAULT", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }

        syncMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "▸ $it", fontSize = 11.sp, color = Color(0xFF00E676), fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun StageFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFF00897B) else DigiWatchPalette.CardSurface,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                if (isSelected) Color(0xFF80CBC4) else DigiWatchPalette.CardBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color.LightGray,
            fontFamily = FontFamily.Monospace
        )
    }
}
