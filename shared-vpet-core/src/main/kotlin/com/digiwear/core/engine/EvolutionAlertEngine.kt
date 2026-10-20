package com.digiwear.core.engine

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.PetState
import com.digiwear.core.model.SpriteFrame
import com.digiwear.core.render.SpriteMatrixRegistry
import com.digiwear.core.render.SpritePalette

/**
 * Core engine governing background digital monster evolution alerts,
 * notification payloads, idempotent alert suppression, and sprite validation.
 */
object EvolutionAlertEngine {

    const val CHANNEL_ID_EVOLUTION = "pet_evolution_channel"
    const val CHANNEL_NAME_EVOLUTION = "Digital Evolution Alerts"
    const val CHANNEL_DESCRIPTION_EVOLUTION = "Alerts when your virtual pet partner undergoes digital monster evolution"
    const val NOTIFICATION_ID_EVOLUTION = 1002
    const val KEY_LAST_NOTIFIED_SPECIES_ID = "last_notified_species_id"
    const val NOTIFICATION_TITLE = "DIGITAL EVOLUTION!"

    data class EvolutionAlertPayload(
        val initialSpeciesId: Int,
        val finalSpeciesId: Int,
        val speciesName: String,
        val stage: PetStage,
        val stageDisplayName: String,
        val notificationTitle: String,
        val contentText: String,
        val summaryText: String,
        val matrix: Array<String>,
        val palette: SpritePalette
    )

    /**
     * Evaluates whether an evolution occurred between [initialState] and [finalState].
     * Returns null if no evolution occurred, pet was frozen, pet is deceased,
     * or species ID is invalid.
     */
    fun evaluateEvolutionAlert(
        initialState: PetState,
        finalState: PetState
    ): EvolutionAlertPayload? {
        val initialId = initialState.speciesId
        val finalId = finalState.speciesId

        if (initialId <= 0 || finalId <= 0) return null
        if (initialId == finalId) return null
        if (initialState.isFrozen || finalState.isFrozen) return null
        if (initialState.isDeceased || finalState.isDeceased) return null

        val newSpecies = PetSpecies.getById(finalId)
        val matrix = SpriteMatrixRegistry.getMatrix(finalId, SpriteFrame.IDLE_A)
        val palette = SpriteMatrixRegistry.getPalette(finalId)

        return EvolutionAlertPayload(
            initialSpeciesId = initialId,
            finalSpeciesId = finalId,
            speciesName = newSpecies.name,
            stage = newSpecies.stage,
            stageDisplayName = newSpecies.stage.displayName,
            notificationTitle = NOTIFICATION_TITLE,
            contentText = "${newSpecies.name} (${newSpecies.stage.displayName})",
            summaryText = "${newSpecies.name} • ${newSpecies.stage.displayName}",
            matrix = matrix,
            palette = palette
        )
    }

    /**
     * Determines whether an evolution notification should be posted, ensuring
     * idempotent suppression when the species has already been notified.
     */
    fun shouldNotifyEvolution(
        lastNotifiedSpeciesId: Int,
        initialSpeciesId: Int,
        finalSpeciesId: Int
    ): Boolean {
        if (initialSpeciesId <= 0 || finalSpeciesId <= 0) return false
        if (initialSpeciesId == finalSpeciesId) return false
        return lastNotifiedSpeciesId != finalSpeciesId
    }

    /**
     * Helper to simulate physiological decay and evolution transition from [initialState] to [currentUtc].
     */
    fun processDecayAndEvaluateAlert(
        initialState: PetState,
        currentUtc: Long,
        isCanonicalLifespanEnabled: Boolean = false
    ): Pair<PetState, EvolutionAlertPayload?> {
        val updated = PetSimulationEngine.processDelta(
            currentState = initialState,
            currentTimestampUtc = currentUtc,
            isCanonicalLifespanEnabled = isCanonicalLifespanEnabled
        )

        val evolvedSpecies = if (!updated.isDeceased && !updated.isFrozen) {
            EvolutionEngine.evaluateEvolution(updated)
        } else {
            null
        }

        val finalState = if (evolvedSpecies != null) {
            EvolutionEngine.evolve(updated, evolvedSpecies, currentUtc)
        } else {
            updated
        }

        val alert = evaluateEvolutionAlert(initialState, finalState)
        return Pair(finalState, alert)
    }
}
