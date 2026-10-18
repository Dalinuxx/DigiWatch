package com.digiwatch.wear.haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticManager(context: Context) {

    private val appContext = context.applicationContext

    private val vibrator: Vibrator? by lazy(LazyThreadSafetyMode.NONE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private val attentionAlertEffect: VibrationEffect by lazy(LazyThreadSafetyMode.NONE) {
        VibrationEffect.createWaveform(longArrayOf(0, 150, 80, 150, 80, 300), -1)
    }

    private val attackImpactEffect: VibrationEffect by lazy(LazyThreadSafetyMode.NONE) {
        VibrationEffect.createWaveform(longArrayOf(0, 60, 40, 100), -1)
    }

    private val victoryFanfareEffect: VibrationEffect by lazy(LazyThreadSafetyMode.NONE) {
        VibrationEffect.createWaveform(longArrayOf(0, 100, 100, 100, 100, 250, 100, 400), -1)
    }

    private val stepMilestoneEffect: VibrationEffect by lazy(LazyThreadSafetyMode.NONE) {
        VibrationEffect.createWaveform(longArrayOf(0, 80, 60, 80), -1)
    }

    var isEnabled: Boolean = true
    var buzzerManager: com.digiwatch.wear.sound.BuzzerManager? = null

    fun playAttentionAlert() {
        buzzerManager?.playAttentionBeep()
        if (!isEnabled) return
        vibrator?.vibrate(attentionAlertEffect)
    }

    fun playAttackImpact() {
        buzzerManager?.playAttackImpactTone()
        if (!isEnabled) return
        vibrator?.vibrate(attackImpactEffect)
    }

    fun playVictoryFanfare() {
        buzzerManager?.playVictoryFanfare()
        if (!isEnabled) return
        vibrator?.vibrate(victoryFanfareEffect)
    }

    fun playStepMilestone() {
        buzzerManager?.playButtonBeep()
        if (!isEnabled) return
        vibrator?.vibrate(stepMilestoneEffect)
    }
}
