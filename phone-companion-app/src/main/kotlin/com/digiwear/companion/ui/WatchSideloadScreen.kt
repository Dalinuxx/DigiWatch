package com.digiwear.companion.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.companion.sideload.SideloadConnectionState
import com.digiwear.companion.sideload.WatchApkInfo
import com.digiwear.companion.sideload.WatchApkSourceManager
import com.digiwear.companion.sideload.WirelessAdbManager
import kotlinx.coroutines.launch

@Composable
fun WatchSideloadScreen(
    wearableManager: PhoneWearableManager,
    onClose: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val adbManager = remember { WirelessAdbManager(context) }
    val apkSourceManager = remember { WatchApkSourceManager(context) }

    val connectionState by adbManager.connectionState.collectAsState()
    val watchDetails by adbManager.watchDetails.collectAsState()
    val consoleLogs by adbManager.consoleLogs.collectAsState()
    val lastError by adbManager.lastError.collectAsState()

    var watchIp by remember {
        val configured = wearableManager.getConfiguredAospWatchIp()
        mutableStateOf(if (configured.isNotEmpty()) configured else "")
    }
    var watchPort by remember { mutableStateOf("5555") }

    var selectedApk by remember { mutableStateOf<WatchApkInfo?>(apkSourceManager.getCachedApk()) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var downloadStatusText by remember { mutableStateOf("") }

    var grantPermissions by remember { mutableStateOf(true) }
    var reinstallKeepData by remember { mutableStateOf(true) }
    var autoLaunch by remember { mutableStateOf(true) }

    var isScanningLan by remember { mutableStateOf(false) }
    var showGuideDialog by remember { mutableStateOf(false) }

    // SAF Document Picker for picking APK from local storage
    val pickApkLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                adbManager.log("Importing selected APK from phone storage...")
                val result = apkSourceManager.importApkFromUri(uri)
                result.onSuccess { info ->
                    selectedApk = info
                    adbManager.log("Selected APK: ${info.displayName} (${info.sizeFormatted})")
                }.onFailure { err ->
                    adbManager.log("Failed to import APK: ${err.message}")
                }
            }
        }
    }

    // Auto-detect asset APK if available on startup
    LaunchedEffect(Unit) {
        if (selectedApk == null) {
            val assetApk = apkSourceManager.getEmbeddedAssetApk()
            if (assetApk != null) {
                selectedApk = assetApk
                adbManager.log("Detected pre-packaged APK in assets: ${assetApk.displayName}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWearPalette.CyberBg)
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (onClose != null) {
            OutlinedButton(
                onClick = onClose,
                border = BorderStroke(1.dp, DigiWearPalette.DigiviceCyan),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DigiWearPalette.DigiviceCyan),
                shape = RoundedCornerShape(6.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("← BACK TO SETTINGS", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }

        // --- 1. Header & Terminal Info ---
        Card(
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WIRELESS ADB // SIDELOAD",
                            color = DigiWearPalette.DigiviceCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Install & Update DigiWear on Watch (No PC Required)",
                            color = Color(0xFF90A4AE),
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    OutlinedButton(
                        onClick = { showGuideDialog = true },
                        border = BorderStroke(1.dp, DigiWearPalette.CrestGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DigiWearPalette.CrestGold)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Guide", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("GUIDE", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // RSA Fingerprint Display
                Surface(
                    color = Color(0xFF0F151F),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "PHONE RSA-2048 ADB FINGERPRINT:",
                            color = Color(0xFF78909C),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = adbManager.getPublicKeyFingerprint(),
                            color = Color(0xFF80CBC4),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // --- 2. Step 1: Smartwatch Connection Panel ---
        Card(
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1. TARGET SMARTWATCH",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    // Connection Badge
                    val (badgeText, badgeBg, badgeFg) = when (connectionState) {
                        SideloadConnectionState.IDLE -> Triple("DISCONNECTED", Color(0xFF263238), Color(0xFF90A4AE))
                        SideloadConnectionState.CONNECTING -> Triple("CONNECTING...", Color(0xFF3E2723), Color(0xFFFFB74D))
                        SideloadConnectionState.AUTHENTICATING -> Triple("CHECK WATCH!", Color(0xFF0D47A1), Color(0xFF40C4FF))
                        SideloadConnectionState.CONNECTED -> Triple("CONNECTED", Color(0xFF1B5E20), Color(0xFF69F0AE))
                        SideloadConnectionState.TRANSFERRING -> Triple("STREAMING...", Color(0xFF1B5E20), Color(0xFF69F0AE))
                        SideloadConnectionState.INSTALLING -> Triple("INSTALLING...", Color(0xFF1B5E20), Color(0xFF69F0AE))
                        SideloadConnectionState.GRANTING_PERMISSIONS -> Triple("PERMISSIONS...", Color(0xFF1B5E20), Color(0xFF69F0AE))
                        SideloadConnectionState.SUCCESS -> Triple("SUCCESS", Color(0xFF1B5E20), Color(0xFF69F0AE))
                        SideloadConnectionState.ERROR -> Triple("ERROR", Color(0xFFB71C1C), Color(0xFFFF8A80))
                    }

                    Surface(
                        color = badgeBg,
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, badgeFg)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeFg,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (connectionState == SideloadConnectionState.IDLE || connectionState == SideloadConnectionState.ERROR) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFF261D12),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFB74D)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFB74D), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "NO WATCH CONNECTED OVER ADB",
                                    color = Color(0xFFFFB74D),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "To sideload DigiWear, enable 'Wireless Debugging' (or 'Debug over Wi-Fi') on your smartwatch in Developer Options, ensure both devices are on the same Wi-Fi / Hotspot, and tap CONNECT below.",
                                color = Color.LightGray,
                                fontSize = 9.5.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = watchIp,
                        onValueChange = { watchIp = it },
                        label = { Text("Watch IP Address", fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                        placeholder = { Text("192.168.1.50", fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DigiWearPalette.DigiviceCyan,
                            unfocusedBorderColor = DigiWearPalette.CardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = watchPort,
                        onValueChange = { watchPort = it },
                        label = { Text("Port", fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                        placeholder = { Text("5555", fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                        modifier = Modifier.width(90.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DigiWearPalette.DigiviceCyan,
                            unfocusedBorderColor = DigiWearPalette.CardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons: Connect, Auto-Detect LAN, Disconnect
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val port = watchPort.toIntOrNull() ?: 5555
                            wearableManager.setConfiguredAospWatchIp(watchIp)
                            scope.launch {
                                adbManager.connect(watchIp, port)
                            }
                        },
                        enabled = connectionState == SideloadConnectionState.IDLE || connectionState == SideloadConnectionState.ERROR,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DigiWearPalette.DigiviceCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CONNECT", fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                isScanningLan = true
                                adbManager.log("Scanning local Wi-Fi / Hotspot for smartwatch...")
                                val discovered = wearableManager.discoverAospWatchOnLan(600)
                                if (!discovered.isNullOrEmpty()) {
                                    watchIp = discovered
                                    adbManager.log("Discovered Watch on LAN: $discovered")
                                } else {
                                    adbManager.log("LAN discovery probe did not find watch. Please enter IP manually.")
                                }
                                isScanningLan = false
                            }
                        },
                        enabled = !isScanningLan,
                        border = BorderStroke(1.dp, Color(0xFF546E7A)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB0BEC5)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Scan", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isScanningLan) "SCANNING..." else "AUTO-DETECT", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    if (connectionState == SideloadConnectionState.CONNECTED) {
                        OutlinedButton(
                            onClick = { adbManager.disconnect() },
                            border = BorderStroke(1.dp, Color(0xFFE57373)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE57373)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("DISCONNECT", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                // Connected Watch Info Box
                AnimatedVisibility(visible = watchDetails != null) {
                    val details = watchDetails
                    if (details != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = Color(0xFF101923),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF26A69A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Watch, contentDescription = null, tint = Color(0xFF26A69A), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${details.model} (${details.manufacturer})",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    if (details.batteryPercent.isNotEmpty()) {
                                        Text(
                                            text = details.batteryPercent,
                                            color = Color(0xFF80CBC4),
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Android ${details.androidVersion} (API ${details.sdkVersion}) ${details.wearPlatform}",
                                    color = Color(0xFF90A4AE),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "DIGIWEAR: ",
                                        color = Color(0xFF78909C),
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    if (details.isDigiWearInstalled) {
                                        Text(
                                            text = "INSTALLED (${details.installedVersionName})",
                                            color = Color(0xFF00E676),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    } else {
                                        Text(
                                            text = "NOT INSTALLED",
                                            color = Color(0xFFFFB74D),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. Step 2: Watch APK Source Selection ---
        Card(
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "2. WATCH APK SOURCE",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { pickApkLauncher.launch("application/vnd.android.package-archive") },
                        border = BorderStroke(1.dp, DigiWearPalette.DigiviceCyan),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DigiWearPalette.DigiviceCyan),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.FileOpen, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SELECT APK", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                isDownloading = true
                                downloadProgress = 0f
                                downloadStatusText = "Connecting to GitHub Releases..."
                                adbManager.log("Starting download of DigiWear watch release APK...")
                                val dlResult = apkSourceManager.downloadReleaseApk { downloaded, total ->
                                    if (total > 0) {
                                        downloadProgress = downloaded.toFloat() / total.toFloat()
                                        val mbDown = downloaded / (1024.0 * 1024.0)
                                        val mbTotal = total / (1024.0 * 1024.0)
                                        downloadStatusText = String.format("%.1f / %.1f MB (%.0f%%)", mbDown, mbTotal, downloadProgress * 100f)
                                    }
                                }
                                dlResult.onSuccess { info ->
                                    selectedApk = info
                                    downloadStatusText = "Download complete!"
                                    adbManager.log("Download complete: ${info.displayName} (${info.sizeFormatted})")
                                }.onFailure { err ->
                                    downloadStatusText = "Failed: ${err.message}"
                                    adbManager.log("Download failed: ${err.message}")
                                }
                                isDownloading = false
                            }
                        },
                        enabled = !isDownloading,
                        border = BorderStroke(1.dp, DigiWearPalette.CrestGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DigiWearPalette.CrestGold),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isDownloading) "DOWNLOADING..." else "DOWNLOAD LATEST", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                // Download Progress Indicator
                if (isDownloading) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { downloadProgress },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = DigiWearPalette.CrestGold,
                        trackColor = Color(0xFF263238)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = downloadStatusText,
                        color = DigiWearPalette.CrestGold,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Selected APK Info Banner
                val apk = selectedApk
                if (apk != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFF101923),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF00E676)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = apk.displayName,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "Size: ${apk.sizeFormatted} • Source: ${apk.sourceType.label}",
                                        color = Color(0xFF80CBC4),
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Step 3: Installation Options ---
        Card(
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "3. INSTALLATION OPTIONS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(6.dp))

                val checkboxColors = CheckboxDefaults.colors(
                    checkedColor = DigiWearPalette.DigiviceCyan,
                    checkmarkColor = Color.Black,
                    uncheckedColor = Color(0xFF546E7A)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = grantPermissions, onCheckedChange = { grantPermissions = it }, colors = checkboxColors)
                    Text("Auto-grant runtime permissions (-g: Step, Sensor, Notifications)", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = reinstallKeepData, onCheckedChange = { reinstallKeepData = it }, colors = checkboxColors)
                    Text("Reinstall and keep existing pet data (-r -d)", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = autoLaunch, onCheckedChange = { autoLaunch = it }, colors = checkboxColors)
                    Text("Launch DigiWear on watch immediately after install", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }

        // --- 5. Step 4: Sideload Action Button ---
        val isReadyToSideload = connectionState == SideloadConnectionState.CONNECTED && selectedApk != null
        val isBusy = connectionState == SideloadConnectionState.TRANSFERRING ||
            connectionState == SideloadConnectionState.INSTALLING ||
            connectionState == SideloadConnectionState.GRANTING_PERMISSIONS

        Button(
            onClick = {
                val apkToInstall = selectedApk
                if (apkToInstall != null) {
                    scope.launch {
                        adbManager.sideloadApk(
                            apkFile = apkToInstall.file,
                            grantPermissions = grantPermissions,
                            reinstallKeepData = reinstallKeepData,
                            autoLaunch = autoLaunch
                        )
                    }
                }
            },
            enabled = isReadyToSideload && !isBusy,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00E676),
                disabledContainerColor = Color(0xFF1E2E28),
                contentColor = Color.Black,
                disabledContentColor = Color(0xFF455A64)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!isBusy) {
                    DigiVectorIcon(type = DigiIconType.BOLT, size = 16.dp, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = if (isBusy) "SIDELOADING IN PROGRESS..." else "SIDELOAD DIGIWEAR TO WATCH",
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }

        if (isBusy) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = Color(0xFF00E676),
                trackColor = Color(0xFF1B2C24)
            )
        }

        // --- 6. Live ADB Console Logs Window ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF080C12)),
            border = BorderStroke(1.dp, Color(0xFF1E2C3D)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE D-TERMINAL LOGS",
                            color = Color(0xFF00E676),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    TextButton(onClick = { adbManager.clearLogs() }) {
                        Text("CLEAR", color = Color(0xFF78909C), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val listState = rememberLazyListState()
                LaunchedEffect(consoleLogs.size) {
                    if (consoleLogs.isNotEmpty()) {
                        listState.animateScrollToItem(consoleLogs.size - 1)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp, max = 220.dp)
                        .background(Color(0xFF05080D))
                        .padding(8.dp)
                ) {
                    if (consoleLogs.isEmpty()) {
                        Text(
                            text = "Terminal ready. Connect to smartwatch to view real-time ADB transmission logs.",
                            color = Color(0xFF455A64),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        LazyColumn(state = listState) {
                            items(consoleLogs) { logEntry ->
                                Text(
                                    text = logEntry,
                                    color = if (logEntry.contains("SUCCESS", true) || logEntry.contains("OK", true)) {
                                        Color(0xFF00E676)
                                    } else if (logEntry.contains("ERROR", true) || logEntry.contains("FAILED", true)) {
                                        Color(0xFFFF5252)
                                    } else if (logEntry.contains("WARNING", true) || logEntry.contains("CHECK", true)) {
                                        Color(0xFFFFD700)
                                    } else {
                                        Color(0xFF80CBC4)
                                    },
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 7. Quick Watch Controls & Diagnostics ---
        if (connectionState == SideloadConnectionState.CONNECTED) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                border = BorderStroke(1.dp, DigiWearPalette.CardBorder),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "QUICK WATCH DIAGNOSTICS & CONTROLS",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { scope.launch { adbManager.refreshWatchStatus() } },
                            border = BorderStroke(1.dp, Color(0xFF546E7A)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB0BEC5)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("STATUS", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        }

                        OutlinedButton(
                            onClick = { scope.launch { adbManager.launchOnWatch() } },
                            border = BorderStroke(1.dp, Color(0xFF00E676)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E676)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("LAUNCH", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        }

                        OutlinedButton(
                            onClick = { scope.launch { adbManager.uninstallFromWatch() } },
                            border = BorderStroke(1.dp, Color(0xFFE57373)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE57373)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("UNINSTALL", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }

    // --- Wireless Debugging Setup Guide Dialog ---
    if (showGuideDialog) {
        AlertDialog(
            onDismissRequest = { showGuideDialog = false },
            confirmButton = {
                TextButton(onClick = { showGuideDialog = false }) {
                    Text("GOT IT", color = DigiWearPalette.DigiviceCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("HOW TO ENABLE WIRELESS DEBUGGING", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "1. WEAR OS SMARTWATCH (Pixel Watch, Galaxy Watch):",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "• On your watch, open Settings -> System -> About -> Versions.\n" +
                            "• Tap 'Build Number' 7 times rapidly until 'You are a developer' appears.\n" +
                            "• Go back to Settings -> Developer Options.\n" +
                            "• Turn ON 'ADB Debugging' and 'Wireless Debugging' (or Debug over Wi-Fi).\n" +
                            "• Connect Watch and Phone to the SAME Wi-Fi network.\n" +
                            "• Enter the IP and Port displayed on the watch into this app and tap CONNECT.\n" +
                            "• On the watch screen prompt, tap 'Always Allow'!",
                        color = Color.LightGray,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "2. STANDALONE AOSP / ANDROID WATCH (Kospet, Lokmat, Lemfo, Zeblaze):",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB74D),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "• Open Settings -> About Watch -> tap 'Build Number' 7 times.\n" +
                            "• Go to Developer Options -> turn ON 'USB Debugging' or 'Wireless Debugging'.\n" +
                            "• Connect watch to Wi-Fi (or your Phone's Wi-Fi Hotspot).\n" +
                            "• Use port 5555 and tap CONNECT.",
                        color = Color.LightGray,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            },
            containerColor = Color(0xFF141D29),
            textContentColor = Color.White,
            titleContentColor = DigiWearPalette.DigiviceCyan
        )
    }
}
