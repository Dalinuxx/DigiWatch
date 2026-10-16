package com.digiwatch.wear.sound

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Authentic 1997 Bandai Piezo Buzzer Sound Engine.
 * Produces the signature 8-bit high-pitch square-wave beeps for Call signals,
 * button presses, attack hits, and victory fanfares.
 * Completely optional: Muted by default so users in meetings are not disturbed.
 */
class BuzzerManager {

    var isEnabled: Boolean = false

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
     * Tactile bandai stem button click chirp (15ms).
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
