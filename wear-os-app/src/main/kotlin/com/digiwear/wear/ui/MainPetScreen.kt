package com.digiwear.wear.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.ui.platform.LocalContext
import com.digiwear.wear.sound.BuzzerManager
import kotlin.math.sqrt
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import com.digiwear.wear.ambient.LocalAmbientState
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.haptic.HapticManager
import com.digiwear.wear.input.HardwareInputManager
import com.digiwear.wear.input.VPetButton
import com.digiwear.wear.render.SpriteCanvasRenderer
import com.digiwear.wear.render.SpriteFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class VPetMenu(val label: String, val icon: String) {
    STATUS("STATUS", "⚖️"),
    FEED("FEED", "🍖"),
    TRAIN("TRAIN", "🥊"),
    BATTLE("BATTLE", "⚔"),
    FLUSH("FLUSH", "💩"),
    LIGHT("LIGHT", "💡"),
    MEDS("MEDS", "🩹"),
    ATTENTION("CALL", "🚨")
}

@Composable
fun MainPetScreen(
    repository: PetRepository,
    hapticManager: HapticManager,
    buzzerManager: BuzzerManager = remember { BuzzerManager() },
    onNavigateBattle: () -> Unit,
    onNavigateAdventure: () -> Unit,
    onNavigateStatus: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val ambientState = LocalAmbientState.current
    val petState by repository.petStateFlow.collectAsState(initial = PetState())
    val currentTheme by repository.themeFlow.collectAsState(initial = BackgroundTheme.CLASSIC_VPET_LCD)
    val isThreeButtonMode by repository.threeButtonModeFlow.collectAsState(initial = false)
    val isPendulumShakeMode by repository.pendulumShakeModeFlow.collectAsState(initial = false)
    val isSoundEnabled by repository.soundEnabledFlow.collectAsState(initial = false)
    val contrastLevel by repository.contrastLevelFlow.collectAsState(initial = "MEDIUM")
    val ambientStyle by repository.ambientStyleFlow.collectAsState(initial = "MINIMAL_DOT")
    val displayConfig = WatchDisplayConfig.current()
    val scope = rememberCoroutineScope()

    LaunchedEffect(isSoundEnabled) {
        buzzerManager.isEnabled = isSoundEnabled
    }

    var actionFeedback by remember { mutableStateOf<String?>(null) }
    var overrideSpriteFrame by remember { mutableStateOf<SpriteFrame?>(null) }
    var selectedMenuIndex by remember { mutableStateOf<Int?>(null) }

    // Training Minigame Quick Time Event state
    var isTrainingActive by remember { mutableStateOf(false) }

    // Periodic time-delta refresh while interactive (strictly paused during Ambient Mode)
    LaunchedEffect(ambientState.isAmbient) {
        if (ambientState.isAmbient) return@LaunchedEffect
        repository.processDecay()
        while (true) {
            delay(30_000L) // Refresh every 30s while interactive
            repository.processDecay()
        }
    }

    // Play haptic & buzzer alert if attention call active
    LaunchedEffect(petState.attentionCallActive) {
        if (petState.attentionCallActive) {
            hapticManager.playAttentionAlert()
            buzzerManager.playAttentionBeep()
        }
    }

    val executeMenuAction: (Int) -> Unit = { index ->
        val item = VPetMenu.entries[index]
        when (item) {
            VPetMenu.FEED -> {
                scope.launch {
                    overrideSpriteFrame = SpriteFrame.JOY_EAT
                    if (petState.hunger < 4) {
                        repository.feedMeal()
                        actionFeedback = "Fed Meat (+1g)"
                    } else {
                        repository.feedProtein()
                        actionFeedback = "Fed Vitamin (+2g)"
                    }
                    hapticManager.playStepMilestone()
                    delay(1200L)
                    overrideSpriteFrame = null
                    actionFeedback = null
                }
            }
            VPetMenu.LIGHT -> {
                scope.launch {
                    repository.toggleLights()
                    hapticManager.playStepMilestone()
                    actionFeedback = if (petState.lightsOff) "Lights ON" else "Lights OFF"
                    delay(1000L)
                    actionFeedback = null
                }
            }
            VPetMenu.BATTLE -> {
                onNavigateBattle()
            }
            VPetMenu.FLUSH -> {
                scope.launch {
                    repository.cleanWaste()
                    hapticManager.playStepMilestone()
                    actionFeedback = "Cleaned!"
                    delay(1000L)
                    actionFeedback = null
                }
            }
            VPetMenu.STATUS -> {
                onNavigateStatus()
            }
            VPetMenu.TRAIN -> {
                isTrainingActive = true
            }
            VPetMenu.MEDS -> {
                scope.launch {
                    if (petState.isInjured) {
                        repository.cureInjury()
                        hapticManager.playStepMilestone()
                        actionFeedback = "Cured!"
                    } else {
                        actionFeedback = "Not Sick"
                    }
                    delay(1000L)
                    actionFeedback = null
                }
            }
            VPetMenu.ATTENTION -> {
                if (petState.attentionCallActive) {
                    val needReason = when {
                        petState.hunger == 0 -> "Needs Food!"
                        petState.strength == 0 -> "Needs Training!"
                        petState.isSleeping && !petState.lightsOff -> "Lights Needed!"
                        petState.isInjured -> "Needs Medicine!"
                        else -> "Attending Pet!"
                    }
                    actionFeedback = needReason
                    hapticManager.playStepMilestone()
                    scope.launch {
                        delay(1200L)
                        actionFeedback = null
                    }
                } else {
                    onNavigateAdventure()
                }
            }
        }
    }

    // Listen to physical smartwatch hardware button presses & rotary crown input
    LaunchedEffect(isTrainingActive, selectedMenuIndex) {
        HardwareInputManager.buttonEvents.collect { button ->
            if (isTrainingActive) {
                // If in training, button C can cancel
                if (button == VPetButton.C) {
                    isTrainingActive = false
                }
                return@collect
            }

            when (button) {
                VPetButton.A -> {
                    // Cycle forward through the 8 menu icons
                    selectedMenuIndex = if (selectedMenuIndex == null) 0 else (selectedMenuIndex!! + 1) % 8
                    hapticManager.playStepMilestone()
                }
                VPetButton.PREV -> {
                    // Cycle backwards
                    selectedMenuIndex = if (selectedMenuIndex == null) 7 else (selectedMenuIndex!! + 7) % 8
                    hapticManager.playStepMilestone()
                }
                VPetButton.B -> {
                    // Confirm / Execute currently highlighted menu icon
                    if (selectedMenuIndex != null) {
                        val idx = selectedMenuIndex!!
                        selectedMenuIndex = null
                        executeMenuAction(idx)
                    } else {
                        // Quick poke / interaction
                        scope.launch {
                            overrideSpriteFrame = SpriteFrame.JOY_EAT
                            hapticManager.playStepMilestone()
                            delay(1000L)
                            overrideSpriteFrame = null
                        }
                    }
                }
                VPetButton.C -> {
                    // Cancel selection or toggle lights
                    if (selectedMenuIndex != null) {
                        selectedMenuIndex = null
                    } else {
                        scope.launch {
                            repository.toggleLights()
                            hapticManager.playStepMilestone()
                            actionFeedback = if (petState.lightsOff) "Lights ON" else "Lights OFF"
                            delay(1000L)
                            actionFeedback = null
                        }
                    }
                }
            }
        }
    }

    if (ambientState.isAmbient) {
        AmbientPetScreen(petState = petState, displayConfig = displayConfig)
        return
    }

    val species = remember(petState.speciesId) { PetSpecies.getById(petState.speciesId) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Dynamic procedural background
        BackgroundCanvas(
            theme = currentTheme,
            isAmbient = false,
            lightsOff = petState.lightsOff,
            contrastLevel = contrastLevel,
            ambientStyle = ambientStyle
        )

        if (isTrainingActive) {
            // ========================================================
            // Training Minigame (OG Timing QTE or Authentic Pendulum Shake)
            // ========================================================
            if (isPendulumShakeMode) {
                TrainingShakeView(
                    petState = petState,
                    displayConfig = displayConfig,
                    hapticManager = hapticManager,
                    onFinished = { won, message ->
                        scope.launch {
                            repository.train(won = won)
                            if (won) {
                                overrideSpriteFrame = SpriteFrame.JOY_EAT
                                actionFeedback = message
                            } else {
                                overrideSpriteFrame = SpriteFrame.REFUSAL
                                actionFeedback = message
                            }
                            isTrainingActive = false
                            delay(1200L)
                            overrideSpriteFrame = null
                            actionFeedback = null
                        }
                    },
                    onCancel = { isTrainingActive = false }
                )
            } else {
                TrainingQteView(
                    petState = petState,
                    displayConfig = displayConfig,
                    hapticManager = hapticManager,
                    onFinished = { won, message ->
                        scope.launch {
                            repository.train(won = won)
                            if (won) {
                                overrideSpriteFrame = SpriteFrame.JOY_EAT
                                actionFeedback = message
                            } else {
                                overrideSpriteFrame = SpriteFrame.REFUSAL
                                actionFeedback = message
                            }
                            isTrainingActive = false
                            delay(1200L)
                            overrideSpriteFrame = null
                            actionFeedback = null
                        }
                    },
                    onCancel = { isTrainingActive = false }
                )
            }
        } else if (isThreeButtonMode) {
            // ========================================================
            // Authentic 3-Button V-Pet Display Frame
            // ========================================================
            ThreeButtonPetView(
                species = species,
                petState = petState,
                displayConfig = displayConfig,
                selectedMenuIndex = selectedMenuIndex,
                overrideSpriteFrame = overrideSpriteFrame,
                actionFeedback = actionFeedback,
                onSelectIcon = { index ->
                    if (selectedMenuIndex == index) {
                        selectedMenuIndex = null
                        executeMenuAction(index)
                    } else {
                        selectedMenuIndex = index
                        hapticManager.playStepMilestone()
                    }
                },
                onPressA = {
                    selectedMenuIndex = if (selectedMenuIndex == null) 0 else (selectedMenuIndex!! + 1) % 8
                    hapticManager.playStepMilestone()
                },
                onPressB = {
                    if (selectedMenuIndex != null) {
                        val idx = selectedMenuIndex!!
                        selectedMenuIndex = null
                        executeMenuAction(idx)
                    } else {
                        selectedMenuIndex = 0
                        hapticManager.playStepMilestone()
                    }
                },
                onPressC = {
                    if (selectedMenuIndex != null) {
                        selectedMenuIndex = null
                    } else {
                        scope.launch {
                            repository.toggleLights()
                            hapticManager.playStepMilestone()
                            actionFeedback = if (petState.lightsOff) "Lights ON" else "Lights OFF"
                            delay(1000L)
                            actionFeedback = null
                        }
                    }
                },
                onNavigateSettings = onNavigateSettings,
                onNavigateStatus = onNavigateStatus,
                onNavigateAdventure = onNavigateAdventure
            )
        } else {
            // ========================================================
            // Modern Touch UI Mode (ScalingLazyColumn with touch buttons)
            // ========================================================
            ModernTouchPetView(
                species = species,
                petState = petState,
                displayConfig = displayConfig,
                overrideSpriteFrame = overrideSpriteFrame,
                actionFeedback = actionFeedback,
                scope = scope,
                repository = repository,
                hapticManager = hapticManager,
                onNavigateBattle = onNavigateBattle,
                onNavigateAdventure = onNavigateAdventure,
                onNavigateStatus = onNavigateStatus,
                onNavigateSettings = onNavigateSettings,
                onStartTraining = {
                    isTrainingActive = true
                },
                onSetOverrideFrame = { frame -> overrideSpriteFrame = frame },
                onSetFeedback = { fb -> actionFeedback = fb }
            )
        }
    }
}

