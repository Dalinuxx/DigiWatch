package com.digiwatch.core.model

import kotlinx.serialization.Serializable

/**
 * Pet life stages adhering to classic V1-V6 physical virtual pet benchmarks.
 */
@Serializable
enum class PetStage(
    val stageIndex: Int,
    val displayName: String,
    val hungerLossMinutes: Int,
    val strengthLossMinutes: Int,
    val wasteIntervalMinutes: Int,
    val sleepStartHour: Int?,
    val sleepEndHour: Int?,
    val graceWindowMinutes: Int,
    val careMistakesEnabled: Boolean
) {
    BABY_I(
        stageIndex = 1,
        displayName = "Baby I",
        hungerLossMinutes = 3,
        strengthLossMinutes = 3,
        wasteIntervalMinutes = 15,
        sleepStartHour = null,
        sleepEndHour = null,
        graceWindowMinutes = 0,
        careMistakesEnabled = false
    ),
    BABY_II(
        stageIndex = 2,
        displayName = "Baby II",
        hungerLossMinutes = 10,
        strengthLossMinutes = 10,
        wasteIntervalMinutes = 30,
        sleepStartHour = 20,
        sleepEndHour = 7,
        graceWindowMinutes = 10,
        careMistakesEnabled = true
    ),
    CHILD(
        stageIndex = 3,
        displayName = "Child",
        hungerLossMinutes = 30,
        strengthLossMinutes = 30,
        wasteIntervalMinutes = 60,
        sleepStartHour = 21,
        sleepEndHour = 8,
        graceWindowMinutes = 10,
        careMistakesEnabled = true
    ),
    ADULT(
        stageIndex = 4,
        displayName = "Adult",
        hungerLossMinutes = 45,
        strengthLossMinutes = 45,
        wasteIntervalMinutes = 90,
        sleepStartHour = 22,
        sleepEndHour = 8,
        graceWindowMinutes = 15,
        careMistakesEnabled = true
    ),
    PERFECT(
        stageIndex = 5,
        displayName = "Perfect",
        hungerLossMinutes = 60,
        strengthLossMinutes = 60,
        wasteIntervalMinutes = 120,
        sleepStartHour = 23,
        sleepEndHour = 9,
        graceWindowMinutes = 15,
        careMistakesEnabled = true
    ),
    ULTIMATE(
        stageIndex = 6,
        displayName = "Ultimate",
        hungerLossMinutes = 90,
        strengthLossMinutes = 90,
        wasteIntervalMinutes = 180,
        sleepStartHour = 23,
        sleepEndHour = 9,
        graceWindowMinutes = 15,
        careMistakesEnabled = true
    );

    /**
     * Checks if a given 24h clock hour and minute falls within this stage's sleep window.
     */
    fun isSleepWindow(hour: Int, minute: Int): Boolean {
        val start = sleepStartHour ?: return false
        val end = sleepEndHour ?: return false

        return if (start > end) {
            // Wraps over midnight (e.g. 21:00 to 08:00)
            hour >= start || hour < end || (hour == end && minute == 0)
        } else {
            // Same-day window
            hour in start until end || (hour == end && minute == 0)
        }
    }

    companion object {
        fun fromIndex(index: Int): PetStage =
            entries.firstOrNull { it.stageIndex == index } ?: BABY_I
    }
}
