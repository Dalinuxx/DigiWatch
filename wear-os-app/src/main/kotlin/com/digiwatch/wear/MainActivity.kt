package com.digiwatch.wear

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import com.digiwatch.wear.nfc.NfcDuelManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.digiwatch.core.model.PetState
import com.digiwatch.wear.ambient.AmbientController
import com.digiwatch.wear.ambient.LocalAmbientState
import com.digiwatch.wear.data.PetRepository
import com.digiwatch.wear.haptic.HapticManager
import com.digiwatch.wear.sensor.StepTrackerManager
import com.digiwatch.wear.ui.AdventureScreen
import com.digiwatch.wear.ui.AmbientPetScreen
import com.digiwatch.wear.ui.BattleScreen
import com.digiwatch.wear.ui.MainPetScreen
import com.digiwatch.wear.ui.SettingsScreen
import com.digiwatch.wear.ui.StatusScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import android.view.KeyEvent
import android.view.MotionEvent
import com.digiwatch.wear.input.HardwareInputManager
import com.digiwatch.wear.input.VPetButton

enum class WearScreen {
    MAIN,
    BATTLE,
    ADVENTURE,
    STATUS,
    SETTINGS
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: PetRepository
    private lateinit var hapticManager: HapticManager
    private lateinit var buzzerManager: com.digiwatch.wear.sound.BuzzerManager
    private lateinit var stepTracker: StepTrackerManager
    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = PetRepository.getInstance(applicationContext)
        hapticManager = HapticManager(this)
        buzzerManager = com.digiwatch.wear.sound.BuzzerManager()
        hapticManager.buzzerManager = buzzerManager
        stepTracker = StepTrackerManager(this, repository, hapticManager, activityScope)

        // Start AOSP Wi-Fi / Hotspot LAN Sync Bridge & UDP Discovery Responder
        com.digiwatch.wear.datalayer.AospLanSyncServer.start(applicationContext)

        // Request ACTIVITY_RECOGNITION / POST_NOTIFICATIONS on AOSP & Android 10+ watches
        requestWatchRuntimePermissionsIfNeeded()

