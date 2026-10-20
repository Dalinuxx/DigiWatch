package com.digiwear.companion.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "synced_pet_history")
data class SyncedPetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val speciesId: Int,
    val speciesName: String,
    val currentHp: Int,
    val maxHp: Int,
    val hunger: Int,
    val strength: Int,
    val weight: Int,
    val ageSeconds: Long,
    val careMistakes: Int,
    val effort: Int,
    val overfeedCount: Int,
    val totalBattles: Int,
    val victories: Int,
    val generation: Int = 1,
    val lifespanDays: Int = 14,
    val isDeceased: Boolean = false,
    val traitedEggBonus: Int = 0,
    val vitalPoints: Int = 0,
    val syncedTimestampUtc: Long = System.currentTimeMillis() / 1000L
)

@Serializable
@Entity(tableName = "companion_memorial_graveyard")
data class CompanionMemorialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val speciesId: Int,
    val speciesName: String,
    val customNickname: String = "",
    val generation: Int = 1,
    val finalStage: String = "",
    val ageDays: Int = 0,
    val battlesWon: Int = 0,
    val battlesTotal: Int = 0,
    val causeOfDeath: String = "OLD_AGE",
    val epitaph: String = "",
    val timestampUtc: Long = System.currentTimeMillis() / 1000L,
    val traitedEggBonusGiven: Int = 1
) {
    val winRatio: Float
        get() = if (battlesTotal > 0) battlesWon.toFloat() / battlesTotal else 0f

    fun toDomain(): com.digiwear.core.model.MemorialRecord = com.digiwear.core.model.MemorialRecord(
        id = id,
        speciesId = speciesId,
        speciesName = speciesName,
        customNickname = customNickname,
        generation = generation,
        finalStage = finalStage,
        ageDays = ageDays,
        battlesWon = battlesWon,
        battlesTotal = battlesTotal,
        causeOfDeath = causeOfDeath,
        epitaph = epitaph,
        timestampUtc = timestampUtc,
        traitedEggBonusGiven = traitedEggBonusGiven
    )

    companion object {
        fun fromDomain(domain: com.digiwear.core.model.MemorialRecord): CompanionMemorialEntity = CompanionMemorialEntity(
            id = domain.id,
            speciesId = domain.speciesId,
            speciesName = domain.speciesName,
            customNickname = domain.customNickname,
            generation = domain.generation,
            finalStage = domain.finalStage,
            ageDays = domain.ageDays,
            battlesWon = domain.battlesWon,
            battlesTotal = domain.battlesTotal,
            causeOfDeath = domain.causeOfDeath,
            epitaph = domain.epitaph,
            timestampUtc = domain.timestampUtc,
            traitedEggBonusGiven = domain.traitedEggBonusGiven
        )
    }
}

@Serializable
@Entity(tableName = "custom_campaigns")
data class CustomCampaignEntity(
    @PrimaryKey
    val campaignId: String,
    val title: String,
    val jsonContent: String,
    val updatedAtUtc: Long = System.currentTimeMillis() / 1000L
)

@Serializable
@Entity(tableName = "digi_vault")
data class StoredDigimonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val speciesId: Int,
    val speciesName: String,
    val customNickname: String = "",
    val stageName: String,
    val attributeName: String,
    val currentHp: Int,
    val maxHp: Int,
    val hunger: Int,
    val strength: Int,
    val weight: Int,
    val ageSeconds: Long,
    val careMistakes: Int,
    val effort: Int,
    val overfeedCount: Int,
    val totalBattles: Int,
    val victories: Int,
    val storedTimestampUtc: Long = System.currentTimeMillis() / 1000L,
    val notes: String = ""
)

