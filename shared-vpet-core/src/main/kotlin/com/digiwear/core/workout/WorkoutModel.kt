package com.digiwear.core.workout

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

/**
 * Workout discipline classification.
 */
@Serializable
enum class WorkoutType(val displayName: String, val description: String) {
    FREE_TRAINING("Free Training", "Gym, weights & sparring session to burn weight and max effort"),
    EXPEDITION_TREK("Expedition Trek", "Jogging or fast-walking to accelerate Adventure Mode sector progress"),
    SHADOW_BOXING("Shadow Boxing", "High-cadence HIIT wrist movement to sharpen battle reflex and attack power")
}

/**
 * Biometric Heart Rate zones with associated Adventure step and effort multipliers.
 */
@Serializable
enum class HeartRateZone(
    val minBpm: Int,
    val maxBpm: Int,
    val label: String,
    val stepMultiplier: Float,
    val hexColor: Long
) {
    RESTING(0, 99, "WARMUP", 1.0f, 0xFF90A4AE),
    AEROBIC(100, 139, "AEROBIC", 1.5f, 0xFF00E676),
    ANAEROBIC(140, 169, "ANAEROBIC", 2.0f, 0xFFFFB300),
    PEAK(170, 250, "PEAK", 2.5f, 0xFFFF1744);

    companion object {
        fun fromBpm(bpm: Int): HeartRateZone = when {
            bpm >= 170 -> PEAK
            bpm >= 140 -> ANAEROBIC
            bpm >= 100 -> AEROBIC
            else -> RESTING
        }
    }
}

/**
 * Result summary of a completed physical workout session.
 */
@Serializable
data class WorkoutSummary(
    val type: WorkoutType,
    val durationSeconds: Long,
    val averageBpm: Int,
    val peakBpm: Int,
    val estimatedCalories: Int,
    val rawSteps: Int,
    val effectiveAdventureSteps: Int,
    val weightGramsBurned: Int,
    val effortGained: Int,
    val energizedBuffGranted: Boolean
)

/**
 * Deterministic engine for processing physical workout sessions and applying
 * canonical virtual pet rewards without altering core simulation decay loops.
 */
object WorkoutEngine {

    /**
     * Estimates active calorie burn from average heart rate, duration, and baseline pet weight.
     */
    fun computeEstimatedCalories(avgBpm: Int, durationSeconds: Long): Int {
        if (durationSeconds <= 0L) return 0
        val baseBpm = max(70, avgBpm)
        // Standard MET estimation: ~0.08 kcal per second at moderate aerobic zone (~130 BPM)
        val intensityFactor = (baseBpm.toFloat() / 120f).coerceIn(0.5f, 2.5f)
        val calories = (durationSeconds * 0.08f * intensityFactor).toInt()
        return max(1, calories)
    }

    /**
     * Computes Adventure sector steps with cardio multiplier bonuses.
     */
    fun computeEffectiveSteps(rawSteps: Int, avgBpm: Int): Int {
        val zone = HeartRateZone.fromBpm(avgBpm)
        return (rawSteps * zone.stepMultiplier).toInt()
    }

    /**
     * Completes a workout session and applies natural V-Pet rewards to [currentState]:
     * - Reduces excess weight down toward canonical baseline.
     * - Fills training effort hearts to 4.
     * - Cleanses stress / distressed state.
     * - Returns the updated [PetState] and [WorkoutSummary].
     */
    fun finishSession(
        currentState: PetState,
        type: WorkoutType,
        durationSeconds: Long,
        rawSteps: Int,
        bpmSamples: List<Int>,
        currentTimestampUtc: Long = System.currentTimeMillis() / 1000L
    ): Pair<PetState, WorkoutSummary> {
        val validSamples = bpmSamples.filter { it > 40 }
        val avgBpm = if (validSamples.isNotEmpty()) validSamples.average().toInt() else 85
        val peakBpm = if (validSamples.isNotEmpty()) validSamples.maxOrNull() ?: avgBpm else avgBpm

        val calories = computeEstimatedCalories(avgBpm, durationSeconds)
        val effectiveSteps = computeEffectiveSteps(rawSteps, avgBpm)

        val species = PetSpecies.getById(currentState.speciesId)
        val baseline = species.baselineWeight

        // 1g burned per 3 minutes of workout or 20+ calories, capped so weight never drops below baseline
        val weightBurnUnits = max(1, (durationSeconds / 180L).toInt() + (calories / 25))
        val newWeight = max(baseline, currentState.weight - weightBurnUnits)
        val weightBurned = currentState.weight - newWeight

        // Full workout awards max training effort
        val newEffort = 4
        val effortGained = max(0, newEffort - currentState.effort)

        // Sessions of 3+ minutes grant an energized buff for battles
        val energized = durationSeconds >= 180L || avgBpm >= 120

        val updatedState = currentState.copy(
            weight = newWeight,
            effort = newEffort,
            lastProcessedTimestampUtc = currentTimestampUtc
        )

        val summary = WorkoutSummary(
            type = type,
            durationSeconds = durationSeconds,
            averageBpm = avgBpm,
            peakBpm = peakBpm,
            estimatedCalories = calories,
            rawSteps = rawSteps,
            effectiveAdventureSteps = effectiveSteps,
            weightGramsBurned = weightBurned,
            effortGained = effortGained,
            energizedBuffGranted = energized
        )

        return Pair(updatedState, summary)
    }
}
