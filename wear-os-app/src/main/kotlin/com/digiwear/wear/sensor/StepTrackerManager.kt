package com.digiwear.wear.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.haptic.HapticManager
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
    private val stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val prefs = context.getSharedPreferences("digiwear_step_prefs", Context.MODE_PRIVATE)

    @Volatile
    private var pendingSteps: Int = 0
    private var lastFlushTimestampMs: Long = 0L
    private var lastAccelStepTimestampMs: Long = 0L

    fun startListening() {
        when {
            stepCounterSensor != null -> {
                sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
            stepDetectorSensor != null -> {
                sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
            accelerometerSensor != null -> {
                // AOSP Standard Watch fallback: wrist accelerometer gait peak detection
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    fun stopListening() {
        flushPendingSteps()
        sensorManager.unregisterListener(this)
    }

    /**
     * Pauses high-frequency CPU accelerometer polling when entering Ambient Mode
     * on hardware lacking a dedicated hardware step counter sensor, allowing the AP to Doze.
     */
    fun onEnterAmbient() {
        flushPendingSteps()
        if (stepCounterSensor == null && accelerometerSensor != null) {
            sensorManager.unregisterListener(this, accelerometerSensor)
        }
    }

    /**
     * Resumes accelerometer polling upon exiting Ambient Mode.
     */
    fun onExitAmbient() {
        if (stepCounterSensor == null && accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
        }
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

    private fun recordStepsDelta(deltaSteps: Int) {
        if (deltaSteps <= 0) return
        val now = System.currentTimeMillis()
        val shouldFlush = synchronized(this) {
            pendingSteps += deltaSteps
            pendingSteps >= 10 || (now - lastFlushTimestampMs >= 5_000L)
        }
        if (shouldFlush) {
            flushPendingSteps()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
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
                    recordStepsDelta(deltaSteps)
                }
            }
            Sensor.TYPE_STEP_DETECTOR -> {
                val steps = event.values.firstOrNull()?.toInt()?.coerceAtLeast(1) ?: 1
                recordStepsDelta(steps)
            }
            Sensor.TYPE_ACCELEROMETER -> {
                // Lightweight gait peak detector (~1.24g threshold with 330ms step cadence debounce)
                val ax = event.values[0]
                val ay = event.values[1]
                val az = event.values[2]
                val mag = kotlin.math.sqrt((ax * ax + ay * ay + az * az).toDouble()).toFloat()
                val now = System.currentTimeMillis()
                if (mag in 12.15f..24.5f && (now - lastAccelStepTimestampMs) >= 330L) {
                    lastAccelStepTimestampMs = now
                    recordStepsDelta(1)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    companion object {
        fun getActivePedometerMode(context: Context): String {
            val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager ?: return "MANUAL_STEP_DECK"
            return when {
                sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null -> "HW STEP COUNTER"
                sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null -> "HW STEP DETECTOR"
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null -> "AOSP ACCEL GAIT"
                else -> "MANUAL STEP DECK"
            }
        }
    }
}
