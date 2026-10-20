package com.digiwear.wear.sound

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    /**
     * Descending 4-tone defeat jingle.
     */
    fun playDefeatJingle() {
        if (!isEnabled) return
        scope.launch {
            val gen = toneGenerator ?: return@launch
            val tones = listOf(
                ToneGenerator.TONE_PROP_PROMPT to 90L,
                ToneGenerator.TONE_PROP_BEEP2 to 90L,
                ToneGenerator.TONE_PROP_BEEP to 110L,
                ToneGenerator.TONE_CDMA_ABBR_ALERT to 220L
            )
            for ((tone, duration) in tones) {
                try {
                    gen.startTone(tone, duration.toInt())
                } catch (_: Exception) {}
                delay(duration + 25L)
            }
        }
    }

    /**
     * Authentic Bandai Piezo Buzzer Evolution Alarm.
     * Plays a high-priority three-phase sequence upon digital monster mutation:
     * 1. 6-beep burst (TONE_PROP_BEEP, 60ms pulse, 50ms pause)
     * 2. Digital glitch warble (TONE_CDMA_NETWORK_USA_RINGBACK, 120ms)
     * 3. 4-tone ascending victory fanfare:
     *    - TONE_PROP_BEEP (80ms)
     *    - TONE_PROP_BEEP2 (80ms)
     *    - TONE_PROP_PROMPT (120ms)
     *    - TONE_CDMA_ALERT_NETWORK_LITE (250ms)
     *
     * Suspending function allowing background workers (such as PetDecayWorker)
     * to hold execution alive until the entire sequence completes (~1.5s total).
     */
    suspend fun playEvolutionAlert() {
        if (!isEnabled) return
        withContext(Dispatchers.Default) {
            val gen = toneGenerator ?: return@withContext
            try {
                // Phase 1: 6-beep burst (60ms pulse, 50ms pause)
                for (i in 0 until 6) {
                    try {
                        gen.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
                    } catch (_: Exception) {}
                    delay(110L)
                }
                delay(40L)

                // Phase 2: 1 digital glitch warble (120ms)
                try {
                    gen.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, 120)
                } catch (_: Exception) {}
                delay(160L)

                // Phase 3: 4-tone ascending victory fanfare
                val fanfare = listOf(
                    ToneGenerator.TONE_PROP_BEEP to 80L,
                    ToneGenerator.TONE_PROP_BEEP2 to 80L,
                    ToneGenerator.TONE_PROP_PROMPT to 120L,
                    ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE to 250L
                )
                for ((tone, duration) in fanfare) {
                    try {
                        gen.startTone(tone, duration.toInt())
                    } catch (_: Exception) {}
                    delay(duration + 30L)
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Asynchronous fire-and-forget evolution alert trigger for UI consumption
     * (e.g. EvolutionCelebrationOverlay or non-coroutine UI event handlers).
     */
    fun playEvolutionAlertAsync() {
        if (!isEnabled) return
        scope.launch {
            playEvolutionAlert()
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
        } catch (_: Exception) {}
    }
}
