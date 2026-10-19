package com.digiwear.wear.sound

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Authentic 1997 Bandai Piezo Buzzer Sound Engine.
 * Produces the signature 8-bit high-pitch square-wave beeps for Call signals,
 * button presses, attack hits, critical cut-ins, and victory fanfares.
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
     * Dramatic battle encounter alarm siren for wild scans and boss clashes.
     */
    fun playEncounterAlarm() {
        if (!isEnabled) return
        scope.launch {
            val gen = toneGenerator ?: return@launch
            try {
                for (i in 0 until 3) {
                    gen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 120)
                    delay(140L)
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Dramatic double-impact chord for Critical Hit cut-ins and mega hits.
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
     * Tactile QTE reflex confirmation chirp.
     */
    fun playQteClickTone() {
        if (!isEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 40)
            } catch (_: Exception) {}
        }
    }

    /**
     * Digital evolution code glitch frequency.
     */
    fun playEvolutionGlitchTone() {
        if (!isEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, 80)
            } catch (_: Exception) {}
        }
    }

    /**
     * Sweeping water flush wave tone.
     */
    fun playFlushTone() {
        if (!isEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL, 160)
            } catch (_: Exception) {}
        }
    }

    /**
     * Medical recovery chime.
     */
    fun playHealTone() {
        if (!isEnabled) return
        scope.launch {
            val gen = toneGenerator ?: return@launch
            try {
                gen.startTone(ToneGenerator.TONE_PROP_BEEP2, 90)
                delay(100L)
                gen.startTone(ToneGenerator.TONE_PROP_PROMPT, 150)
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
