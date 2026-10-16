package com.digiwatch.core.model

import kotlinx.serialization.Serializable

/**
 * Multi-variable condition vector for evaluating form mutations.
 * Evaluated against active species branch tables.
 */
@Serializable
data class EvolutionRule(
    val targetSpeciesId: Int,
    val minAgeSeconds: Long = 0L,
    val minCareMistakes: Int = 0,
    val maxCareMistakes: Int = 999,
    val minEffort: Int = 0,
    val minOverfeed: Int = 0,
    val minBattles: Int = 0,
    val minWinRatio: Float = 0.0f,
    val minWeight: Int = 0
) {
    /**
     * Checks whether the given [state] satisfies all condition constraints.
     */
    fun matches(state: PetState): Boolean {
        if (state.ageSeconds < minAgeSeconds) return false
        if (state.careMistakes < minCareMistakes || state.careMistakes > maxCareMistakes) return false
        if (state.effort < minEffort) return false
        if (state.overfeedCount < minOverfeed) return false
        if (state.totalBattles < minBattles) return false
        if (state.winRatio < minWinRatio) return false
        if (state.weight < minWeight) return false
        return true
    }
}
