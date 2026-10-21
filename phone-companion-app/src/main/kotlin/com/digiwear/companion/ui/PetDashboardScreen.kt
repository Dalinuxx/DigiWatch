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
    var currentShellStyle by remember { mutableStateOf(DigiviceShellStyle.ORIGINAL_1997_GREY) }

    // Watch Settings State (100% parity with Watch SettingsScreen)
    var selectedTheme by remember { mutableStateOf(BackgroundTheme.CLASSIC_VPET_LCD) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(false) }
    var threeButtonMode by remember { mutableStateOf(true) }
    var buttonPlacement by remember { mutableStateOf("RIGHT") }
    var pendulumShakeMode by remember { mutableStateOf(false) }
    var nfcBridgeMode by remember { mutableStateOf("NFC_DUEL") }
    var displayShapeOverride by remember { mutableStateOf("AUTO") }
    var dailyStepGoal by remember { mutableIntStateOf(5000) }
    var contrastLevel by remember { mutableStateOf("MEDIUM") }
    var sleepSchedule by remember { mutableStateOf("21:00_07:00") }
    var ambientStyle by remember { mutableStateOf("MINIMAL_DOT") }
    var hazardAlertsEnabled by remember { mutableStateOf(true) }
    var clockFormat24h by remember { mutableStateOf(false) }
    var aospWatchIp by remember { mutableStateOf(wearableManager.getConfiguredAospWatchIp()) }
    var showPartnerCutIn by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val species = remember(petState.speciesId) { PetSpecies.getById(petState.speciesId) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val buzzerManager = remember { com.digiwear.companion.sound.BuzzerManager(context) }

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
            watchSettings["vpet_button_placement"]?.let { buttonPlacement = it }
            watchSettings["pendulum_shake_mode"]?.let { pendulumShakeMode = it.toBoolean() }
            watchSettings["sound_enabled"]?.let { soundEnabled = it.toBoolean(); buzzerManager.isEnabled = it.toBoolean() }
            watchSettings["nfc_bridge_mode"]?.let { nfcBridgeMode = it }
            watchSettings["display_shape_override"]?.let { displayShapeOverride = it }
            watchSettings["daily_step_goal"]?.toIntOrNull()?.let { dailyStepGoal = it }
            watchSettings["lcd_contrast_level"]?.let { contrastLevel = it }
            watchSettings["sleep_schedule"]?.let { sleepSchedule = it }
            watchSettings["ambient_display_style"]?.let { ambientStyle = it }
            watchSettings["hazard_alerts_enabled"]?.let { hazardAlertsEnabled = it.toBoolean() }
            watchSettings["clock_format_24h"]?.let { clockFormat24h = it.toBoolean() }
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

    Box(modifier = Modifier.fillMaxSize()) {
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
            watchSettings["vpet_button_placement"]?.let { buttonPlacement = it }
                                    watchSettings["pendulum_shake_mode"]?.let { pendulumShakeMode = it.toBoolean() }
                                    watchSettings["sound_enabled"]?.let { soundEnabled = it.toBoolean(); buzzerManager.isEnabled = it.toBoolean() }
                                    watchSettings["nfc_bridge_mode"]?.let { nfcBridgeMode = it }
                                    watchSettings["display_shape_override"]?.let { displayShapeOverride = it }
                                    watchSettings["daily_step_goal"]?.toIntOrNull()?.let { dailyStepGoal = it }
                                    watchSettings["lcd_contrast_level"]?.let { contrastLevel = it }
                                    watchSettings["sleep_schedule"]?.let { sleepSchedule = it }
                                    watchSettings["ambient_display_style"]?.let { ambientStyle = it }
                                    watchSettings["hazard_alerts_enabled"]?.let { hazardAlertsEnabled = it.toBoolean() }
                                    watchSettings["clock_format_24h"]?.let { clockFormat24h = it.toBoolean() }
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

        // Digivice Shell Edition Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DigiviceShellStyle.entries.forEach { style ->
                val isSel = currentShellStyle == style
                Box(
                    modifier = Modifier
                        .background(
                            if (isSel) style.borderColor.copy(alpha = 0.25f) else Color(0xFF141C24),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isSel) style.borderColor else Color(0xFF263238),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { currentShellStyle = style }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = style.shortName,
                        fontSize = 9.sp,
                        fontWeight = if (isSel) FontWeight.Black else FontWeight.Normal,
                        color = if (isSel) style.borderColor else Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Authentic Bandai V-Pet & Digivice Shell Viewport
        VPetBrickShellFrame(
            shellStyle = currentShellStyle,
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
                    .padding(8.dp)
                    .clickable { showPartnerCutIn = true },
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
                
                if (petState.isFrozen) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x3300E5FF), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "COLD SLEEP STASIS ACTIVE • ALL DECAY & AGING PAUSED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF80D8FF),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                SegmentedGaugeRow(
                    label = "HUNGER",
                    filled = petState.hunger,
                    activeColor = Color(0xFFFF5252),
                    symbolFilled = "HEART",
                    symbolEmpty = "EMPTY"
                )
                SegmentedGaugeRow(
                    label = "STRENGTH",
                    filled = petState.strength,
                    activeColor = Color(0xFFFFCA28),
                    symbolFilled = "STAR",
                    symbolEmpty = "EMPTY"
                )
                SegmentedGaugeRow(
                    label = "EFFORT",
                    filled = petState.effort,
                    activeColor = Color(0xFFAB47BC),
                    symbolFilled = "STAR",
                    symbolEmpty = "EMPTY"
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
                            buzzerManager.playVictoryFanfare()
                            "[DIGIVOLUTION] Partner evolved into ${evolved.name}!"
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
                            buzzerManager.playButtonBeep()
                            executeCareAction("FEED_MEAL", { PetSimulationEngine.feedMeal(it, System.currentTimeMillis() / 1000L) }, "Fed Ration Meat")
                            shellPreviewStance = 2
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigiVectorIcon(type = DigiIconType.FEED_MEAT, size = 12.dp, tint = Color(0xFFFFB74D))
                            Text("MEAT", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            buzzerManager.playButtonBeep()
                            executeCareAction("FEED_PROTEIN", { PetSimulationEngine.feedProtein(it, System.currentTimeMillis() / 1000L) }, "Fed Protein Vitamin")
                            shellPreviewStance = 2
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigiVectorIcon(type = DigiIconType.PROTEIN_TABLET, size = 12.dp, tint = Color(0xFF4FC3F7))
                            Text("VITAMIN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            buzzerManager.playAttackImpactTone()
                            executeCareAction("TRAIN_WIN", { PetSimulationEngine.train(it, true, System.currentTimeMillis() / 1000L) }, "Mega Hit Training")
                            shellPreviewStance = 3
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigiVectorIcon(type = DigiIconType.TRAIN_GLOVE, size = 12.dp, tint = Color(0xFFFF8A80))
                            Text("TRAIN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            buzzerManager.playFlushTone()
                            executeCareAction("CLEAN_WASTE", { PetSimulationEngine.cleanWaste(it) }, "Flushed Bio-Waste")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigiVectorIcon(type = DigiIconType.CLEAN_FLUSH, size = 12.dp, tint = Color(0xFF80DEEA))
                            Text("CLEAN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            executeCareAction("TOGGLE_LIGHTS", { PetSimulationEngine.toggleLights(it, System.currentTimeMillis() / 1000L) }, "Toggled Sleep Lights")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigiVectorIcon(type = DigiIconType.SLEEP_LIGHT, size = 12.dp, tint = Color(0xFFFFE082))
                            Text("LIGHTS", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            buzzerManager.playHealTone()
                            executeCareAction("CURE_INJURY", { PetSimulationEngine.cureInjury(it) }, "Applied First Aid Bandage")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigiVectorIcon(type = DigiIconType.BANDAGE_CROSS, size = 12.dp, tint = Color(0xFFFFB74D))
                            Text("BANDAGE", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = {
                        buzzerManager.playHealTone()
                        executeCareAction(
                            "TOGGLE_COLD_SLEEP",
                            { PetSimulationEngine.toggleColdSleep(it, System.currentTimeMillis() / 1000L) },
                            if (!petState.isFrozen) "Cold Sleep Active (Paused)" else "Awakened from Cold Sleep"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (petState.isFrozen) Color(0x3300E5FF) else Color.Transparent
                    ),
                    border = BorderStroke(1.dp, if (petState.isFrozen) Color(0xFF00E5FF) else Color(0xFF546E7A))
                ) {
                    Text(
                        text = if (petState.isFrozen) "WAKE UP FROM COLD SLEEP" else "COLD SLEEP / REST MODE (PAUSE DECAY)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (petState.isFrozen) Color(0xFF00E5FF) else Color(0xFF80D8FF),
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showStarterEggPicker = !showStarterEggPicker },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, DigiWearPalette.CrestGold)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DigiVectorIcon(type = DigiIconType.EGG, size = 12.dp, tint = DigiWearPalette.CrestGold)
                        Text(
                            text = if (showStarterEggPicker) "HIDE DIGI-EGG INCUBATOR" else "HATCH NEW STARTER DIGI-EGG (16 CANONICAL LINEAGES)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DigiWearPalette.CrestGold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
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
                                            "Hatched ${eggSp.name}!"
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        DigiVectorIcon(type = DigiIconType.DNA_HELIX, size = 12.dp, tint = Color(0xFFEA80FC))
                        Text(
                            text = "DNA JOGRESS, DIGIXROS & MODE CHANGE CHAMBER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA80FC),
                            fontFamily = FontFamily.Monospace
                        )
                    }
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
                                        syncStatus = "[JOGRESS] ${recipe.fusionType.displayName}: Awakened ${targetSp.name}! (${if (beamed) "Synced to Watch" else "Saved on Phone"})"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(recipe.fusionType.badgeColorHex))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    DigiVectorIcon(type = DigiIconType.DNA_HELIX, size = 11.dp, tint = Color.Black)
                                    Text(
                                        text = "AWAKEN ${targetSp.name.uppercase()} (${recipe.fusionType.displayName.uppercase()}) & BEAM TO WATCH",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        DigiVectorIcon(type = DigiIconType.DNA_HELIX, size = 11.dp, tint = Color(0xFFEA80FC))
                        Text(
                            text = if (showAllJogressCatalog) "HIDE ${com.digiwear.core.fusion.JogressEngine.RECIPES.size}-RECIPE JOGRESS / DIGIXROS MATRIX" else "BROWSE ALL ${com.digiwear.core.fusion.JogressEngine.RECIPES.size} DNA JOGRESS, DIGIXROS & MODE CHANGE RECIPES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA80FC),
                            fontFamily = FontFamily.Monospace
                        )
                    }
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
                                        syncStatus = "[JOGRESS] Awakened ${targetSp.name} (${recipe.fusionType.displayName})! (${if (beamed) "Synced to Watch" else "Phone Local"})"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(recipe.fusionType.badgeColorHex))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    DigiVectorIcon(type = DigiIconType.DNA_HELIX, size = 10.dp, tint = Color.Black)
                                    Text("FUSE", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                                }
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

                // Haptic Feedback Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "HAPTIC FEEDBACK", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                        Text(text = "Vibrations on watch for steps, attacks & evolutions", fontSize = 9.5.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = { vibrationEnabled = it }
                    )
                }

                // Chiptune Buzzer Sound FX Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "CHIPTUNE BUZZER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                        Text(text = "8-Bit piezo square-wave LCD beeps & battle fanfare", fontSize = 9.5.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = soundEnabled,
                        onCheckedChange = { soundEnabled = it }
                    )
                }

                // 3-Button LCD Controls Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "3-BUTTON LCD CONTROLS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                        Text(text = "[A•SEL] [B•OK] [C•ESC] LCD bar + Rotary/Stem hardware keys", fontSize = 9.5.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = threeButtonMode,
                        onCheckedChange = { threeButtonMode = it }
                    )
                }

                // Training Method Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "TRAINING METHOD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                        Text(text = if (pendulumShakeMode) "Pendulum Shake (5-8 hits)" else "Timing QTE (Default)", fontSize = 9.5.sp, color = if (pendulumShakeMode) Color(0xFFFFD54F) else Color.Gray)
                    }
                    Switch(
                        checked = pendulumShakeMode,
                        onCheckedChange = { pendulumShakeMode = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // LCD Contrast Level Selector
                Text(
                    text = "LCD CONTRAST LEVEL:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD54F),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val contrastChoices = listOf(
                        "LOW" to "Soft Matrix",
                        "MEDIUM" to "Standard (Def)",
                        "HIGH" to "Deep Crisp"
                    )
                    contrastChoices.forEach { (levelId, label) ->
                        val active = contrastLevel == levelId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (active) Color(0xFFE65100) else DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(1.dp, if (active) Color(0xFFFFB74D) else Color(0xFF263238), RoundedCornerShape(8.dp))
                                .clickable { contrastLevel = levelId }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Color.LightGray, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Daily Step Target Selector
                Text(
                    text = "DAILY STEP TARGET:",
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
                    val stepChoices = listOf(3000, 5000, 7500, 10000)
                    stepChoices.forEach { goal ->
                        val active = dailyStepGoal == goal
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (active) Color(0xFF00838F) else DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(1.dp, if (active) Color(0xFF4DD0E1) else Color(0xFF263238), RoundedCornerShape(8.dp))
                                .clickable { dailyStepGoal = goal }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${goal / 1000}K", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Color.LightGray, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sleep Schedule Selector
                Text(
                    text = "SLEEP SCHEDULE:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB39DDB),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val sleepChoices = listOf(
                        "20:00_07:00" to "20–07 Early",
                        "21:00_07:00" to "21–07 Standard",
                        "22:00_08:00" to "22–08 Owl",
                        "23:00_08:00" to "23–08 Late"
                    )
                    sleepChoices.forEach { (schedId, label) ->
                        val active = sleepSchedule == schedId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (active) Color(0xFF4527A0) else DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(1.dp, if (active) Color(0xFF9575CD) else Color(0xFF263238), RoundedCornerShape(8.dp))
                                .clickable { sleepSchedule = schedId }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Color.LightGray, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Always-On Ambient Display Style Selector
                Text(
                    text = "ALWAYS-ON AMBIENT STYLE:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF81C784),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val ambChoices = listOf(
                        "MINIMAL_DOT" to "Minimal Dot",
                        "OUTLINE" to "Outline Wire",
                        "DIMMED_LCD" to "Dimmed Retro"
                    )
                    ambChoices.forEach { (styleId, label) ->
                        val active = ambientStyle == styleId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (active) Color(0xFF2E7D32) else DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(1.dp, if (active) Color(0xFF81C784) else Color(0xFF263238), RoundedCornerShape(8.dp))
                                .clickable { ambientStyle = styleId }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = label, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Color.LightGray, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Clock Format Toggle (12H vs 24H)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "CLOCK TIME FORMAT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                        Text(text = if (clockFormat24h) "24-Hour Military (13:00)" else "12-Hour Standard (1:00 PM)", fontSize = 9.5.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = clockFormat24h,
                        onCheckedChange = { clockFormat24h = it }
                    )
                }

                // Environmental Hazard Notifications Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "HAZARD NOTIFICATIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                        Text(text = "Alert when entering X-Program Fog & Miasma zones", fontSize = 9.5.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = hazardAlertsEnabled,
                        onCheckedChange = { hazardAlertsEnabled = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // NFC Tap Bridge Mode Selector (Parity with Watch SettingsScreen)
                Text(
                    text = "NFC TAP BRIDGE MODE:",
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
                        Triple("NFC_DUEL", DigiIconType.NFC_WAVE, "Tap-to-Duel"),
                        Triple("WILD_SCAN", DigiIconType.RADAR_SCAN, "Wild Scan"),
                        Triple("ACOM_RELAY", DigiIconType.GLOBE_NET, "Phone A-Com")
                    )
                    nfcOptions.forEach { opt ->
                        val modeId = opt.first
                        val iconType = opt.second
                        val label = opt.third
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
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                DigiVectorIcon(type = iconType, size = 10.dp, tint = if (active) Color(0xFF00E676) else Color.LightGray)
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
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Watch Display Shape Override (for Round AOSP Watches reporting Square ROMs)
                Text(
                    text = "SCREEN DISPLAY SHAPE:",
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
                        Triple("AUTO", DigiIconType.SETTINGS_GEAR, "Auto-Detect"),
                        Triple("ROUND", DigiIconType.WATCH_DEVICE, "Round Bezel"),
                        Triple("SQUARE", DigiIconType.VAULT_SAFE, "Square LCD")
                    )
                    shapeOptions.forEach { opt ->
                        val shapeId = opt.first
                        val iconType = opt.second
                        val label = opt.third
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
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                DigiVectorIcon(type = iconType, size = 10.dp, tint = if (active) Color(0xFF82B1FF) else Color.LightGray)
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DigiVectorIcon(type = DigiIconType.WATCH_DEVICE, size = 12.dp, tint = Color(0xFF80D8FF))
                        Text(
                            text = "AOSP / ANDROID WATCH WI-FI & HOTSPOT BRIDGE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF80D8FF),
                            fontFamily = FontFamily.Monospace
                        )
                    }
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
                                        syncStatus = "[OK] Linked to AOSP Watch at $foundIp:45820!"
                                    } else if (aospWatchIp.isNotBlank()) {
                                        wearableManager.setConfiguredAospWatchIp(aospWatchIp)
                                        val state = wearableManager.requestPetStateFromWatch()
                                        if (state != null) {
                                            petState = state
                                            persistPhoneSnapshot(state)
                                            syncStatus = "[OK] Linked to AOSP Watch at $aospWatchIp:45820!"
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
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                DigiVectorIcon(type = DigiIconType.SEARCH_LENS, size = 10.dp, tint = Color.White)
                                Text("LINK AOSP", fontSize = 9.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                            }
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
                                displayShapeOverride = displayShapeOverride,
                                dailyStepGoal = dailyStepGoal,
                                contrastLevel = contrastLevel,
                                sleepSchedule = sleepSchedule,
                                ambientStyle = ambientStyle,
                                hazardAlertsEnabled = hazardAlertsEnabled,
                                clockFormat24h = clockFormat24h
                            )
                            syncStatus = if (ok) "All Settings applied to Smartwatch!" else "Saved locally (Smartwatch offline)"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "Apply", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("APPLY SETTINGS TO SMARTWATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
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
                        val readiness = remember(petState, rule) { EvolutionEngine.evaluateConditionDetails(petState, rule) }
                        val targetSpecies = readiness.targetSpecies
                        val matches = readiness.overallMet
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
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .background(Color(0xFF111720), RoundedCornerShape(2.dp))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(readiness.completionPercentage / 100f)
                                                .height(4.dp)
                                                .background(
                                                    if (matches) Color(0xFF00E676) else Color(0xFFFFCA28),
                                                    RoundedCornerShape(2.dp)
                                                )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = readiness.conditions.joinToString(" | ") { cond ->
                                            val sym = if (cond.isMet) "[OK]" else "[--]"
                                            "$sym ${cond.label}: ${cond.current}/${cond.target}"
                                        },
                                        fontSize = 9.sp,
                                        color = if (matches) Color(0xFF80CBC4) else Color(0xFF90A4AE),
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

    if (showPartnerCutIn) {
        PhoneCutInOverlay(
            speciesId = petState.speciesId,
            title = "OFFICIAL ALBUM CUT-IN",
            autoDismissMs = null,
            onDismiss = { showPartnerCutIn = false }
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
