package com.digiwear.wear.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.digiwear.wear.MainActivity
import com.digiwear.wear.R
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.sound.BuzzerManager

class PetDecayWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repo = PetRepository.getInstance(context)
        val state = repo.processDecay(System.currentTimeMillis() / 1000L)
        val prefs = context.getSharedPreferences("digiwear_wear_prefs", Context.MODE_PRIVATE)

        if (state.attentionCallActive) {
            val callTimestamp = state.attentionCallTimestampUtc ?: 0L
            val lastNotifiedCall = prefs.getLong(KEY_LAST_NOTIFIED_CALL_UTC, -1L)
            if (callTimestamp != lastNotifiedCall) {
                prefs.edit().putLong(KEY_LAST_NOTIFIED_CALL_UTC, callTimestamp).apply()
                if (prefs.getBoolean("vibration_enabled", true)) {
                    triggerVibrationAlert()
                }
                if (prefs.getBoolean("sound_enabled", false)) {
                    BuzzerManager().apply { isEnabled = true }.playAttentionBeep()
                }
                postNotification()
            }
        } else if (prefs.getLong(KEY_LAST_NOTIFIED_CALL_UTC, -1L) != -1L) {
            prefs.edit().remove(KEY_LAST_NOTIFIED_CALL_UTC).apply()
        }

        return Result.success()
    }

    private fun triggerVibrationAlert() {
        val pattern = longArrayOf(0, 200, 100, 200, 100, 400)
        val effect = VibrationEffect.createWaveform(pattern, -1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.vibrate(effect)
        }
    }

    private fun postNotification() {
        val channelId = "pet_attention_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(channelId) == null) {
            val channel = NotificationChannel(
                channelId,
                "Pet Attention Call",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when pet requires food, training, or care"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("DigiWear Call Signal!")
            .setContentText("Your digital monster requires immediate attention!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1001, notification)
        }
    }

    companion object {
        private const val KEY_LAST_NOTIFIED_CALL_UTC = "last_notified_call_utc"
    }
}
