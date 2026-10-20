package com.digiwear.core.engine

import com.digiwear.core.model.EvolutionRule
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.PetState
import kotlin.math.max

/**
 * Evolution Decision Engine evaluating multi-variable condition vectors against species branch tables,
 * plus lineage graph introspection for Watch and Phone DigiDex guides.
 */
object EvolutionEngine {

    /**
     * All 10 hatchable Baby I starter Digi-Eggs across the 205-species roster.
     */
    val STARTER_EGG_IDS: List<Int> = listOf(
        101, 103, 105, 107, 109, 111, 113, 130, 131, 132,
        1101, 1104, 1106, 1108, 1111, 1113
    )

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

    /**
     * Returns all species and the specific [EvolutionRule] that can directly evolve into [targetSpeciesId].
     */
    fun getPreEvolutions(targetSpeciesId: Int): List<Pair<PetSpecies, EvolutionRule>> {
        val results = mutableListOf<Pair<PetSpecies, EvolutionRule>>()
        for (species in PetSpecies.SPECIES_REGISTRY.values.sortedBy { it.id }) {
            for (rule in species.evolutionRules) {
                if (rule.targetSpeciesId == targetSpeciesId) {
                    results.add(species to rule)
                }
            }
        }
        return results
    }

    /**
     * Finds the shortest canonical natural evolution path from one of the 10 Baby I starter eggs
     * to [targetSpeciesId]. Returns the list of species IDs from Baby I to [targetSpeciesId].
     */
    fun findEggLineagePath(targetSpeciesId: Int): List<Int> {
        if (targetSpeciesId in STARTER_EGG_IDS) return listOf(targetSpeciesId)

        val visited = mutableSetOf<Int>()
        val parentMap = mutableMapOf<Int, Int>()
        val queue = ArrayDeque<Int>()

        for (eggId in STARTER_EGG_IDS) {
            if (PetSpecies.SPECIES_REGISTRY.containsKey(eggId)) {
                visited.add(eggId)
                queue.addLast(eggId)
            }
        }

        while (queue.isNotEmpty()) {
            val currentId = queue.removeFirst()
            if (currentId == targetSpeciesId) {
                val path = mutableListOf<Int>()
                var cursor: Int? = targetSpeciesId
                while (cursor != null) {
                    path.add(0, cursor)
                    cursor = parentMap[cursor]
                }
                return path
            }
            val species = PetSpecies.SPECIES_REGISTRY[currentId] ?: continue
            for (rule in species.evolutionRules) {
                val nextId = rule.targetSpeciesId
                if (nextId !in visited && PetSpecies.SPECIES_REGISTRY.containsKey(nextId)) {
                    visited.add(nextId)
                    parentMap[nextId] = currentId
                    queue.addLast(nextId)
                }
            }
        }
        return emptyList()
    }

    /**
     * Formats an [EvolutionRule] into a concise human-readable requirement summary for Watch/Phone UI.
     */
    fun describeRule(rule: EvolutionRule): String {
        val parts = mutableListOf<String>()
        if (rule.minAgeSeconds > 0L) {
            val hours = rule.minAgeSeconds / 3600L
            parts.add("Age ≥ ${hours}h")
        }
        if (rule.minCareMistakes > 0 || rule.maxCareMistakes < 999) {
            val cmText = when {
                rule.minCareMistakes == rule.maxCareMistakes -> "CM = ${rule.minCareMistakes}"
                rule.maxCareMistakes >= 999 -> "CM ≥ ${rule.minCareMistakes}"
                else -> "CM ${rule.minCareMistakes}–${rule.maxCareMistakes}"
            }
            parts.add(cmText)
        }
        if (rule.minEffort > 0) parts.add("Effort ≥ ${rule.minEffort}♥")
        if (rule.minBattles > 0) parts.add("Battles ≥ ${rule.minBattles}")
        if (rule.minWinRatio > 0f) parts.add("Win ≥ ${(rule.minWinRatio * 100).toInt()}%")
        if (rule.minWeight > 0) parts.add("Weight ≥ ${rule.minWeight}G")
        if (rule.minOverfeed > 0) parts.add("Overfeed ≥ ${rule.minOverfeed}")
        if (rule.minVitalPoints > 0) parts.add("Vitals ≥ ${rule.minVitalPoints} VP")
        return if (parts.isEmpty()) "Natural Ascension" else parts.joinToString(" • ")
    }
}
