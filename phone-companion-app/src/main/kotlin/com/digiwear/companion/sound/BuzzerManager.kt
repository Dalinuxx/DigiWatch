package com.digiwear.companion.sound

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Authentic 1997 Bandai Piezo Buzzer Sound Engine for Phone Companion.
 * Produces signature 8-bit square wave beeps for Call signals, button taps,
 * critical cut-in battle moments, and victory fanfares.
 * Respects user settings and is muted by default.
 */
class BuzzerManager(context: Context) {

    private val prefs = context.getSharedPreferences("digiwear_companion_prefs", Context.MODE_PRIVATE)

    var isEnabled: Boolean
        get() = prefs.getBoolean("retro_sound_enabled", false)
        set(value) = prefs.edit().putBoolean("retro_sound_enabled", value).apply()

    private val scope = CoroutineScope(Dispatchers.Default)

    private val toneGenerator: ToneGenerator? by lazy(LazyThreadSafetyMode.NONE) {
        try {
            ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Iconic Bandai 4-beep attention call sequence (pip-pip-pip-pip).
     */
    fun playAttentionBeep() {
        if (!isEnabled) return
        scope.launch {
            val gen = toneGenerator ?: return@launch
            for (i in 0 until 4) {
                try {
                    gen.startTone(ToneGenerator.TONE_PROP_BEEP, 75)
                } catch (_: Exception) {}
                delay(130L)
            }
        }
    }

    /**
     * Tactile bandai stem button click chirp (20ms).
     */
    fun playButtonBeep() {
        if (!isEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 20)
            } catch (_: Exception) {}
        }
    }

    /**
     * Punch / attack impact tone.
     */
    fun playAttackImpactTone() {
        if (!isEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
            } catch (_: Exception) {}
        }
    }

    /**
     * Dramatic double-impact chord for Critical Hit cut-ins and boss encounters.
     */
    fun playCriticalCutInTone() {
        if (!isEnabled) return
        scope.launch {
            val gen = toneGenerator ?: return@launch
            try {
                gen.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 120)
                delay(140L)
                gen.startTone(ToneGenerator.TONE_PROP_PROMPT, 180)
            } catch (_: Exception) {}
        }
    }

    /**
     * Ascending 4-tone victory fanfare.
     */
    fun playVictoryFanfare() {
        if (!isEnabled) return
        scope.launch {
            val gen = toneGenerator ?: return@launch
            val tones = listOf(
                ToneGenerator.TONE_PROP_BEEP to 80L,
                ToneGenerator.TONE_PROP_BEEP2 to 80L,
                ToneGenerator.TONE_PROP_PROMPT to 120L,
                ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE to 250L
            )
            for ((tone, duration) in tones) {
                try {
                    gen.startTone(tone, duration.toInt())
                } catch (_: Exception) {}
                delay(duration + 30L)
            }
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
        } catch (_: Exception) {}
    }
}
