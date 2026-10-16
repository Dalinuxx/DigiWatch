package com.digiwatch.core.engine

import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetState
import kotlin.math.max

/**
 * Evolution Decision Engine evaluating multi-variable condition vectors against species branch tables.
 */
object EvolutionEngine {

    /**
     * Evaluates whether the current [state] qualifies for an evolutionary mutation.
     * Returns the target [PetSpecies] if an evolution occurs, or null if no mutation conditions are met.
     */
    fun evaluateEvolution(state: PetState): PetSpecies? {
        val currentSpecies = PetSpecies.getById(state.speciesId)
        val candidateRules = currentSpecies.evolutionRules

        for (rule in candidateRules) {
            if (rule.matches(state)) {
                return PetSpecies.getById(rule.targetSpeciesId)
            }
        }
        return null
    }

    /**
     * Applies the evolutionary mutation to [state], adapting HP, baseline weight, and resetting stage counters.
     */
    fun evolve(state: PetState, targetSpecies: PetSpecies, currentTimestampUtc: Long): PetState {
        val newMaxHp = targetSpecies.baseHp
        val newWeight = max(state.weight, targetSpecies.baselineWeight)

        return state.copy(
            speciesId = targetSpecies.id,
            maxHp = newMaxHp,
            currentHp = newMaxHp,
            weight = newWeight,
            // Stage-specific care mistake and effort counters reset upon form mutation
            careMistakes = 0,
            effort = 0,
            overfeedCount = 0,
            lastProcessedTimestampUtc = currentTimestampUtc,
            attentionCallActive = false,
            attentionCallTimestampUtc = null
        )
    }
}
