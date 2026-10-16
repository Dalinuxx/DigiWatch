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
import com.digiwatch.companion.data.PhoneDatabase
import com.digiwatch.companion.data.StoredDigimonEntity
import com.digiwatch.companion.data.SyncedPetEntity
import com.digiwatch.companion.datalayer.PhoneWearableManager
import com.digiwatch.companion.render.SpriteCanvasRenderer
import com.digiwatch.companion.widget.DigiWatchAppWidgetProvider
import com.digiwatch.core.engine.EvolutionEngine
import com.digiwatch.core.model.BackgroundTheme
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetState
import com.digiwatch.core.model.SpriteFrame
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

    // Interactive V-Pet Brick Shell State
    var selectedShellIcon by remember { mutableIntStateOf(0) }
    var shellPreviewStance by remember { mutableIntStateOf(0) } // 0=IDLE, 1=WALK, 2=HAPPY, 3=ATTACK

    // Watch Settings State
    var selectedTheme by remember { mutableStateOf(BackgroundTheme.CLASSIC_VPET_LCD) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var threeButtonMode by remember { mutableStateOf(false) }
    var pendulumShakeMode by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val species = remember(petState.speciesId) { PetSpecies.getById(petState.speciesId) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Load latest snapshot on start if available
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
            .background(DigiWatchPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Digivice Section Header + Sync Watch Action
        DigiSectionHeader(
            tag = "ACTIVE PARTNER MONITOR",
            title = "#${species.id} ${species.name}",
            accentColor = DigiWatchPalette.DigiviceCyan,
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
                                val syncedSpecies = PetSpecies.getById(petState.speciesId)
                                syncStatus = if (liveWatchState != null) {
                                    "Synced live ${syncedSpecies.name} from Smartwatch!"
                                } else {
                                    "Connected to ${nodes.size} Smartwatch Node(s)"
                                }
                                db.phoneDao().insertSnapshot(
                                    SyncedPetEntity(
                                        syncedTimestampUtc = System.currentTimeMillis() / 1000L,
                                        speciesId = petState.speciesId,
                                        speciesName = syncedSpecies.name,
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
                                        victories = petState.victories
                                    )
                                )
                                DigiWatchAppWidgetProvider.updateAllWidgets(context)
                            } else {
                                syncStatus = "No Bluetooth Watch connected (Offline Mode)"
                            }
                            isSyncing = false
                        }
                    },
                    border = BorderStroke(1.dp, DigiWatchPalette.DigiviceCyan)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = DigiWatchPalette.DigiviceCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSyncing) "SYNCING..." else "SYNC WATCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWatchPalette.DigiviceCyan,
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
            border = BorderStroke(1.dp, if (vaultDepositSuccess != null) Color(0xFF00E676) else DigiWatchPalette.DigiviceCyan),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = DigiWatchPalette.DigiviceCyan
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
                color = if (vaultDepositSuccess != null) Color(0xFF00E676) else DigiWatchPalette.DigiviceCyan
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Physiological Stat Metrics (4-Segment Block Gauges + Care Grid)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ BIOLOGICAL TELEMETRY & CARE METRICS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWatchPalette.DigiviceCyan,
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

        // Watch Settings & Background Customizer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Palette, contentDescription = "Themes", tint = DigiWatchPalette.CrestGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "▸ SMARTWATCH SHELL & LCD THEMES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWatchPalette.CrestGold,
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
                                    if (selected) Color(0xFF263238) else DigiWatchPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selected) DigiWatchPalette.CrestGold else Color(0xFF222E3C),
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
                                color = if (selected) DigiWatchPalette.CrestGold else Color.White,
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

                // 3-Button V-Pet Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "3-Button Authentic V-Pet Mode", fontSize = 12.sp, color = Color.White)
                        Text(text = "Shows [A•SEL] [B•OK] [C•ESC] LCD shell on watch", fontSize = 10.sp, color = Color.Gray)
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

                Spacer(modifier = Modifier.height(12.dp))

                // Apply Settings to Smartwatch Button
                Button(
                    onClick = {
                        scope.launch {
                            val ok = wearableManager.sendSettingsToWatch(
                                themeId = selectedTheme.id,
                                vibrationEnabled = vibrationEnabled,
                                threeButtonMode = threeButtonMode,
                                pendulumShakeMode = pendulumShakeMode
                            )
                            syncStatus = if (ok) "Settings applied to Smartwatch!" else "Failed to apply (watch offline)"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "Apply", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("APPLY SETTINGS TO DIGIWATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Branching Evolution Radar with Miniature Target Sprites
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ EVOLUTION BRANCHING RADAR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWatchPalette.CrestGold,
                    fontFamily = FontFamily.Monospace
                )
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
                                    if (matches) Color(0xFF142E24) else DigiWatchPalette.CardInner,
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
                                        text = "CM: ${rule.minCareMistakes}-${rule.maxCareMistakes} • Btl: ${rule.minBattles}+ • Win: ${(rule.minWinRatio * 100).toInt()}%",
                                        fontSize = 10.sp,
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
                    syncStatus = if (ok) "Successfully pushed state to DigiWatch!" else "Failed to send (is watch paired?)"
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
            .background(DigiWatchPalette.CardInner, RoundedCornerShape(8.dp))
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
