package com.digiwear.companion.ui

import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.digiwear.companion.BuildConfig
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.data.SyncedPetEntity
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.core.model.BackgroundTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Dedicated Settings and Diagnostics screen for DigiWear.
 * Purposefully isolates all watch hardware synchronization, AOSP LAN bridges,
 * Wireless ADB Sideloading, and full database JSON backups into a central control room.
 */
@Composable
fun SettingsScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current
    val buzzerManager = remember { com.digiwear.companion.sound.BuzzerManager(context) }

    // Connected Smartwatch Detection State
    var connectedWatchNodeCount by remember { mutableIntStateOf(0) }
    var hasCheckedConnection by remember { mutableStateOf(false) }
    var showSideloadDialog by remember { mutableStateOf(false) }
    var syncStatus by remember { mutableStateOf<String?>(null) }
    var isSyncing by remember { mutableStateOf(false) }

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
    var sleepSchedule by remember { mutableStateOf("CANONICAL") }
    var ambientStyle by remember { mutableStateOf("MINIMAL_DOT") }
    var hazardAlertsEnabled by remember { mutableStateOf(true) }
    var clockFormat24h by remember { mutableStateOf(false) }
    var aospWatchIp by remember { mutableStateOf("") }
    var isAutoDetectingAosp by remember { mutableStateOf(false) }

    // Backup & Restore State
    var showBackupRestoreModal by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    // Initial load
    LaunchedEffect(Unit) {
        val nodes = wearableManager.getConnectedWatchNodes()
        connectedWatchNodeCount = nodes.size
        hasCheckedConnection = true
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWearPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DigiSectionHeader(
            tag = "SETTINGS // CONTROL ROOM",
            title = "Hardware Sync & Bridges",
            subtitle = "Wear OS & AOSP configuration, wireless sideloading & backups",
            accentColor = DigiWearPalette.CrestGold
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (BuildConfig.DEBUG) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF14241E)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF00E676))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DigiVectorIcon(type = DigiIconType.SETTINGS_GEAR, size = 14.dp, tint = Color(0xFF00E676))
                    Column {
                        Text(
                            text = "DEBUG BUILD ACTIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF00E676),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Developer Sprite Studio & 12-frame matrix enabled in DigiDex tab.",
                            fontSize = 9.sp,
                            color = Color.LightGray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // --- 1. SMARTWATCH CONNECTION STATUS & DISCOVERY ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                1.dp,
                if (connectedWatchNodeCount > 0) Color(0xFF00E676) else Color(0xFFFFB74D)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    if (connectedWatchNodeCount > 0) Color(0xFF00E676) else Color(0xFFFFB74D),
                                    CircleShape
                                )
                        )
                        Column {
                            Text(
                                text = if (connectedWatchNodeCount > 0) "SMARTWATCH LINKED" else "NO WATCH DETECTED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (connectedWatchNodeCount > 0) Color(0xFF00E676) else Color(0xFFFFB74D),
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (connectedWatchNodeCount > 0)
                                    "$connectedWatchNodeCount Wear OS node(s) connected via DataLayer"
                                else
                                    "Watch app is not connected or Bluetooth is off",
                                fontSize = 9.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                isSyncing = true
                                val nodes = wearableManager.getConnectedWatchNodes()
                                connectedWatchNodeCount = nodes.size
                                val watchSettings = wearableManager.requestSettingsFromWatch()
                                if (watchSettings != null) {
                                    watchSettings["theme"]?.let { selectedTheme = BackgroundTheme.fromId(it) }
                                    watchSettings["vibration"]?.let { vibrationEnabled = it.toBoolean() }
                                    watchSettings["sound_enabled"]?.let { soundEnabled = it.toBoolean() }
                                }
                                syncStatus = "Refreshed: ${nodes.size} watch node(s) found"
                                isSyncing = false
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, DigiWearPalette.DigiviceCyan)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", modifier = Modifier.size(13.dp), tint = DigiWearPalette.DigiviceCyan)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isSyncing) "CHECKING..." else "REFRESH", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = DigiWearPalette.DigiviceCyan)
                    }
                }

                if (hasCheckedConnection && connectedWatchNodeCount == 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF261D12), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFFFB74D).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Watch, contentDescription = null, tint = Color(0xFFFFB74D), modifier = Modifier.size(14.dp))
                                Text(
                                    text = "PAIRING SUGGESTION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFB74D),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1. Ensure your Wear OS or AOSP smartwatch is powered on and Bluetooth is active.\n" +
                                        "2. On Wear OS: Pair via the Galaxy Wearable or Pixel Watch app.\n" +
                                        "3. On Standalone AOSP watches: Connect phone and watch to the same Wi-Fi network and use the LAN bridge below.\n" +
                                        "4. Need to install the app on your watch? Tap 'WIRELESS ADB SIDELOAD' below!",
                                fontSize = 9.sp,
                                color = Color(0xFFFFE082),
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. WIRELESS ADB SIDELOAD WATCH APP (PRIMARY ACTION) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF142436)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF00E5FF))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                    Text(
                        text = "WIRELESS ADB SIDELOAD TO WATCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00E5FF),
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Install or update DigiWear on your Wear OS or AOSP smartwatch straight from your phone over Wi-Fi — zero PC, USB cables, or terminal commands needed!",
                    fontSize = 9.5.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { showSideloadDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                ) {
                    DigiVectorIcon(type = DigiIconType.BOLT, size = 15.dp, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "OPEN WIRELESS ADB WATCH SIDELOADER",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 3. AOSP STANDARD WATCH WI-FI / HOTSPOT BRIDGE ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "▸ AOSP / FULL ANDROID WATCH WI-FI BRIDGE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF80CBC4),
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Direct LAN socket bridge for standalone AOSP watches lacking Google Play Services (Kospet, Lokmat, Zeblaze, etc.)",
                    fontSize = 9.5.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = aospWatchIp,
                        onValueChange = { aospWatchIp = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("192.168.1.50", fontSize = 10.sp, color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DigiWearPalette.DigiviceCyan,
                            unfocusedBorderColor = Color(0xFF37474F),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray
                        )
                    )

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                isAutoDetectingAosp = true
                                val detectedIp = wearableManager.discoverAospWatchOnLan()
                                if (detectedIp != null) {
                                    aospWatchIp = detectedIp
                                    wearableManager.setConfiguredAospWatchIp(detectedIp)
                                    syncStatus = "Discovered AOSP Watch at $detectedIp!"
                                } else {
                                    syncStatus = "No AOSP Watch broadcast detected on LAN."
                                }
                                isAutoDetectingAosp = false
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF26A69A))
                    ) {
                        Text(
                            text = if (isAutoDetectingAosp) "SCAN..." else "AUTO-DETECT",
                            fontSize = 9.5.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF26A69A),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            wearableManager.setConfiguredAospWatchIp(aospWatchIp)
                            syncStatus = "Saved AOSP Watch IP: $aospWatchIp"
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DigiWearPalette.DigiviceCyan)
                    ) {
                        Text("LINK", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 4. SMARTWATCH HARDWARE TOGGLES & DISPLAY ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "▸ SMARTWATCH HARDWARE CONTROLS & DISPLAY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigiWearPalette.DigiviceCyan,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                // LCD Theme Selector
                Text(text = "LCD BACKLIGHT THEME:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD54F), fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BackgroundTheme.entries.forEach { theme ->
                        val isSelected = selectedTheme == theme
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(theme.primaryColorHex).copy(alpha = 0.35f) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Color(theme.primaryColorHex) else Color(0xFF263238),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedTheme = theme }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = theme.displayName,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(theme.primaryColorHex) else Color.LightGray,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Settings Switch Rows (With weight(1f) to guarantee clean label line wrapping)
                SettingSwitchRow(
                    title = "HAPTIC FEEDBACK",
                    subtitle = "Tactile wrist vibration for steps, sparring hits, and evolution calls",
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it }
                )

                SettingSwitchRow(
                    title = "CHIPTUNE PIEZO BUZZER",
                    subtitle = "Retro 8-bit square-wave audio for alerts, training prompts, and care beeps",
                    checked = soundEnabled,
                    onCheckedChange = {
                        soundEnabled = it
                        buzzerManager.isEnabled = it
                    }
                )

                SettingSwitchRow(
                    title = "3-BUTTON LCD CONTROLS",
                    subtitle = if (threeButtonMode) {
                        "Virtual [A] [B] [C] side buttons enabled on watch face (1997 classic brick layout)"
                    } else {
                        "Full-screen direct touch gestures enabled (Modern tap-anywhere layout)"
                    },
                    checked = threeButtonMode,
                    onCheckedChange = { threeButtonMode = it }
                )

                // Dedicated Mode Selector for Training Method
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = "TRAINING METHOD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (pendulumShakeMode) {
                            "Pendulum Shake: Shake watch 5 to 8 times using wrist accelerometer (Digimon Pendulum)."
                        } else {
                            "Timing QTE: Tap action button when cursor hits the sweet spot zone (Digital Monster)."
                        },
                        fontSize = 9.sp,
                        color = if (pendulumShakeMode) Color(0xFFFFD54F) else Color(0xFF80D8FF),
                        lineHeight = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (!pendulumShakeMode) Color(0xFF00695C) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    if (!pendulumShakeMode) Color(0xFF64FFDA) else Color(0xFF263238),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { pendulumShakeMode = false }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "TIMING QTE",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!pendulumShakeMode) Color.White else Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (pendulumShakeMode) Color(0xFF4A148C) else DigiWearPalette.CardInner,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    if (pendulumShakeMode) Color(0xFFE040FB) else Color(0xFF263238),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { pendulumShakeMode = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PENDULUM SHAKE",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (pendulumShakeMode) Color.White else Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                SettingSwitchRow(
                    title = "24-HOUR CLOCK FORMAT",
                    subtitle = if (clockFormat24h) "24H military format on watch face (e.g. 15:45)" else "12H standard format with AM/PM (e.g. 3:45 PM)",
                    checked = clockFormat24h,
                    onCheckedChange = { clockFormat24h = it }
                )

                SettingSwitchRow(
                    title = "HAZARD ALERTS",
                    subtitle = "Warn when active sector has adverse conditions (glitch fog, radiation, blizzard)",
                    checked = hazardAlertsEnabled,
                    onCheckedChange = { hazardAlertsEnabled = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // LCD Contrast Level
                Text(text = "LCD CONTRAST LEVEL:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFB74D), fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("LOW" to "Soft Matrix", "MEDIUM" to "Standard (Def)", "HIGH" to "Deep Crisp").forEach { (levelId, label) ->
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

                Spacer(modifier = Modifier.height(10.dp))

                // Daily Step Target
                Text(text = "DAILY STEP TARGET:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF80D8FF), fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(3000, 5000, 7500, 10000).forEach { goal ->
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

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val sent = wearableManager.sendSettingsToWatch(
                                themeId = selectedTheme.id,
                                vibrationEnabled = vibrationEnabled,
                                threeButtonMode = threeButtonMode,
                                buttonPlacement = buttonPlacement,
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
                            syncStatus = if (sent) "Successfully beamed settings to Smartwatch!" else "Settings saved locally on Phone."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DigiWearPalette.DigiviceCyan)
                ) {
                    DigiVectorIcon(type = DigiIconType.BEAM_ANTENNA, size = 15.dp, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("BEAM SETTINGS TO SMARTWATCH", fontSize = 10.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 5. DATA, STORAGE & BACKUP MANAGEMENT ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFCE93D8))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "▸ DATA, STORAGE & DATABASE BACKUPS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCE93D8),
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Export complete partner vaults, DigiDex discoveries, and adventure quest state to JSON",
                    fontSize = 9.5.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showBackupRestoreModal = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFCE93D8))
                    ) {
                        Icon(Icons.Default.Archive, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("BACKUP / JSON", fontSize = 10.sp, color = Color(0xFFCE93D8), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showResetConfirmDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFFF5252))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WIPE DATA", fontSize = 10.sp, color = Color(0xFFFF5252), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        syncStatus?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "▸ $it", fontSize = 10.sp, color = Color(0xFF00E676), fontFamily = FontFamily.Monospace)
        }
    }

    // Wireless ADB Sideload Dialog
    if (showSideloadDialog) {
        Dialog(
            onDismissRequest = { showSideloadDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            WatchSideloadScreen(
                wearableManager = wearableManager,
                onClose = { showSideloadDialog = false }
            )
        }
    }

    // Backup & Restore Full Screen Modal
    if (showBackupRestoreModal) {
        Dialog(
            onDismissRequest = { showBackupRestoreModal = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DigiWearPalette.CyberBg)
                    .statusBarsPadding()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DATABASE BACKUP & RESTORE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                        OutlinedButton(
                            onClick = { showBackupRestoreModal = false },
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFF546E7A))
                        ) {
                            Text("CLOSE", fontSize = 10.sp, color = Color.White, fontFamily = FontFamily.Monospace)
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BackupRestoreScreen(db, wearableManager)
                    }
                }
            }
        }
    }

    // Wipe / Clean Data Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("Reset Companion Database?") },
            text = { Text("This will clear all local companion snapshots, cryo-vault partners, and discovered species. Smartwatch data is not affected.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            // Clear local database
                            db.clearAllTables()
                            showResetConfirmDialog = false
                            syncStatus = "Companion database reset to clean state."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                ) {
                    Text("RESET DATA", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color.Gray,
                lineHeight = 12.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = DigiWearPalette.DigiviceCyan,
                checkedTrackColor = Color(0xFF142D38)
            )
        )
    }
}
