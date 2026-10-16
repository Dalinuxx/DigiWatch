package com.digiwatch.wear.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.digiwatch.wear.data.PetRepository
import com.digiwatch.wear.haptic.HapticManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepTrackerManager(
    private val context: Context,
    private val repository: PetRepository,
    private val hapticManager: HapticManager,
    private val scope: CoroutineScope
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val prefs = context.getSharedPreferences("digiwatch_step_prefs", Context.MODE_PRIVATE)

    @Volatile
    private var pendingSteps: Int = 0
    private var lastFlushTimestampMs: Long = 0L

    fun startListening() {
        stepCounterSensor?.let {
            // Enable 5-second hardware FIFO batching (5,000,000 us) to avoid waking AP on every single step
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI, 5_000_000)
        }
    }

    fun stopListening() {
        flushPendingSteps()
        sensorManager.unregisterListener(this)
    }

    fun flushPendingSteps() {
        val stepsToFlush = synchronized(this) {
            val s = pendingSteps
            pendingSteps = 0
            s
        }
        if (stepsToFlush > 0) {
            lastFlushTimestampMs = System.currentTimeMillis()
            scope.launch(Dispatchers.IO) {
                val result = repository.addAdventureSteps(stepsToFlush)
                if (result.nodeCleared || result.bossEncounter != null) {
                    hapticManager.playStepMilestone()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_STEP_COUNTER) return

        val rawSteps = event.values[0].toInt()
        val lastSavedSteps = prefs.getInt("last_hardware_step_count", -1)

        // First boot or hardware counter reset after device reboot
        if (lastSavedSteps < 0 || rawSteps < lastSavedSteps) {
            prefs.edit().putInt("last_hardware_step_count", rawSteps).apply()
            return
        }

        val deltaSteps = (rawSteps - lastSavedSteps).coerceAtMost(50_000)
        if (deltaSteps > 0) {
            prefs.edit().putInt("last_hardware_step_count", rawSteps).apply()
            val now = System.currentTimeMillis()
            val shouldFlush = synchronized(this) {
                pendingSteps += deltaSteps
                pendingSteps >= 10 || (now - lastFlushTimestampMs >= 5_000L)
            }
            if (shouldFlush) {
                flushPendingSteps()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
