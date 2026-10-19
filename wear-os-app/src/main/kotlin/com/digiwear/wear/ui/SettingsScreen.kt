package com.digiwear.wear.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.PetSpecies
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.haptic.HapticManager
import com.digiwear.wear.input.HardwareInputManager
import com.digiwear.wear.input.VPetButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    repository: PetRepository,
    hapticManager: HapticManager,
    onBack: () -> Unit
) {
    val displayConfig = WatchDisplayConfig.current()
    val scope = rememberCoroutineScope()
    val listState = rememberScalingLazyListState()

    LaunchedEffect(Unit) {
        HardwareInputManager.buttonEvents.collect { button ->
            if (button == VPetButton.C) {
                onBack()
            }
        }
    }

    val currentTheme by repository.themeFlow.collectAsState(initial = BackgroundTheme.CLASSIC_VPET_LCD)
    val isVibrationEnabled by repository.vibrationFlow.collectAsState(initial = true)
    val isThreeButtonMode by repository.threeButtonModeFlow.collectAsState(initial = false)
    val isPendulumShakeMode by repository.pendulumShakeModeFlow.collectAsState(initial = false)
    val isSoundEnabled by repository.soundEnabledFlow.collectAsState(initial = false)
    val nfcBridgeMode by repository.nfcBridgeModeFlow.collectAsState(initial = "NFC_DUEL")
    val displayShapeOverride by repository.displayShapeOverrideFlow.collectAsState(initial = "AUTO")

    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var showEggSelector by remember { mutableStateOf(false) }

    val themes = BackgroundTheme.entries
    val nfcModes = listOf(
        "NFC_DUEL" to "Tap-to-Duel (P2P)",
        "WILD_SCAN" to "Wild Tag Scanner",
        "ACOM_RELAY" to "Phone A-Com Relay"
    )

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF12141A)),
        state = listState,
        contentPadding = displayConfig.contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "SETTINGS",
                fontSize = displayConfig.headerFontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64B5F6),
                fontFamily = FontFamily.Monospace
            )
        }

        // ==========================================
        // 1. Background Theme Selector
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "BACKGROUND THEME",
                fontSize = 9.sp,
                color = Color.LightGray,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val currentIdx = themes.indexOf(currentTheme)
                        val prevIdx = if (currentIdx > 0) currentIdx - 1 else themes.size - 1
                        repository.setTheme(themes[prevIdx])
                        hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(34.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF263238))
                ) {
                    Text("◀", fontSize = 10.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    // Swatch preview box
                    Box(
                        modifier = Modifier
                            .size(24.dp, 12.dp)
                            .background(Color(currentTheme.primaryColorHex), RoundedCornerShape(2.dp))
                    )
                    Text(
                        text = currentTheme.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = {
                        val currentIdx = themes.indexOf(currentTheme)
                        val nextIdx = (currentIdx + 1) % themes.size
                        repository.setTheme(themes[nextIdx])
                        hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(34.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF263238))
                ) {
                    Text("▶", fontSize = 10.sp)
                }
            }
        }

        // ==========================================
        // 2. Vibration Feedback Toggle
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VIBRATIONS",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
                Button(
                    onClick = {
                        val next = !isVibrationEnabled
                        repository.setVibrationEnabled(next)
                        hapticManager.isEnabled = next
                        if (next) hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(width = 54.dp, height = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isVibrationEnabled) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                ) {
                    Text(
                        text = if (isVibrationEnabled) "ON" else "OFF",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 3. 8-Bit Bandai Buzzer Sound FX Toggle
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BUZZER SOUNDS",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "8-Bit Piezo Beeps",
                        fontSize = 8.sp,
                        color = Color.LightGray
                    )
                }
                Button(
                    onClick = {
                        val next = !isSoundEnabled
                        repository.setSoundEnabled(next)
                        if (isVibrationEnabled) hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(width = 54.dp, height = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isSoundEnabled) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                ) {
                    Text(
                        text = if (isSoundEnabled) "ON" else "OFF",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 4. 3-Button V-Pet Authentic Mode Toggle
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "3-BUTTON MODE",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "[A][B][C] + Stem Keys",
                        fontSize = 8.sp,
                        color = Color.LightGray
                    )
                }
                Button(
                    onClick = {
                        val next = !isThreeButtonMode
                        repository.setThreeButtonMode(next)
                        if (isVibrationEnabled) hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(width = 54.dp, height = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isThreeButtonMode) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                ) {
                    Text(
                        text = if (isThreeButtonMode) "ON" else "OFF",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 4. Training Mode Toggle (QTE vs Pendulum Shake)
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TRAIN METHOD",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (isPendulumShakeMode) "Pendulum Shake (5-8)" else "OG Timing QTE (Default)",
                        fontSize = 8.sp,
                        color = if (isPendulumShakeMode) Color(0xFFFFD54F) else Color.LightGray
                    )
                }
                Button(
                    onClick = {
                        val next = !isPendulumShakeMode
                        repository.setPendulumShakeMode(next)
                        if (isVibrationEnabled) hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(width = 60.dp, height = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isPendulumShakeMode) Color(0xFF6A1B9A) else Color(0xFF00695C)
                    )
                ) {
                    Text(
                        text = if (isPendulumShakeMode) "SHAKE" else "QTE",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 5. NFC Tap Bridge Mode Selector
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(8.dp))
            val currentPair = nfcModes.find { it.first == nfcBridgeMode } ?: nfcModes.first()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "NFC TAP MODE",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = currentPair.second,
                        fontSize = 8.sp,
                        color = Color(0xFF80D8FF)
                    )
                }
                Button(
                    onClick = {
                        val idx = nfcModes.indexOfFirst { it.first == nfcBridgeMode }.coerceAtLeast(0)
                        val nextMode = nfcModes[(idx + 1) % nfcModes.size].first
                        repository.setNfcBridgeMode(nextMode)
                        if (isVibrationEnabled) hapticManager.playStepMilestone()
                    },
                    modifier = Modifier.size(width = 60.dp, height = 32.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0277BD))
                ) {
                    Text(
                        text = when (nfcBridgeMode) {
                            "WILD_SCAN" -> "SCAN"
                            "ACOM_RELAY" -> "A-COM"
                            else -> "DUEL"
                        },
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 6. Hatch New Digi-Egg / Reset
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(10.dp))
            if (!showEggSelector) {
                Button(
                    onClick = { showEggSelector = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = displayConfig.horizontalPadding),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5E35B1))
                ) {
                    Text("🥚 HATCH NEW DIGI-EGG", fontSize = 8.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                }
            } else {
                val starterEggs = listOf(
                    Triple(101, "V1•BOTA", Color(0xFF37474F)),
                    Triple(103, "V2•PUNI", Color(0xFFC62828)),
                    Triple(105, "V3•POYO", Color(0xFF0277BD)),
                    Triple(107, "V4•YURA", Color(0xFF2E7D32)),
                    Triple(109, "V5•ZURU", Color(0xFF33691E)),
                    Triple(111, "PEN•MOKU", Color(0xFFE65100)),
                    Triple(113, "VIR•KURA", Color(0xFF4A148C)),
                    Triple(130, "NYO•NYOK", Color(0xFFAD1457)),
                    Triple(131, "PIC•PICH", Color(0xFF0097A7)),
                    Triple(132, "PUK•PUKA", Color(0xFF1565C0))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding)
                        .background(Color(0xFF1D1730), RoundedCornerShape(8.dp))
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SELECT STARTER DIGI-EGG:",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFCE93D8),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    starterEggs.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            pair.forEach { (eggId, label, bgColor) ->
                                val sp = PetSpecies.getById(eggId)
                                Button(
                                    onClick = {
                                        scope.launch {
                                            repository.hatchNewEgg(eggId)
                                            hapticManager.playVictoryFanfare()
                                            feedbackMessage = "Hatched ${sp.name}!"
                                            showEggSelector = false
                                            delay(1500L)
                                            feedbackMessage = null
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(26.dp).padding(horizontal = 2.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = bgColor)
                                ) {
                                    Text(label, fontSize = 7.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✕ CANCEL",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.clickable { showEggSelector = false }
                    )
                }
            }
        }

        feedbackMessage?.let { msg ->
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg,
                    fontSize = 9.sp,
                    color = Color(0xFF69F0AE),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ==========================================
        // 7. AOSP & Wear OS Hardware Compatibility Diagnostics + Display Shape + LAN Sync
        // ==========================================
        item {
            val context = androidx.compose.ui.platform.LocalContext.current
            val pedometerMode = remember { com.digiwear.wear.sensor.StepTrackerManager.getActivePedometerMode(context) }
            val nfcAvailable = remember { com.digiwear.wear.nfc.NfcDuelManager.isNfcAvailable(context) }
            val lanIp = remember { com.digiwear.wear.datalayer.AospLanSyncServer.getLocalIpAddress() }
            val shapeModes = listOf("AUTO", "ROUND", "SQUARE")
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = displayConfig.horizontalPadding)
                    .background(Color(0xFF1A222D), RoundedCornerShape(8.dp))
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⌚ PLATFORM & AOSP STATUS",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF80D8FF),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${displayConfig.runtimePlatformLabel} (${displayConfig.screenWidthDp}x${displayConfig.screenHeightDp}dp)",
                    fontSize = 7.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "PEDOMETER: $pedometerMode",
                    fontSize = 6.5.sp,
                    color = Color(0xFF69F0AE),
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "NFC RADIO: ${if (nfcAvailable) "HCE / NDEF READY" else "DIGIROM CODE MODE"}",
                    fontSize = 6.5.sp,
                    color = Color(0xFFFFD54F),
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "AOSP LAN BRIDGE: $lanIp:${com.digiwear.wear.datalayer.AospLanSyncServer.TCP_PORT}",
                    fontSize = 6.5.sp,
                    color = Color(0xFFEA80FC),
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {
                            val idx = shapeModes.indexOf(displayShapeOverride).coerceAtLeast(0)
                            val nextShape = shapeModes[(idx + 1) % shapeModes.size]
                            repository.setDisplayShapeOverride(nextShape)
                            if (isVibrationEnabled) hapticManager.playStepMilestone()
                        },
                        modifier = Modifier.weight(1f).height(24.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3949AB))
                    ) {
                        Text("SHAPE: $displayShapeOverride", fontSize = 6.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                repository.addAdventureSteps(100)
                                hapticManager.playStepMilestone()
                                feedbackMessage = "+100 Steps Added!"
                                delay(1500L)
                                feedbackMessage = null
                            }
                        },
                        modifier = Modifier.weight(1f).height(24.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00695C))
                    ) {
                        Text("👟 +100 STEPS", fontSize = 6.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // ==========================================
        // 8. Back Button
        // ==========================================
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(horizontal = displayConfig.horizontalPadding),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF263238))
            ) {
                Text("BACK [C]", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
    }
}
