package com.digiwear.wear.ambient

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.wear.ambient.AmbientLifecycleObserver
import kotlinx.coroutines.delay

data class AmbientState(
    val isAmbient: Boolean = false,
    val burnInProtection: Boolean = false,
    val lowBitAmbient: Boolean = false,
    val ambientTick: Long = 0L
)

val LocalAmbientState = compositionLocalOf { AmbientState() }

@Composable
fun AmbientController(
    activity: ComponentActivity,
    onEnterAmbient: () -> Unit = {},
    onExitAmbient: () -> Unit = {},
    onAmbientTick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var isAmbient by remember { mutableStateOf(false) }
    var burnInProtection by remember { mutableStateOf(false) }
    var lowBitAmbient by remember { mutableStateOf(false) }
    var ambientTick by remember { mutableLongStateOf(0L) }
    var lastHardwareTickMs by remember { mutableLongStateOf(0L) }

    val isWearableSharedLibraryAvailable = remember {
        if (!activity.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WATCH)) {
            false
        } else {
            try {
                val clazz = Class.forName("androidx.wear.ambient.SharedLibraryVersion")
                val method = clazz.getDeclaredMethod("verifySharedLibraryPresent")
                method.isAccessible = true
                method.invoke(null)
                true
            } catch (_: Throwable) {
                false
            }
        }
    }

    DisposableEffect(activity, isWearableSharedLibraryAvailable) {
        if (!isWearableSharedLibraryAvailable) {
            return@DisposableEffect onDispose {}
        }

        val callback = object : AmbientLifecycleObserver.AmbientLifecycleCallback {
            override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
                isAmbient = true
                burnInProtection = ambientDetails.burnInProtectionRequired
                lowBitAmbient = ambientDetails.deviceHasLowBitAmbient
                val now = System.currentTimeMillis()
                lastHardwareTickMs = now
                ambientTick = now
                onEnterAmbient()
            }

            override fun onExitAmbient() {
                isAmbient = false
                onExitAmbient()
            }

            override fun onUpdateAmbient() {
                val now = System.currentTimeMillis()
                lastHardwareTickMs = now
                ambientTick = now
                onAmbientTick()
            }
        }

        var observer: AmbientLifecycleObserver? = null
        try {
            val obs = AmbientLifecycleObserver(activity, callback)
            activity.lifecycle.addObserver(obs)
            observer = obs
        } catch (e: Throwable) {
            android.util.Log.w("AmbientController", "AmbientLifecycleObserver unavailable on this hardware: ${e.message}")
        }

        onDispose {
            observer?.let { activity.lifecycle.removeObserver(it) }
        }
    }

    // Fallback 1-minute tick only when hardware onUpdateAmbient has not fired within 55s
    LaunchedEffect(isAmbient) {
        while (isAmbient) {
            delay(60_000L)
            val now = System.currentTimeMillis()
            if (now - lastHardwareTickMs >= 55_000L) {
                lastHardwareTickMs = now
                ambientTick = now
                onAmbientTick()
            }
        }
    }

    val state = remember(isAmbient, burnInProtection, lowBitAmbient, ambientTick) {
        AmbientState(
            isAmbient = isAmbient,
            burnInProtection = burnInProtection,
            lowBitAmbient = lowBitAmbient,
            ambientTick = ambientTick
        )
    }

    CompositionLocalProvider(LocalAmbientState provides state) {
        content()
    }
}
