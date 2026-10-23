package com.digiwear.companion.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Authentic 1997 Bandai Piezo Buzzer Sound Engine for Phone Companion.
 * Produces pure 1-bit / 8-bit square-wave PCM chiptunes using a synthesized AudioTrack pipeline,
 * accurately matching the physical piezoelectric buzzer of 1997 Digital Monster, Pendulum, and Color LCDs.
 * Gracefully falls back to ToneGenerator if AudioTrack hardware buffers are unavailable.
 * Respects user settings and is muted by default.
 */
class BuzzerManager(context: Context) {

    private val prefs = context.getSharedPreferences("digiwear_companion_prefs", Context.MODE_PRIVATE)

    var isEnabled: Boolean
        get() = prefs.getBoolean("retro_sound_enabled", false)
        set(value) = prefs.edit().putBoolean("retro_sound_enabled", value).apply()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    private var toneGeneratorInitialized = false

    private val toneGenerator: ToneGenerator? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        toneGeneratorInitialized = true
        try {
            ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
        } catch (_: Exception) {
            null
        }
    }

    @Volatile
    private var synthInitialized = false

    private val synth: PureSquareWaveSynth by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        synthInitialized = true
        PureSquareWaveSynth()
    }

    /**
     * Iconic Bandai 4-beep attention call sequence (pip-pip-pip-pip).
     */
    fun playAttentionBeep() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                for (i in 0 until 4) {
                    synth.playTone(2400f, 70)
                    synth.playSilence(60)
                }
            } else {
                val gen = toneGenerator ?: return@launch
                for (i in 0 until 4) {
                    try {
                        gen.startTone(ToneGenerator.TONE_PROP_BEEP, 75)
                    } catch (_: Exception) {}
                    delay(130L)
                }
            }
        }
    }

    /**
     * Tactile bandai stem button click chirp (18ms).
     */
    fun playButtonBeep() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                synth.playTone(3200f, 18)
            } else {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 20)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Punch / attack impact tone.
     */
    fun playAttackImpactTone() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                synth.playTone(1400f, 40)
                synth.playTone(800f, 60)
            } else {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Dramatic battle encounter alarm siren for wild scans and boss clashes.
     */
    fun playEncounterAlarm() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                for (i in 0 until 3) {
                    synth.playTone(3000f, 80)
                    synth.playTone(2200f, 80)
                }
            } else {
                val gen = toneGenerator ?: return@launch
                try {
                    for (i in 0 until 3) {
                        gen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 120)
                        delay(140L)
                    }
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Dramatic double-impact chord for Critical Hit cut-ins and boss encounters.
     */
    fun playCriticalCutInTone() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                synth.playTone(3600f, 100)
                synth.playSilence(20)
                synth.playTone(2800f, 150)
            } else {
                val gen = toneGenerator ?: return@launch
                try {
                    gen.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 120)
                    delay(140L)
                    gen.startTone(ToneGenerator.TONE_PROP_PROMPT, 180)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Tactile QTE reflex confirmation chirp.
     */
    fun playQteClickTone() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                synth.playTone(2800f, 35)
            } else {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 40)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Digital evolution code glitch frequency.
     */
    fun playEvolutionGlitchTone() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                for (i in 0 until 4) {
                    synth.playTone(1800f, 20)
                    synth.playTone(3200f, 20)
                }
            } else {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, 80)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Sweeping water flush wave tone.
     */
    fun playFlushTone() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                synth.playTone(1600f, 50)
                synth.playTone(1200f, 50)
                synth.playTone(900f, 60)
            } else {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL, 160)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Medical recovery chime.
     */
    fun playHealTone() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                synth.playTone(2093f, 80)
                synth.playSilence(20)
                synth.playTone(3136f, 140)
            } else {
                val gen = toneGenerator ?: return@launch
                try {
                    gen.startTone(ToneGenerator.TONE_PROP_BEEP2, 90)
                    delay(100L)
                    gen.startTone(ToneGenerator.TONE_PROP_PROMPT, 150)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Ascending 4-tone victory fanfare.
     */
    fun playVictoryFanfare() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                val notes = listOf(2093f to 80, 2637f to 80, 3136f to 100, 4186f to 250)
                for ((f, d) in notes) {
                    synth.playTone(f, d)
                    synth.playSilence(25)
                }
            } else {
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
    }

    /**
     * Descending 4-tone defeat jingle.
     */
    fun playDefeatJingle() {
        if (!isEnabled) return
        scope.launch {
            if (synth.isAvailable) {
                val notes = listOf(3136f to 90, 2637f to 90, 2093f to 100, 1568f to 200)
                for ((f, d) in notes) {
                    synth.playTone(f, d)
                    synth.playSilence(25)
                }
            } else {
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
    }

    /**
     * Authentic Bandai Piezo Buzzer Evolution Alarm.
     */
    suspend fun playEvolutionAlert() {
        if (!isEnabled) return
        withContext(Dispatchers.Default) {
            if (synth.isAvailable) {
                // Phase 1: 6-beep burst
                for (i in 0 until 6) {
                    synth.playTone(2400f, 60)
                    synth.playSilence(50)
                }
                // Phase 2: Digital glitch flutter
                for (i in 0 until 4) {
                    synth.playTone(1800f, 20)
                    synth.playTone(3200f, 20)
                }
                synth.playSilence(40)
                // Phase 3: Ascending victory fanfare
                val fanfare = listOf(2093f to 80, 2637f to 80, 3136f to 120, 4186f to 250)
                for ((f, d) in fanfare) {
                    synth.playTone(f, d)
                    synth.playSilence(30)
                }
            } else {
                val gen = toneGenerator ?: return@withContext
                try {
                    for (i in 0 until 6) {
                        try {
                            gen.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
                        } catch (_: Exception) {}
                        delay(110L)
                    }
                    delay(40L)
                    try {
                        gen.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, 120)
                    } catch (_: Exception) {}
                    delay(160L)
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
    }

    fun playEvolutionAlertAsync() {
        if (!isEnabled) return
        scope.launch {
            playEvolutionAlert()
        }
    }

    fun release() {
        scope.cancel()
        if (synthInitialized) {
            synth.release()
        }
        if (toneGeneratorInitialized) {
            try {
                toneGenerator?.release()
            } catch (_: Exception) {}
        }
    }

    /**
     * Pure square-wave PCM audio synthesizer generating in-memory chiptune frequencies via AudioTrack.
     */
    private class PureSquareWaveSynth {
        private val sampleRate = 22050
        private var track: AudioTrack? = null

        init {
            try {
                val minBuf = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBuf.coerceAtLeast(4096))
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
                audioTrack.play()
                track = audioTrack
            } catch (_: Exception) {
                track = null
            }
        }

        val isAvailable: Boolean
            get() = track?.playState == AudioTrack.PLAYSTATE_PLAYING

        fun playTone(freqHz: Float, durationMs: Int, volume: Short = 16000) {
            val t = track ?: return
            val totalSamples = (sampleRate * (durationMs / 1000f)).toInt()
            if (totalSamples <= 0) return
            val period = (sampleRate / freqHz).coerceAtLeast(2f)
            val halfPeriod = period / 2f
            val buffer = ShortArray(totalSamples)
            for (i in 0 until totalSamples) {
                val phase = i % period.toInt()
                buffer[i] = if (phase < halfPeriod) volume else (-volume).toShort()
            }
            try {
                t.write(buffer, 0, buffer.size)
            } catch (_: Exception) {}
        }

        fun playSilence(durationMs: Int) {
            val t = track ?: return
            val totalSamples = (sampleRate * (durationMs / 1000f)).toInt()
            if (totalSamples <= 0) return
            val buffer = ShortArray(totalSamples)
            try {
                t.write(buffer, 0, buffer.size)
            } catch (_: Exception) {}
        }

        fun release() {
            try {
                track?.stop()
                track?.release()
            } catch (_: Exception) {}
            track = null
        }
    }
}
