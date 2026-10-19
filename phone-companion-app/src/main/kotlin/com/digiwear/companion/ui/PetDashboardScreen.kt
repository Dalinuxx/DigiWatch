package com.digiwear.companion.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.data.StoredDigimonEntity
import com.digiwear.companion.data.SyncedPetEntity
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.companion.render.SpriteCanvasRenderer
import com.digiwear.companion.widget.DigiWearAppWidgetProvider
import com.digiwear.core.engine.EvolutionEngine
import com.digiwear.core.engine.PetSimulationEngine
import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import com.digiwear.core.model.SpriteFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PetDashboardScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    var petState by remember { mutableStateOf(PetState(speciesId = 201, weight = 24, ageSeconds = 92000L, victories = 8, totalBattles = 10, effort = 3)) }
    var syncStatus by remember { mutableStateOf<String?>(null) }
    var isSyncing by remember { mutableStateOf(false) }
    var vaultDepositSuccess by remember { mutableStateOf<String?>(null) }
    var showStarterEggPicker by remember { mutableStateOf(false) }

    // Interactive V-Pet Brick Shell State
    var selectedShellIcon by remember { mutableIntStateOf(0) }
    var shellPreviewStance by remember { mutableIntStateOf(0) } // 0=IDLE, 1=WALK, 2=HAPPY, 3=ATTACK

    // Watch Settings State (100% parity with Watch SettingsScreen)
    var selectedTheme by remember { mutableStateOf(BackgroundTheme.CLASSIC_VPET_LCD) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(false) }
    var threeButtonMode by remember { mutableStateOf(true) }
    var pendulumShakeMode by remember { mutableStateOf(false) }
    var nfcBridgeMode by remember { mutableStateOf("NFC_DUEL") }
    var displayShapeOverride by remember { mutableStateOf("AUTO") }
    var aospWatchIp by remember { mutableStateOf(wearableManager.getConfiguredAospWatchIp()) }

    val scope = rememberCoroutineScope()
    val species = remember(petState.speciesId) { PetSpecies.getById(petState.speciesId) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val persistPhoneSnapshot: suspend (PetState) -> Unit = { updated ->
        val sp = PetSpecies.getById(updated.speciesId)
        db.phoneDao().insertSnapshot(
            SyncedPetEntity(
                syncedTimestampUtc = System.currentTimeMillis() / 1000L,
                speciesId = updated.speciesId,
                speciesName = sp.name,
                currentHp = updated.currentHp,
                maxHp = updated.maxHp,
                hunger = updated.hunger,
                strength = updated.strength,
                weight = updated.weight,
                ageSeconds = updated.ageSeconds,
                careMistakes = updated.careMistakes,
                effort = updated.effort,
                overfeedCount = updated.overfeedCount,
                totalBattles = updated.totalBattles,
                victories = updated.victories
            )
        )
        DigiWearAppWidgetProvider.updateAllWidgets(context)
    }

    // Load latest snapshot & watch settings on start if available
    LaunchedEffect(Unit) {
        val latest = db.phoneDao().getLatestSnapshot()
        if (latest != null) {
            petState = PetState(
                speciesId = latest.speciesId,
                currentHp = latest.currentHp,
                maxHp = latest.maxHp,
                hunger = latest.hunger,
                strength = latest.strength,
                weight = latest.weight,
                ageSeconds = latest.ageSeconds,
                careMistakes = latest.careMistakes,
                effort = latest.effort,
                overfeedCount = latest.overfeedCount,
                totalBattles = latest.totalBattles,
                victories = latest.victories
            )
        }
        val watchSettings = wearableManager.requestSettingsFromWatch()
        if (watchSettings != null) {
            watchSettings["theme"]?.let { selectedTheme = BackgroundTheme.fromId(it) }
            watchSettings["vibration"]?.let { vibrationEnabled = it.toBoolean() }
            watchSettings["vpet_3button_mode"]?.let { threeButtonMode = it.toBoolean() }
            watchSettings["pendulum_shake_mode"]?.let { pendulumShakeMode = it.toBoolean() }
            watchSettings["sound_enabled"]?.let { soundEnabled = it.toBoolean() }
            watchSettings["nfc_bridge_mode"]?.let { nfcBridgeMode = it }
            watchSettings["display_shape_override"]?.let { displayShapeOverride = it }
        }
        aospWatchIp = wearableManager.getConfiguredAospWatchIp()
    }

    // Sprite animation ticker
    var frameToggle by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(500L)
            frameToggle = !frameToggle
        }
    }

    val activeSpriteFrame = when (shellPreviewStance) {
        1 -> if (frameToggle) SpriteFrame.WALK_A else SpriteFrame.WALK_B
        2 -> if (frameToggle) SpriteFrame.fromIndex(4) else SpriteFrame.fromIndex(5)
        3 -> if (frameToggle) SpriteFrame.ATTACK_CHARGE else SpriteFrame.ATTACK_RELEASE
        else -> if (frameToggle) SpriteFrame.IDLE_A else SpriteFrame.IDLE_B
    }

    val stanceNames = listOf("IDLE", "WALK", "HAPPY", "ATTACK")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWearPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Digivice Section Header + Sync Watch Action
        DigiSectionHeader(
            tag = "ACTIVE PARTNER MONITOR",
            title = "#${species.id} ${species.name}",
            accentColor = DigiWearPalette.DigiviceCyan,
            trailingContent = {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isSyncing = true
                            val nodes = wearableManager.getConnectedWatchNodes()
                            if (nodes.isNotEmpty()) {
                                val liveWatchState = wearableManager.requestPetStateFromWatch()
                                if (liveWatchState != null) {
                                    petState = liveWatchState
                                }
                                val watchSettings = wearableManager.requestSettingsFromWatch()
                                if (watchSettings != null) {
                                    watchSettings["theme"]?.let { selectedTheme = BackgroundTheme.fromId(it) }
                                    watchSettings["vibration"]?.let { vibrationEnabled = it.toBoolean() }
                                    watchSettings["vpet_3button_mode"]?.let { threeButtonMode = it.toBoolean() }
                                    watchSettings["pendulum_shake_mode"]?.let { pendulumShakeMode = it.toBoolean() }
                                    watchSettings["sound_enabled"]?.let { soundEnabled = it.toBoolean() }
                                    watchSettings["nfc_bridge_mode"]?.let { nfcBridgeMode = it }
                                }
                                val syncedSpecies = PetSpecies.getById(petState.speciesId)
                                syncStatus = if (liveWatchState != null) {
                                    "Synced live ${syncedSpecies.name} & settings from Smartwatch!"
                                } else {
                                    "Connected to ${nodes.size} Smartwatch Node(s)"
                                }
                                persistPhoneSnapshot(petState)
                            } else {
                                syncStatus = "No Bluetooth Watch connected (Offline Companion Mode)"
                            }
                            isSyncing = false
                        }
                    },
                    border = BorderStroke(1.dp, DigiWearPalette.DigiviceCyan)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = DigiWearPalette.DigiviceCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSyncing) "SYNCING..." else "SYNC WATCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWearPalette.DigiviceCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Stage, Attribute & Weight Pills Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StageBadge(species.stage.displayName)
            AttributeBadge(species.attribute.displayName)
            Box(
                modifier = Modifier
                    .background(Color(0xFF172230), RoundedCornerShape(6.dp))
                    .border(1.dp, Color(0xFF37474F), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "WT: ${petState.weight}G • AGE: ${petState.ageSeconds / 3600}H",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB0BEC5),
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        syncStatus?.let {
            Text(
                text = "▸ $it",
                fontSize = 11.sp,
                color = Color(0xFF00E676),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Authentic 1997 Bandai Brick-Wall Casing & Interactive LCD Screen
        VPetBrickShellFrame(
            screenBgColor = Color(selectedTheme.primaryColorHex),
            selectedIconIndex = selectedShellIcon,
            statusLabel = "STANCE: ${stanceNames[shellPreviewStance]}",
            onButtonA = { selectedShellIcon = (selectedShellIcon + 1) % 8 },
            onButtonB = { shellPreviewStance = (shellPreviewStance + 1) % 4 },
            onButtonC = {
                selectedShellIcon = 0
                shellPreviewStance = 0
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Authentic 1997 Bandai V-Pet Dot-Matrix Pixel Sprite
                SpriteCanvasRenderer(
                    speciesId = petState.speciesId,
                    frame = activeSpriteFrame,
                    size = 112.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Vault Freeze/Deposit Action
        OutlinedButton(
            onClick = {
                scope.launch {
                    val entry = StoredDigimonEntity(
                        speciesId = petState.speciesId,
                        speciesName = species.name,
                        stageName = species.stage.displayName,
                        attributeName = species.attribute.displayName,
                        currentHp = petState.currentHp,
                        maxHp = petState.maxHp,
                        hunger = petState.hunger,
                        strength = petState.strength,
                        weight = petState.weight,
                        ageSeconds = petState.ageSeconds,
                        careMistakes = petState.careMistakes,
                        effort = petState.effort,
                        overfeedCount = petState.overfeedCount,
                        totalBattles = petState.totalBattles,
                        victories = petState.victories,
                        notes = "Frozen from Companion Dashboard"
                    )
                    db.phoneDao().insertVaultPet(entry)
                    vaultDepositSuccess = "Stored ${species.name} in DigiVault Cryo-Stasis!"
                    delay(3000L)
                    vaultDepositSuccess = null
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, if (vaultDepositSuccess != null) Color(0xFF00E676) else DigiWearPalette.DigiviceCyan),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = DigiWearPalette.DigiviceCyan
            )
        ) {
            Icon(
                imageVector = Icons.Default.Archive,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = vaultDepositSuccess ?: "FREEZE & DEPOSIT PARTNER TO DIGIVAULT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (vaultDepositSuccess != null) Color(0xFF00E676) else DigiWearPalette.DigiviceCyan
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Physiological Stat Metrics (4-Segment Block Gauges + Care Grid)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ BIOLOGICAL TELEMETRY & CARE METRICS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWearPalette.DigiviceCyan,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                SegmentedGaugeRow(
                    label = "HUNGER",
                    filled = petState.hunger,
                    activeColor = Color(0xFFFF5252),
                    symbolFilled = "♥",
                    symbolEmpty = "♡"
                )
                SegmentedGaugeRow(
                    label = "STRENGTH",
                    filled = petState.strength,
                    activeColor = Color(0xFFFFCA28),
                    symbolFilled = "★",
                    symbolEmpty = "☆"
                )
                SegmentedGaugeRow(
                    label = "EFFORT",
                    filled = petState.effort,
                    activeColor = Color(0xFFAB47BC),
                    symbolFilled = "★",
                    symbolEmpty = "☆"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 3-Column Vitals Telemetry Tiles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TelemetryTile(
                        modifier = Modifier.weight(1f),
                        label = "MISTAKES",
                        value = "${petState.careMistakes}",
                        valueColor = if (petState.careMistakes == 0) Color(0xFF00E676) else Color(0xFFFF8A80)
                    )
                    TelemetryTile(
                        modifier = Modifier.weight(1f),
                        label = "OVERFEED",
                        value = "${petState.overfeedCount}",
                        valueColor = Color.White
                    )
                    val winPct = if (petState.totalBattles > 0) (petState.victories * 100) / petState.totalBattles else 0
                    TelemetryTile(
                        modifier = Modifier.weight(1f),
                        label = "WIN RATIO",
                        value = "$winPct% (${petState.victories}W)",
                        valueColor = Color(0xFF81C784)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remote Care & Egg Hatch Deck Card (executes on Phone & Paired Watch)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ REMOTE V-PET CARE & DIGI-EGG DECK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E676),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                val executeCareAction: (String, (PetState) -> PetState, String) -> Unit = { remoteCmd, localTransform, label ->
                    scope.launch {
                        val watchUpdated = wearableManager.sendRemoteCareCommand(remoteCmd)
                        val nextState = watchUpdated ?: localTransform(petState)
                        val evolved = EvolutionEngine.evaluateEvolution(nextState)
                        val finalState = if (evolved != null) {
                            EvolutionEngine.evolve(nextState, evolved, System.currentTimeMillis() / 1000L)
                        } else nextState
                        petState = finalState
                        persistPhoneSnapshot(finalState)
                        if (watchUpdated == null) {
                            wearableManager.sendPetStateToWatch(finalState)
                        }
                        syncStatus = if (evolved != null) {
                            "✨ DIGIVOLUTION! Partner evolved into ${evolved.name}!"
                        } else {
                            "$label applied (${if (watchUpdated != null) "Watch + Phone" else "Phone Local"})"
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            executeCareAction("FEED_MEAL", { PetSimulationEngine.feedMeal(it, System.currentTimeMillis() / 1000L) }, "🍖 Fed Ration Meat")
                            shellPreviewStance = 2
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🍖 MEAT", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            executeCareAction("FEED_PROTEIN", { PetSimulationEngine.feedProtein(it, System.currentTimeMillis() / 1000L) }, "💊 Fed Protein Vitamin")
                            shellPreviewStance = 2
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("💊 VITAMIN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            executeCareAction("TRAIN_WIN", { PetSimulationEngine.train(it, true, System.currentTimeMillis() / 1000L) }, "🥊 Mega Hit Training")
                            shellPreviewStance = 3
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🥊 TRAIN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            executeCareAction("CLEAN_WASTE", { PetSimulationEngine.cleanWaste(it) }, "🧹 Flushed Bio-Waste")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🧹 CLEAN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            executeCareAction("TOGGLE_LIGHTS", { PetSimulationEngine.toggleLights(it, System.currentTimeMillis() / 1000L) }, "💡 Toggled Sleep Lights")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("💡 LIGHTS", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {
                            executeCareAction("CURE_INJURY", { PetSimulationEngine.cureInjury(it) }, "💉 Administered Med-Shot")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("💉 HEAL", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showStarterEggPicker = !showStarterEggPicker },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, DigiWearPalette.CrestGold)
                ) {
                    Text(
                        text = if (showStarterEggPicker) "HIDE DIGI-EGG INCUBATOR" else "🥚 HATCH NEW STARTER DIGI-EGG (10 LINEAGES)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWearPalette.CrestGold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (showStarterEggPicker) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        EvolutionEngine.STARTER_EGG_IDS.forEach { eggId ->
                            val eggSp = PetSpecies.getById(eggId)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .background(DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF455A64), RoundedCornerShape(8.dp))
                                    .clickable {
                                        executeCareAction(
                                            "HATCH_EGG:$eggId",
                                            {
                                                PetState(
                                                    speciesId = eggId,
                                                    currentHp = eggSp.baseHp,
                                                    maxHp = eggSp.baseHp,
                                                    hunger = 4,
                                                    strength = 4,
                                                    weight = eggSp.baselineWeight
                                                )
                                            },
                                            "🥚 Hatched ${eggSp.name}!"
                                        )
                                        showStarterEggPicker = false
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                SpriteCanvasRenderer(speciesId = eggId, frame = SpriteFrame.IDLE_A, size = 32.dp)
                                Text(eggSp.name, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // DNA Jogress, DigiXros & Mode Change Chamber Card
        val activeJogressRecipes = remember(species.id) {
            com.digiwear.core.fusion.JogressEngine.getCompatibleRecipesFor(species.id)
        }
        var showAllJogressCatalog by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFAB47BC))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "▸ ⚡ DNA JOGRESS, DIGIXROS & MODE CHANGE CHAMBER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA80FC),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (activeJogressRecipes.isNotEmpty()) "${activeJogressRecipes.size} READY" else "${com.digiwear.core.fusion.JogressEngine.RECIPES.size} RECIPES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = DigiWearPalette.CrestGold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (activeJogressRecipes.isNotEmpty()) {
                    Text(
                        text = "Your active ${species.name} is compatible with ${activeJogressRecipes.size} canonical DNA Jogress / DigiXros / Mode Change awakening(s):",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    activeJogressRecipes.forEach { recipe ->
                        val partnerId = if (recipe.primarySpeciesId == species.id) recipe.secondarySpeciesId else recipe.primarySpeciesId
                        val partnerSp = PetSpecies.getById(partnerId)
                        val targetSp = PetSpecies.getById(recipe.targetSpeciesId)
                        val isSelfMode = recipe.primarySpeciesId == recipe.secondarySpeciesId

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color(0xFF1A1426), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(recipe.fusionType.badgeColorHex), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    SpriteCanvasRenderer(speciesId = species.id, frame = SpriteFrame.IDLE_A, size = 36.dp)
                                    Text(if (isSelfMode) " ══▶ " else " + ", fontSize = 13.sp, fontWeight = FontWeight.Black, color = DigiWearPalette.CrestGold)
                                    if (!isSelfMode) {
                                        SpriteCanvasRenderer(speciesId = partnerSp.id, frame = SpriteFrame.IDLE_A, size = 36.dp)
                                        Text(" ══▶ ", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF00E5FF))
                                    }
                                    SpriteCanvasRenderer(speciesId = targetSp.id, frame = SpriteFrame.IDLE_A, size = 42.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${targetSp.name} (#${targetSp.id})",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF69F0AE)
                                        )
                                        Text(
                                            text = if (isSelfMode) "${recipe.fusionType.displayName} Awakening" else "+ #${partnerSp.id} ${partnerSp.name} (${recipe.fusionType.displayName})",
                                            fontSize = 10.sp,
                                            color = Color(recipe.fusionType.badgeColorHex),
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = recipe.description,
                                            fontSize = 9.5.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        val fused = com.digiwear.core.fusion.JogressEngine.executeFusion(
                                            primary = petState,
                                            secondarySpeciesId = partnerSp.id,
                                            secondaryVictories = 15,
                                            secondaryEffort = 4
                                        )
                                        petState = fused
                                        persistPhoneSnapshot(fused)
                                        val beamed = wearableManager.sendPetStateToWatch(fused)
                                        syncStatus = "⚡ ${recipe.fusionType.displayName}: Awakened ${targetSp.name}! (${if (beamed) "Synced to Watch" else "Saved on Phone"})"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(recipe.fusionType.badgeColorHex))
                            ) {
                                Text(
                                    text = "⚡ AWAKEN ${targetSp.name.uppercase()} (${recipe.fusionType.displayName.uppercase()}) & BEAM TO WATCH",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "${species.name} (${species.stage.displayName}) has not reached its Adult/Perfect/Ultimate Jogress stage yet. Evolve your partner or browse all ${com.digiwear.core.fusion.JogressEngine.RECIPES.size} DNA Jogress & DigiXros combinations below:",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showAllJogressCatalog = !showAllJogressCatalog },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color(0xFFCE93D8))
                ) {
                    Text(
                        text = if (showAllJogressCatalog) "HIDE ${com.digiwear.core.fusion.JogressEngine.RECIPES.size}-RECIPE JOGRESS / DIGIXROS MATRIX" else "⚡ BROWSE ALL ${com.digiwear.core.fusion.JogressEngine.RECIPES.size} DNA JOGRESS, DIGIXROS & MODE CHANGE RECIPES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA80FC),
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (showAllJogressCatalog) {
                    Spacer(modifier = Modifier.height(8.dp))
                    com.digiwear.core.fusion.JogressEngine.RECIPES.forEach { recipe ->
                        val spA = PetSpecies.getById(recipe.primarySpeciesId)
                        val spB = PetSpecies.getById(recipe.secondarySpeciesId)
                        val targetSp = PetSpecies.getById(recipe.targetSpeciesId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(0.75.dp, Color(recipe.fusionType.badgeColorHex).copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                SpriteCanvasRenderer(speciesId = spA.id, frame = SpriteFrame.IDLE_A, size = 28.dp)
                                Text("+", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DigiWearPalette.CrestGold, modifier = Modifier.padding(horizontal = 2.dp))
                                SpriteCanvasRenderer(speciesId = spB.id, frame = SpriteFrame.IDLE_A, size = 28.dp)
                                Text("→", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF), modifier = Modifier.padding(horizontal = 3.dp))
                                SpriteCanvasRenderer(speciesId = targetSp.id, frame = SpriteFrame.IDLE_A, size = 32.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "${spA.name} + ${spB.name} → ${targetSp.name}",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${recipe.fusionType.displayName} • ${recipe.description}",
                                        fontSize = 9.sp,
                                        color = Color(recipe.fusionType.badgeColorHex),
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    scope.launch {
                                        val baseState = PetState(
                                            speciesId = spA.id,
                                            currentHp = spA.baseHp,
                                            maxHp = spA.baseHp,
                                            weight = spA.baselineWeight,
                                            victories = 20,
                                            totalBattles = 25,
                                            effort = 4
                                        )
                                        val fused = com.digiwear.core.fusion.JogressEngine.executeFusion(
                                            primary = baseState,
                                            secondarySpeciesId = spB.id,
                                            secondaryVictories = 20,
                                            secondaryEffort = 4
                                        )
                                        petState = fused
                                        persistPhoneSnapshot(fused)
                                        val beamed = wearableManager.sendPetStateToWatch(fused)
                                        syncStatus = "⚡ Awakened ${targetSp.name} (${recipe.fusionType.displayName})! (${if (beamed) "Synced to Watch" else "Phone Local"})"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(recipe.fusionType.badgeColorHex))
                            ) {
                                Text("⚡ FUSE", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Watch Settings & Background Customizer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Palette, contentDescription = "Themes", tint = DigiWearPalette.CrestGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "▸ SMARTWATCH SHELL & HARDWARE SETTINGS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWearPalette.CrestGold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable themes row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BackgroundTheme.entries.forEach { theme ->
                        val selected = selectedTheme == theme
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(
                                    if (selected) Color(0xFF263238) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selected) DigiWearPalette.CrestGold else Color(0xFF222E3C),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedTheme = theme }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp, 20.dp)
                                    .background(Color(theme.primaryColorHex), RoundedCornerShape(4.dp))
                                    .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = theme.displayName,
                                fontSize = 10.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) DigiWearPalette.CrestGold else Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Vibration Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Haptic Vibrations on Watch", fontSize = 12.sp, color = Color.White)
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = { vibrationEnabled = it }
                    )
                }

                // Piezo Chiptune Buzzer Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "1997 Piezo Chiptune Buzzer", fontSize = 12.sp, color = Color.White)
                        Text(text = "Synthesizes square-wave LCD beeps & fanfare on watch", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = soundEnabled,
                        onCheckedChange = { soundEnabled = it }
                    )
                }

                // 3-Button V-Pet Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "3-Button Authentic V-Pet Mode", fontSize = 12.sp, color = Color.White)
                        Text(text = "Shows [A•SEL] [B•OK] [C•ESC] LCD shell + Rotary/Stem keys", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = threeButtonMode,
                        onCheckedChange = { threeButtonMode = it }
                    )
                }

                // Pendulum Shake Training Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Pendulum Shake Training Mode", fontSize = 12.sp, color = Color.White)
                        Text(text = "Off = OG Timing QTE (Default) • On = Authentic Wrist Shake (5-8 hits)", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = pendulumShakeMode,
                        onCheckedChange = { pendulumShakeMode = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // NFC Tap Bridge Mode Selector (Parity with Watch SettingsScreen)
                Text(
                    text = "Watch NFC Tap Bridge Mode:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF80D8FF),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val nfcOptions = listOf(
                        "NFC_DUEL" to "📶 Tap-to-Duel",
                        "WILD_SCAN" to "📡 Wild Scan",
                        "ACOM_RELAY" to "🌐 Phone A-Com"
                    )
                    nfcOptions.forEach { (modeId, label) ->
                        val active = nfcBridgeMode == modeId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (active) Color(0xFF00695C) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    if (active) Color(0xFF00E676) else Color(0xFF263238),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { nfcBridgeMode = modeId }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (active) Color.White else Color.LightGray,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Watch Display Shape Override (for Round AOSP Watches reporting Square ROMs)
                Text(
                    text = "Watch Display Shape Override (Wear OS & AOSP):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWearPalette.CrestGold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val shapeOptions = listOf(
                        "AUTO" to "⚙ Auto-Detect",
                        "ROUND" to "⭕ Round Bezel",
                        "SQUARE" to "⬜ Square / Rect"
                    )
                    shapeOptions.forEach { (shapeId, label) ->
                        val active = displayShapeOverride == shapeId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (active) Color(0xFF3949AB) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    if (active) Color(0xFF82B1FF) else Color(0xFF263238),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { displayShapeOverride = shapeId }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (active) Color.White else Color.LightGray,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // AOSP & Standard Android Watch Wi-Fi / Hotspot LAN Bridge
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF141E2B), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF00B0FF), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "⌚ AOSP / ANDROID WATCH WI-FI & HOTSPOT BRIDGE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF80D8FF),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Pairs standalone AOSP Android watches (Kospet, Zeblaze, Lokmat, Lemfo, etc.) without Google Wear OS services over local Wi-Fi or Phone Hotspot (TCP 45820 / UDP 45821).",
                        fontSize = 9.5.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.OutlinedTextField(
                            value = aospWatchIp,
                            onValueChange = {
                                aospWatchIp = it
                                wearableManager.setConfiguredAospWatchIp(it)
                            },
                            label = { Text("AOSP Watch IP (optional — auto-discovers if blank)", fontSize = 9.5.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    syncStatus = "Scanning Wi-Fi / Hotspot for AOSP Watch (UDP 45821)..."
                                    val foundIp = wearableManager.discoverAospWatchOnLan(800)
                                    if (foundIp != null) {
                                        aospWatchIp = foundIp
                                        val state = wearableManager.requestPetStateFromWatch()
                                        if (state != null) {
                                            petState = state
                                            persistPhoneSnapshot(state)
                                        }
                                        syncStatus = "✅ Linked to AOSP Watch at $foundIp:45820!"
                                    } else if (aospWatchIp.isNotBlank()) {
                                        wearableManager.setConfiguredAospWatchIp(aospWatchIp)
                                        val state = wearableManager.requestPetStateFromWatch()
                                        if (state != null) {
                                            petState = state
                                            persistPhoneSnapshot(state)
                                            syncStatus = "✅ Linked to AOSP Watch at $aospWatchIp:45820!"
                                        } else {
                                            syncStatus = "Saved IP $aospWatchIp (ensure Watch app is open on same Wi-Fi/Hotspot)"
                                        }
                                    } else {
                                        syncStatus = "No AOSP Watch auto-discovered; check Watch Settings -> AOSP LAN BRIDGE IP"
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD))
                        ) {
                            Text("🔍 LINK AOSP", fontSize = 9.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Apply Settings to Smartwatch Button
                Button(
                    onClick = {
                        scope.launch {
                            val ok = wearableManager.sendSettingsToWatch(
                                themeId = selectedTheme.id,
                                vibrationEnabled = vibrationEnabled,
                                threeButtonMode = threeButtonMode,
                                pendulumShakeMode = pendulumShakeMode,
                                soundEnabled = soundEnabled,
                                nfcBridgeMode = nfcBridgeMode,
                                displayShapeOverride = displayShapeOverride
                            )
                            syncStatus = if (ok) "All Settings applied to Smartwatch!" else "Saved locally (Smartwatch offline)"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "Apply", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("APPLY SETTINGS TO DIGIWEAR", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Branching Evolution Radar with Starter Egg Lineage & Miniature Target Sprites
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ EVOLUTION BRANCHING RADAR & LINEAGE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWearPalette.CrestGold,
                    fontFamily = FontFamily.Monospace
                )
                val lineagePath = remember(species.id) { EvolutionEngine.findEggLineagePath(species.id).map { PetSpecies.getById(it) } }
                if (lineagePath.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "EGG LINEAGE: " + lineagePath.joinToString(" → ") { "${it.name} (${it.stage.displayName})" },
                        fontSize = 9.5.sp,
                        color = Color(0xFF80CBC4),
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (species.evolutionRules.isEmpty()) {
                    Text(
                        text = "Apex Form Reached (Ultimate Stage). Check DigiVault or D-Com Hub for DNA / Jogress fusions!",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                } else {
                    species.evolutionRules.forEach { rule ->
                        val targetSpecies = PetSpecies.getById(rule.targetSpeciesId)
                        val matches = EvolutionEngine.evaluateEvolution(petState)?.id == rule.targetSpeciesId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    if (matches) Color(0xFF142E24) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    if (matches) Color(0xFF00E676) else Color(0xFF222E3C),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Color(0xFF1A2822), RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0x4480CBC4), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SpriteCanvasRenderer(
                                        speciesId = targetSpecies.id,
                                        frame = SpriteFrame.IDLE_A,
                                        size = 36.dp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${targetSpecies.name} (${targetSpecies.stage.displayName})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (matches) Color(0xFF69F0AE) else Color.White
                                    )
                                    Text(
                                        text = EvolutionEngine.describeRule(rule),
                                        fontSize = 9.5.sp,
                                        color = Color(0xFFB0BEC5),
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (matches) Color(0xFF00E676) else Color(0xFF263238),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (matches) "READY" else "LOCKED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (matches) Color.Black else Color(0xFF90A4AE),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Push to Smartwatch Action
        Button(
            onClick = {
                scope.launch {
                    val ok = wearableManager.sendPetStateToWatch(petState)
                    syncStatus = if (ok) "Successfully pushed state to DigiWear!" else "Failed to send (is watch paired?)"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD))
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Push")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PUSH PET STATE TO SMARTWATCH",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TelemetryTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .background(DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF222E3C), RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF78909C),
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = valueColor,
            fontFamily = FontFamily.Monospace
        )
    }
}