        setContent {
            AmbientController(
                activity = this,
                onEnterAmbient = {
                    stepTracker.onEnterAmbient()
                },
                onExitAmbient = {
                    stepTracker.onExitAmbient()
                },
                onAmbientTick = {
                    activityScope.launch(Dispatchers.IO) {
                        repository.processDecay()
                    }
                }
            ) {
                WearNavigationHost(repository, hapticManager, buzzerManager)
            }
        }
    }

    private fun requestWatchRuntimePermissionsIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val perms = mutableListOf<String>()
            if (checkSelfPermission(android.Manifest.permission.ACTIVITY_RECOGNITION) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                perms.add(android.Manifest.permission.ACTIVITY_RECOGNITION)
            }
            if (android.os.Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                perms.add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
            if (perms.isNotEmpty()) {
                try {
                    requestPermissions(perms.toTypedArray(), 1001)
                } catch (_: Exception) {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        stepTracker.startListening()
        activityScope.launch(Dispatchers.IO) {
            repository.processDecay()
        }
        if (NfcDuelManager.isNfcEnabled(this)) {
            NfcDuelManager.enableReaderMode(this) { opponentCode ->
                repository.setBeamedCommCode(opponentCode)
                hapticManager.playStepMilestone()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stepTracker.stopListening()
        NfcDuelManager.disableReaderMode(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = androidx.core.content.IntentCompat.getParcelableExtra(intent, NfcAdapter.EXTRA_TAG, Tag::class.java)
        if (tag != null) {
            val opponentCode = NfcDuelManager.extractCommCodeFromTag(tag)
            repository.setBeamedCommCode(opponentCode)
            hapticManager.playStepMilestone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        com.digiwatch.wear.datalayer.AospLanSyncServer.stop()
        buzzerManager.release()
        activityScope.coroutineContext[kotlinx.coroutines.Job]?.cancel()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val button = when (keyCode) {
            KeyEvent.KEYCODE_STEM_1,
            KeyEvent.KEYCODE_STEM_PRIMARY,
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_PAGE_UP,
            KeyEvent.KEYCODE_F1 -> VPetButton.A

            KeyEvent.KEYCODE_STEM_2,
            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_SPACE,
            KeyEvent.KEYCODE_CAMERA,
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_F2 -> VPetButton.B

            KeyEvent.KEYCODE_STEM_3,
            KeyEvent.KEYCODE_ESCAPE,
            KeyEvent.KEYCODE_SYSRQ -> VPetButton.C

            KeyEvent.KEYCODE_BACK -> {
                // If consumed by active QTE or menu cancel, intercept back
                if (HardwareInputManager.onButtonDown(VPetButton.C)) {
                    buzzerManager.playButtonBeep()
                    return true
                }
                return super.onKeyDown(keyCode, event)
            }

            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_PAGE_DOWN -> VPetButton.PREV

            else -> null
        }

        if (button != null) {
            val consumed = HardwareInputManager.onButtonDown(button)
            if (consumed) {
                buzzerManager.playButtonBeep()
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_SCROLL) {
            val delta = -event.getAxisValue(MotionEvent.AXIS_SCROLL)
            if (delta > 0.05f) {
                HardwareInputManager.onButtonDown(VPetButton.A)
                buzzerManager.playButtonBeep()
                return true
            } else if (delta < -0.05f) {
                HardwareInputManager.onButtonDown(VPetButton.PREV)
                buzzerManager.playButtonBeep()
                return true
            }
        }
        return super.onGenericMotionEvent(event)
    }
}

@Composable
fun WearNavigationHost(
    repository: PetRepository,
    hapticManager: HapticManager,
    buzzerManager: com.digiwatch.wear.sound.BuzzerManager = remember { com.digiwatch.wear.sound.BuzzerManager() }
) {
    val ambientState = LocalAmbientState.current
    var currentScreen by remember { mutableStateOf(WearScreen.MAIN) }

    // Intercept system back gesture / back button on AOSP & Wear OS watches when on a sub-screen
    androidx.activity.compose.BackHandler(enabled = currentScreen != WearScreen.MAIN) {
        buzzerManager.playButtonBeep()
        currentScreen = WearScreen.MAIN
    }

    LaunchedEffect(Unit) {
        repository.vibrationFlow.collect { enabled ->
            hapticManager.isEnabled = enabled
        }
    }

    LaunchedEffect(Unit) {
        repository.soundEnabledFlow.collect { enabled ->
            buzzerManager.isEnabled = enabled
        }
    }

    // Guarantee low-power monochrome display (OPR <= 15%) in Ambient Mode regardless of active sub-screen
    if (ambientState.isAmbient && currentScreen != WearScreen.MAIN) {
        val petState by repository.petStateFlow.collectAsState(initial = PetState())
        AmbientPetScreen(petState = petState)
        return
    }

    when (currentScreen) {
        WearScreen.MAIN -> MainPetScreen(
            repository = repository,
            hapticManager = hapticManager,
            buzzerManager = buzzerManager,
            onNavigateBattle = { currentScreen = WearScreen.BATTLE },
            onNavigateAdventure = { currentScreen = WearScreen.ADVENTURE },
            onNavigateStatus = { currentScreen = WearScreen.STATUS },
            onNavigateSettings = { currentScreen = WearScreen.SETTINGS }
        )
        WearScreen.BATTLE -> BattleScreen(
            repository = repository,
            hapticManager = hapticManager,
            onBack = { currentScreen = WearScreen.MAIN }
        )
        WearScreen.ADVENTURE -> AdventureScreen(
            repository = repository,
            hapticManager = hapticManager,
            onBack = { currentScreen = WearScreen.MAIN }
        )
        WearScreen.STATUS -> StatusScreen(
            repository = repository,
            onBack = { currentScreen = WearScreen.MAIN }
        )
        WearScreen.SETTINGS -> SettingsScreen(
            repository = repository,
            hapticManager = hapticManager,
            onBack = { currentScreen = WearScreen.MAIN }
        )
    }
}
