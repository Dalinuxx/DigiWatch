package com.digiwear.wear.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.digiwear.core.model.PetState

@Entity(tableName = "pet_state")
data class PetEntity(
    @PrimaryKey
    val id: Int = 1,
    val speciesId: Int = 101, // Starts as Botamon (Baby I)
    val currentHp: Int = 50,
    val maxHp: Int = 50,
    val hunger: Int = 4,
    val strength: Int = 4,
    val weight: Int = 5,
    val ageSeconds: Long = 0L,
    val careMistakes: Int = 0,
    val effort: Int = 0,
    val overfeedCount: Int = 0,
    val totalBattles: Int = 0,
    val victories: Int = 0,
    val wasteCount: Int = 0,
    val isSleeping: Boolean = false,
    val lightsOff: Boolean = false,
    val isFrozen: Boolean = false,
    val isInjured: Boolean = false,
    val lastProcessedTimestampUtc: Long = System.currentTimeMillis() / 1000L,
    val attentionCallActive: Boolean = false,
    val attentionCallTimestampUtc: Long? = null,
    val hungerAccumulatedSeconds: Long = 0L,
    val strengthAccumulatedSeconds: Long = 0L,
    val wasteAccumulatedSeconds: Long = 0L,
    val generation: Int = 1,
    val lifespanDays: Int = 14,
    val isDeceased: Boolean = false,
    val traitedEggBonus: Int = 0,
    val vitalPoints: Int = 0
) {
    fun toDomain(): PetState = PetState(
        speciesId = speciesId,
        currentHp = currentHp,
        maxHp = maxHp,
        hunger = hunger,
        strength = strength,
        weight = weight,
        ageSeconds = ageSeconds,
        careMistakes = careMistakes,
        effort = effort,
        overfeedCount = overfeedCount,
        totalBattles = totalBattles,
        victories = victories,
        wasteCount = wasteCount,
        isSleeping = isSleeping,
        lightsOff = lightsOff,
        isFrozen = isFrozen,
        isInjured = isInjured,
        lastProcessedTimestampUtc = lastProcessedTimestampUtc,
        attentionCallActive = attentionCallActive,
        attentionCallTimestampUtc = attentionCallTimestampUtc,
        hungerAccumulatedSeconds = hungerAccumulatedSeconds,
        strengthAccumulatedSeconds = strengthAccumulatedSeconds,
        wasteAccumulatedSeconds = wasteAccumulatedSeconds,
        generation = generation,
        lifespanDays = lifespanDays,
        isDeceased = isDeceased,
        traitedEggBonus = traitedEggBonus,
        vitalPoints = vitalPoints
    )

    companion object {
        fun fromDomain(domain: PetState): PetEntity = PetEntity(
            id = 1,
            speciesId = domain.speciesId,
            currentHp = domain.currentHp,
            maxHp = domain.maxHp,
            hunger = domain.hunger,
            strength = domain.strength,
            weight = domain.weight,
            ageSeconds = domain.ageSeconds,
            careMistakes = domain.careMistakes,
            effort = domain.effort,
            overfeedCount = domain.overfeedCount,
            totalBattles = domain.totalBattles,
            victories = domain.victories,
            wasteCount = domain.wasteCount,
            isSleeping = domain.isSleeping,
            lightsOff = domain.lightsOff,
            isFrozen = domain.isFrozen,
            isInjured = domain.isInjured,
            lastProcessedTimestampUtc = domain.lastProcessedTimestampUtc,
            attentionCallActive = domain.attentionCallActive,
            attentionCallTimestampUtc = domain.attentionCallTimestampUtc,
            hungerAccumulatedSeconds = domain.hungerAccumulatedSeconds,
            strengthAccumulatedSeconds = domain.strengthAccumulatedSeconds,
            wasteAccumulatedSeconds = domain.wasteAccumulatedSeconds,
            generation = domain.generation,
            lifespanDays = domain.lifespanDays,
            isDeceased = domain.isDeceased,
            traitedEggBonus = domain.traitedEggBonus,
            vitalPoints = domain.vitalPoints
        )
    }
}

@Entity(tableName = "adventure_state")
data class AdventureEntity(
    @PrimaryKey
    val id: Int = 1,
    val campaignId: String = "file_island_corrupted_sector",
    val currentNodeIndex: Int = 0,
    val accumulatedStepsCurrentNode: Int = 0,
    val totalAdventureSteps: Int = 0,
    val clearedFlagsCsv: String = "",
    val isCompleted: Boolean = false,
    val unlockedEggsCsv: String = "",
    val collectedItemsCsv: String = "",
    val unlockedCampaignsCsv: String = ""
)