/**
 * Authentic Bandai LCD style 3-Button layout with recessed top & bottom 4-icon LCD rails,
 * framed pixel-art viewport, and tactile [A] [B] [C] shell buttons alongside physical stem input.
 */
@Composable
private fun ThreeButtonPetView(
    species: PetSpecies,
    petState: PetState,
    displayConfig: WatchDisplayConfig,
    selectedMenuIndex: Int?,
    overrideSpriteFrame: SpriteFrame?,
    actionFeedback: String?,
    onSelectIcon: (Int) -> Unit,
    onPressA: () -> Unit,
    onPressB: () -> Unit,
    onPressC: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateStatus: () -> Unit,
    onNavigateAdventure: () -> Unit
) {
    val attrColor = when (species.attribute.displayName) {
        "Vaccine" -> Color(0xFF00E5FF)
        "Data" -> Color(0xFFFFCA28)
        "Virus" -> Color(0xFFFF5252)
        else -> Color(0xFF69F0AE)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(displayConfig.contentPadding),
        contentAlignment = Alignment.Center
    ) {
        val isTallDisplay = displayConfig.screenHeightDp > 340
        Column(
            modifier = if (isTallDisplay) {
                Modifier
                    .widthIn(max = 280.dp)
                    .height(300.dp)
            } else {
                Modifier.fillMaxSize()
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Recessed V-Pet LCD Rail: Food (0), Light (1), Battle (2), Flush (3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x77091016), RoundedCornerShape(6.dp))
                    .border(0.75.dp, Color(0x4480CBC4), RoundedCornerShape(6.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..3) {
                    val item = VPetMenu.entries[i]
                    val isSelected = selectedMenuIndex == i
                    Box(
                        modifier = Modifier
                            .size(width = if (isSelected) 32.dp else 28.dp, height = 24.dp)
                            .background(
                                color = if (isSelected) Color(0xFF00B0FF) else Color(0x33000000),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 0.5.dp,
                                color = if (isSelected) Color(0xFFFFD54F) else Color(0x5590A4AE),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable { onSelectIcon(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.icon, fontSize = if (isSelected) 13.sp else 11.sp)
                    }
                }
            }

            // Center V-Pet LCD Screen Viewport (Telemetry, Sprite, Prompt)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header line: Name, Attribute Pill, Weight, Quest & Settings
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = species.name.take(11),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB9F6CA),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(
                            modifier = Modifier
                                .background(attrColor.copy(alpha = 0.22f), RoundedCornerShape(3.dp))
                                .border(0.5.dp, attrColor, RoundedCornerShape(3.dp))
                                .padding(horizontal = 3.dp, vertical = 0.5.dp)
                        ) {
                            Text(
                                text = species.attribute.displayName.take(3).uppercase(),
                                fontSize = 6.sp,
                                fontWeight = FontWeight.Black,
                                color = attrColor,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${petState.weight}g",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE0E0E0),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(Color(0x66004D40), CircleShape)
                                .border(0.5.dp, Color(0xFF4DB6AC), CircleShape)
                                .clickable { onNavigateAdventure() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🗺", fontSize = 8.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(Color(0x66263238), CircleShape)
                                .border(0.5.dp, Color(0xFF90A4AE), CircleShape)
                                .clickable { onNavigateSettings() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚙", fontSize = 8.sp, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(1.dp))

                // Framed Hunger & Strength LCD Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x55000000), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "H:${"♥".repeat(petState.hunger)}${"♡".repeat(4 - petState.hunger)}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF5252),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0x55000000), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "S:${"★".repeat(petState.strength)}${"☆".repeat(4 - petState.strength)}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                if (petState.attentionCallActive) {
                    Box(
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .background(Color(0xCCD50000), RoundedCornerShape(3.dp))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "⚠ CALL SIGNAL ⚠",
                            fontSize = 7.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                if (petState.wasteCount > 0) {
                    Text(
                        text = "💩 ".repeat(petState.wasteCount),
                        fontSize = 9.sp
                    )
                }

                // Center Sprite Viewport
                Box(
                    modifier = Modifier
                        .size(displayConfig.mainSpriteSize)
                        .border(
                            width = 0.75.dp,
                            color = if (selectedMenuIndex != null) Color(0x88FFD54F) else Color(0x33FFFFFF),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onNavigateStatus() },
                    contentAlignment = Alignment.Center
                ) {
                    SpriteCanvasRenderer(
                        speciesId = petState.speciesId,
                        isAmbient = false,
                        overrideFrame = overrideSpriteFrame,
                        isSleeping = petState.isSleeping,
                        isInjured = petState.isInjured,
                        size = displayConfig.mainSpriteSize
                    )
                }

                // Active Menu Prompt or Action Feedback Pill
                val promptText = if (selectedMenuIndex != null) {
                    "► ${VPetMenu.entries[selectedMenuIndex].label} ◄"
                } else actionFeedback ?: if (petState.isSleeping) "ZZZ... SLEEPING" else null

                if (promptText != null) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x66091016), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = promptText,
                            fontSize = 8.sp,
                            color = if (selectedMenuIndex != null) Color(0xFFFFD54F) else Color(0xFFB9F6CA),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Bottom Recessed V-Pet LCD Rail: Flush (4), Light (5), Meds (6), Call (7)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x77091016), RoundedCornerShape(6.dp))
                    .border(0.75.dp, Color(0x4480CBC4), RoundedCornerShape(6.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 4..7) {
                    val item = VPetMenu.entries[i]
                    val isSelected = selectedMenuIndex == i
                    val isCallActive = (i == 7 && petState.attentionCallActive)
                    Box(
                        modifier = Modifier
                            .size(width = if (isSelected || isCallActive) 32.dp else 28.dp, height = 24.dp)
                            .background(
                                color = when {
                                    isSelected -> Color(0xFF00B0FF)
                                    isCallActive -> Color(0xFFD50000)
                                    else -> Color(0x33000000)
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = if (isSelected || isCallActive) 1.5.dp else 0.5.dp,
                                color = when {
                                    isSelected -> Color(0xFFFFD54F)
                                    isCallActive -> Color(0xFFFFD54F)
                                    else -> Color(0x5590A4AE)
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable { onSelectIcon(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.icon, fontSize = if (isSelected || isCallActive) 13.sp else 11.sp)
                    }
                }
            }

            // Tactile Molded Bandai ( A ) ( B ) ( C ) Shell Button Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button A: Select / Cycle
                Box(
                    modifier = Modifier
                        .size(width = 38.dp, height = 22.dp)
                        .background(Color(0xFF263238), RoundedCornerShape(11.dp))
                        .border(1.2.dp, Color(0xFF80D8FF), RoundedCornerShape(11.dp))
                        .clickable { onPressA() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("( A )", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White, fontFamily = FontFamily.Monospace)
                }

                // Button B: Confirm / Execute
                Box(
                    modifier = Modifier
                        .size(width = 38.dp, height = 22.dp)
                        .background(Color(0xFF1B5E20), RoundedCornerShape(11.dp))
                        .border(1.2.dp, Color(0xFF69F0AE), RoundedCornerShape(11.dp))
                        .clickable { onPressB() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("( B )", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFB9F6CA), fontFamily = FontFamily.Monospace)
                }

                // Button C: Cancel / Light
                Box(
                    modifier = Modifier
                        .size(width = 38.dp, height = 22.dp)
                        .background(Color(0xFF4A148C), RoundedCornerShape(11.dp))
                        .border(1.2.dp, Color(0xFFEA80FC), RoundedCornerShape(11.dp))
                        .clickable { onPressC() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("( C )", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFF3E5F5), fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

/**
 * Modern Digivice / Vital Bracelet Touch UI layout with scrollable ScalingLazyColumn,
 * recessed LCD partner stage, attribute badge, and tactile labeled care dock.
 */
@Composable
private fun ModernTouchPetView(
    species: PetSpecies,
    petState: PetState,
    displayConfig: WatchDisplayConfig,
    overrideSpriteFrame: SpriteFrame?,
    actionFeedback: String?,
    scope: kotlinx.coroutines.CoroutineScope,
    repository: PetRepository,
    hapticManager: HapticManager,
    onNavigateBattle: () -> Unit,
    onNavigateAdventure: () -> Unit,
    onNavigateStatus: () -> Unit,
    onNavigateSettings: () -> Unit,
    onStartTraining: () -> Unit,
    onSetOverrideFrame: (SpriteFrame?) -> Unit,
    onSetFeedback: (String?) -> Unit
) {
    val listState = rememberScalingLazyListState()
    val attrColor = when (species.attribute.displayName) {
        "Vaccine" -> Color(0xFF00E5FF)
        "Data" -> Color(0xFFFFCA28)
        "Virus" -> Color(0xFFFF5252)
        else -> Color(0xFF69F0AE)
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = displayConfig.contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header row: Monster name, Stage/Attribute badge, weight, and Settings button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp)
                        .background(Color(0x770B1219), RoundedCornerShape(8.dp))
                        .border(0.75.dp, Color(0x4480CBC4), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = species.name,
                            fontSize = displayConfig.headerFontSize,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB9F6CA),
                            fontFamily = FontFamily.Monospace
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = species.stage.displayName.uppercase(),
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF80CBC4),
                                fontFamily = FontFamily.Monospace
                            )
                            Text(" • ", fontSize = 7.sp, color = Color.Gray)
                            Text(
                                text = species.attribute.displayName.uppercase(),
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = attrColor,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(" • ${petState.weight}g", fontSize = 7.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                        }
                    }
                    Button(
                        onClick = onNavigateSettings,
                        modifier = Modifier.size(24.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF263238))
                    ) {
                        Text("⚙", fontSize = 10.sp)
                    }
                }
            }

            // Segmented Hearts & Stars Bar (Hunger ♥ & Strength ★)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x660B1219), RoundedCornerShape(6.dp))
                            .border(0.5.dp, Color(0x66FF5252), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "H ${"♥".repeat(petState.hunger)}${"♡".repeat(4 - petState.hunger)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (petState.hunger == 0) Color.Red else Color(0xFFFF5252),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0x660B1219), RoundedCornerShape(6.dp))
                            .border(0.5.dp, Color(0x66FFD700), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "S ${"★".repeat(petState.strength)}${"☆".repeat(4 - petState.strength)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (petState.strength == 0) Color.Red else Color(0xFFFFD700),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Attention Call Banner
            if (petState.attentionCallActive) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .background(Color(0xFFD50000), RoundedCornerShape(6.dp))
                            .border(1.dp, Color(0xFFFFD54F), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🚨 CALL SIGNAL ACTIVE 🚨",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Waste indicator
            if (petState.wasteCount > 0) {
                item {
                    Text(
                        text = "💩 ".repeat(petState.wasteCount),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }

            // Center Recessed LCD Pet Stage (Interactive, click to inspect status)
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .background(Color(0x44000000), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0x5580CBC4), RoundedCornerShape(10.dp))
                        .clickable { onNavigateStatus() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    SpriteCanvasRenderer(
                        speciesId = petState.speciesId,
                        isAmbient = false,
                        overrideFrame = overrideSpriteFrame,
                        isSleeping = petState.isSleeping,
                        isInjured = petState.isInjured,
                        size = displayConfig.mainSpriteSize
                    )
                    Text(
                        text = "${species.stage.displayName.uppercase()} • ${petState.weight}g • ${petState.victories}W/${petState.totalBattles}B",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB0BEC5),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Action Feedback message
            actionFeedback?.let { msg ->
                item {
                    Box(
                        modifier = Modifier
                            .background(Color(0xCC0D1B2A), RoundedCornerShape(6.dp))
                            .border(0.75.dp, Color(0xFF69F0AE), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = msg,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF69F0AE),
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Primary Pet Care Controls (Meat, Vitamin, Clean, Light)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Feed Meat
                    Button(
                        onClick = {
                            scope.launch {
                                onSetOverrideFrame(SpriteFrame.JOY_EAT)
                                repository.feedMeal()
                                hapticManager.playStepMilestone()
                                onSetFeedback("Fed Meat (+1g)")
                                delay(1200L)
                                onSetOverrideFrame(null)
                                onSetFeedback(null)
                            }
                        },
                        modifier = Modifier
                            .size(displayConfig.buttonSize)
                            .padding(displayConfig.actionRowSpacing),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC62828))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🍖", fontSize = 11.sp)
                            Text("MEAT", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    // Feed Vitamin / Protein
                    Button(
                        onClick = {
                            scope.launch {
                                onSetOverrideFrame(SpriteFrame.JOY_EAT)
                                repository.feedProtein()
                                hapticManager.playStepMilestone()
                                onSetFeedback("Fed Vitamin (+2g)")
                                delay(1200L)
                                onSetOverrideFrame(null)
                                onSetFeedback(null)
                            }
                        },
                        modifier = Modifier
                            .size(displayConfig.buttonSize)
                            .padding(displayConfig.actionRowSpacing),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF6C00))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💊", fontSize = 11.sp)
                            Text("VIT", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    // Clean Waste
                    Button(
                        onClick = {
                            scope.launch {
                                repository.cleanWaste()
                                hapticManager.playStepMilestone()
                                onSetFeedback("Cleaned!")
                                delay(1000L)
                                onSetFeedback(null)
                            }
                        },
                        modifier = Modifier
                            .size(displayConfig.buttonSize)
                            .padding(displayConfig.actionRowSpacing),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0277BD))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🧹", fontSize = 11.sp)
                            Text("FLUSH", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    // Toggle Lights
                    Button(
                        onClick = {
                            scope.launch {
                                repository.toggleLights()
                                hapticManager.playStepMilestone()
                                onSetFeedback(if (petState.lightsOff) "Lights ON" else "Lights OFF")
                                delay(1000L)
                                onSetFeedback(null)
                            }
                        },
                        modifier = Modifier
                            .size(displayConfig.buttonSize)
                            .padding(displayConfig.actionRowSpacing),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (petState.lightsOff) Color(0xFF37474F) else Color(0xFFF9A825)
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(if (petState.lightsOff) "💡" else "☀", fontSize = 11.sp)
                            Text("LIGHT", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            // Training, Status & Health Controls
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Train Minigame
                    Button(
                        onClick = onStartTraining,
                        modifier = Modifier
                            .size(displayConfig.buttonSize)
                            .padding(displayConfig.actionRowSpacing),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6A1B9A))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🥊", fontSize = 11.sp)
                            Text("TRAIN", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    // Status Inspector
                    Button(
                        onClick = onNavigateStatus,
                        modifier = Modifier
                            .size(displayConfig.buttonSize)
                            .padding(displayConfig.actionRowSpacing),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00695C))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📊", fontSize = 11.sp)
                            Text("STATS", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    // Cure Injury / Sickness
                    if (petState.isInjured) {
                        Button(
                            onClick = {
                                scope.launch {
                                    repository.cureInjury()
                                    hapticManager.playStepMilestone()
                                    onSetFeedback("Cured!")
                                    delay(1000L)
                                    onSetFeedback(null)
                                }
                            },
                            modifier = Modifier
                                .size(displayConfig.buttonSize)
                                .padding(displayConfig.actionRowSpacing),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E7D32))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("💉", fontSize = 11.sp)
                                Text("MEDS", fontSize = 6.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }

            // Navigation Buttons (Battle & Adventure Map)
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = displayConfig.horizontalPadding),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onNavigateBattle,
                        modifier = Modifier
                            .weight(1f)
                            .height(30.dp)
                            .padding(horizontal = 2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD84315))
                    ) {
                        Text("⚔ BATTLE", fontSize = 8.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                    Button(
                        onClick = onNavigateAdventure,
                        modifier = Modifier
                            .weight(1f)
                            .height(30.dp)
                            .padding(horizontal = 2.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00796B))
                    ) {
                        Text("🗺 QUEST", fontSize = 8.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

/**
 * Authentic Training Minigame featuring a dynamic power gauge Quick Time Event (QTE).
 * Supports both physical stem button presses (zero touch-latency) and on-screen tap.
 */
@Composable
private fun TrainingQteView(
    petState: PetState,
    displayConfig: WatchDisplayConfig,
    hapticManager: HapticManager,
    onFinished: (won: Boolean, message: String) -> Unit,
    onCancel: () -> Unit
) {
    var phase by remember { mutableStateOf("COUNTDOWN") } // COUNTDOWN -> PUNCH -> RESOLVED
    var countdownValue by remember { mutableIntStateOf(3) }
    var punchGauge by remember { mutableFloatStateOf(0f) }
    var punchRegistered by remember { mutableStateOf(false) }

    // Oscillating gauge animation (only active during the PUNCH phase)
    val animatedGaugeState = if (phase == "PUNCH") {
        val infiniteTransition = rememberInfiniteTransition(label = "gauge")
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 650, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "gaugeValue"
        )
    } else {
        null
    }

    // Countdown logic
    LaunchedEffect(Unit) {
        while (countdownValue > 1) {
            hapticManager.playStepMilestone()
            delay(500L)
            countdownValue--
        }
        hapticManager.playAttackImpact()
        delay(400L)
        phase = "PUNCH"
    }

    val handlePunchHit: (Float) -> Unit = { gaugeVal ->
        if (!punchRegistered) {
            punchRegistered = true
            punchGauge = gaugeVal
            phase = "RESOLVED"
            HardwareInputManager.activeQteListener = null

            if (gaugeVal >= 0.75f) {
                hapticManager.playVictoryFanfare()
                onFinished(true, "MEGA HIT! 5/5 SMASHED! (+1 Effort)")
            } else if (gaugeVal >= 0.40f) {
                hapticManager.playAttackImpact()
                onFinished(true, "GOOD HIT! 3/5 SMASHED! (+1 Effort)")
            } else {
                hapticManager.playAttentionAlert()
                onFinished(false, "MISS! 1/5 Hit (Try Again)")
            }
        }
    }

    // Register ultra-low latency hardware button listener
    DisposableEffect(phase) {
        if (phase == "PUNCH") {
            HardwareInputManager.activeQteListener = { button ->
                if (button == VPetButton.B || button == VPetButton.A) {
                    handlePunchHit(animatedGaugeState?.value ?: 0f)
                    true
                } else false
            }
        }
        onDispose {
            HardwareInputManager.activeQteListener = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF140D26))
            .padding(displayConfig.contentPadding)
            .clickable {
                if (phase == "PUNCH") {
                    handlePunchHit(animatedGaugeState?.value ?: 0f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TRAINING QTE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCE93D8),
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(4.dp))

            when (phase) {
                "COUNTDOWN" -> {
                    Text(
                        text = "$countdownValue",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD54F)
                    )
                    Text("GET READY...", fontSize = 10.sp, color = Color.White)
                }
                "PUNCH" -> {
                    Text(
                        text = "PUNCH NOW!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFF1744)
                    )
                    Text(
                        text = "PRESS [B] / STEM OR TAP",
                        fontSize = 9.sp,
                        color = Color(0xFF80D8FF),
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dynamic Power Meter Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(18.dp)
                            .background(Color(0xFF263238), RoundedCornerShape(9.dp))
                            .border(1.dp, Color.White, RoundedCornerShape(9.dp))
                    ) {
                        // Sweet spot background (0.75 - 1.0)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.25f)
                                .height(18.dp)
                                .align(Alignment.CenterEnd)
                                .background(Color(0x6600E676), RoundedCornerShape(topEnd = 9.dp, bottomEnd = 9.dp))
                        )

                        // Moving Indicator (Draw-phase invalidation only: 0 layout passes)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .drawWithContent {
                                    val gauge = animatedGaugeState?.value ?: 0f
                                    val barColor = if (gauge >= 0.75f) Color(0xFF00E676) else Color(0xFFFFD54F)
                                    val radius = 9.dp.toPx()
                                    drawRoundRect(
                                        color = barColor,
                                        size = Size(size.width * gauge.coerceIn(0f, 1f), size.height),
                                        cornerRadius = CornerRadius(radius, radius)
                                    )
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SWEET SPOT 80-100%",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676)
                    )
                }
                "RESOLVED" -> {
                    Text(
                        text = if (punchGauge >= 0.75f) "★ SUPER PUNCH! ★" else "FINISHED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF69F0AE)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            SpriteCanvasRenderer(
                speciesId = petState.speciesId,
                isAmbient = false,
                overrideFrame = if (phase == "PUNCH") SpriteFrame.ATTACK_RELEASE else SpriteFrame.IDLE_A,
                size = 54.dp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(Color(0x44000000), RoundedCornerShape(4.dp))
                    .clickable { onCancel() }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Cancel [C]", fontSize = 8.sp, color = Color.Gray)
            }
        }
    }
}

/**
 * Authentic Bandai Digimon Pendulum Shake Training Minigame.
 * Uses the hardware accelerometer to detect physical wrist shakes during a 3.5-second count window.
 * Bandai authentic sweet spot: 5 to 8 shakes produces a Super Hit / Mega Punch!
 * Zero battery impact: Accelerometer listener is registered ONLY while the SHAKE phase is active
 * and immediately unregistered upon completion or disposal.
 */
@Composable
private fun TrainingShakeView(
    petState: PetState,
    displayConfig: WatchDisplayConfig,
    hapticManager: HapticManager,
    onFinished: (won: Boolean, message: String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var phase by remember { mutableStateOf("COUNTDOWN") } // COUNTDOWN -> SHAKE -> RESOLVED
    var countdownValue by remember { mutableIntStateOf(3) }
    var shakeCount by remember { mutableIntStateOf(0) }
    var timeRemainingMs by remember { mutableIntStateOf(3500) }
    var resolved by remember { mutableStateOf(false) }

    // Countdown logic
    LaunchedEffect(Unit) {
        while (countdownValue > 1) {
            hapticManager.playStepMilestone()
            delay(500L)
            countdownValue--
        }
        hapticManager.playAttackImpact()
        delay(400L)
        phase = "SHAKE"
    }

    // Shake timer countdown (3500ms)
    LaunchedEffect(phase) {
        if (phase == "SHAKE") {
            val stepMs = 50L
            while (timeRemainingMs > 0 && !resolved) {
                delay(stepMs)
                timeRemainingMs = (timeRemainingMs - stepMs.toInt()).coerceAtLeast(0)
            }
            if (!resolved) {
                resolved = true
                phase = "RESOLVED"
                delay(500L)
                when {
                    shakeCount in 5..8 -> {
                        hapticManager.playVictoryFanfare()
                        onFinished(true, "MEGA HIT! $shakeCount SHAKES! (+1 Effort)")
                    }
                    shakeCount in 3..4 || shakeCount in 9..10 -> {
                        hapticManager.playAttackImpact()
                        onFinished(true, "GOOD HIT! $shakeCount SHAKES! (+1 Effort)")
                    }
                    else -> {
                        hapticManager.playAttentionAlert()
                        onFinished(false, "MISS! $shakeCount Shakes (5-8 is Sweet Spot)")
                    }
                }
            }
        }
    }

    // Hardware Accelerometer Listener - strictly scoped to SHAKE phase
    DisposableEffect(phase) {
        if (phase == "SHAKE") {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            val accel = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            var lastShakeTimestamp = 0L

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null || phase != "SHAKE" || resolved) return
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    val gForce = magnitude / SensorManager.GRAVITY_EARTH

                    val now = System.currentTimeMillis()
                    // Detect distinct shake peak (gForce > 1.85g) with 200ms debounce
                    if (gForce > 1.85f && (now - lastShakeTimestamp) > 200L) {
                        lastShakeTimestamp = now
                        shakeCount++
                        hapticManager.playStepMilestone()
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            if (sensorManager != null && accel != null) {
                sensorManager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_GAME)
            }

            // Also allow physical button tap / screen tap as fallback
            HardwareInputManager.activeQteListener = { button ->
                if (button == VPetButton.B || button == VPetButton.A) {
                    shakeCount++
                    hapticManager.playStepMilestone()
                    true
                } else false
            }

            onDispose {
                sensorManager?.unregisterListener(listener)
                HardwareInputManager.activeQteListener = null
            }
        } else {
            onDispose {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF140D26))
            .padding(displayConfig.contentPadding)
            .clickable {
                if (phase == "SHAKE" && !resolved) {
                    shakeCount++
                    hapticManager.playStepMilestone()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "PENDULUM SHAKE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD54F),
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(4.dp))

            when (phase) {
                "COUNTDOWN" -> {
                    Text(
                        text = "$countdownValue",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD54F)
                    )
                    Text("GET READY TO SHAKE...", fontSize = 9.sp, color = Color.White)
                }
                "SHAKE" -> {
                    Text(
                        text = "SHAKE! [ $shakeCount ]",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = if (shakeCount in 5..8) Color(0xFF00E676) else Color(0xFFFF1744)
                    )
                    Text(
                        text = "SWEET SPOT: 5-8 SHAKES",
                        fontSize = 8.sp,
                        color = Color(0xFF80D8FF),
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Time remaining bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(8.dp)
                            .background(Color(0xFF263238), RoundedCornerShape(4.dp))
                            .border(0.5.dp, Color.White, RoundedCornerShape(4.dp))
                    ) {
                        val progress = (timeRemainingMs / 3500f).coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .background(
                                    if (shakeCount in 5..8) Color(0xFF00E676) else Color(0xFFFFD54F),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Shake watch or tap [B]",
                        fontSize = 7.sp,
                        color = Color.LightGray
                    )
                }
                "RESOLVED" -> {
                    Text(
                        text = if (shakeCount in 5..8) "★ SUPER HIT! ★" else if (shakeCount in 3..4 || shakeCount in 9..10) "GOOD HIT!" else "MISS!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (shakeCount in 5..8) Color(0xFF69F0AE) else if (shakeCount in 3..4 || shakeCount in 9..10) Color(0xFFFFD54F) else Color(0xFFFF5252)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            SpriteCanvasRenderer(
                speciesId = petState.speciesId,
                isAmbient = false,
                overrideFrame = if (phase == "SHAKE") {
                    if (shakeCount % 2 == 0) SpriteFrame.WALK_A else SpriteFrame.ATTACK_RELEASE
                } else SpriteFrame.IDLE_A,
                size = 54.dp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(Color(0x44000000), RoundedCornerShape(4.dp))
                    .clickable { onCancel() }
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Cancel [C]", fontSize = 8.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun AmbientPetScreen(
    petState: PetState,
    displayConfig: WatchDisplayConfig = WatchDisplayConfig.current()
) {
    val ambientState = LocalAmbientState.current
    val sdf = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    // Driven purely by the 1-minute hardware/ambient tick — zero background coroutine wakeups
    val currentTime = remember(ambientState.ambientTick) { sdf.format(Date()) }

    // 4-quadrant ±2.dp pixel-shift cycle when OLED burn-in protection is active
    val shiftStep = (ambientState.ambientTick % 4L).toInt()
    val shiftX = if (ambientState.burnInProtection) {
        if (shiftStep == 0 || shiftStep == 3) (-2).dp else 2.dp
    } else 0.dp
    val shiftY = if (ambientState.burnInProtection) {
        if (shiftStep < 2) (-2).dp else 2.dp
    } else 0.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            // Minimum 10px boundary enforcement for OLED burn-in protection
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.offset(x = shiftX, y = shiftY),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentTime,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Monochrome Outline Sprite (OPR <= 15%)
            SpriteCanvasRenderer(
                speciesId = petState.speciesId,
                isAmbient = true,
                isSleeping = petState.isSleeping,
                isInjured = petState.isInjured,
                size = if (displayConfig.isRound) 76.dp else 88.dp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (petState.attentionCallActive) "⚠ CALL" else if (petState.isSleeping) "ZZZ" else "OK",
                fontSize = 10.sp,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
