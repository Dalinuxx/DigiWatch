package com.digiwear.wear.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.digiwear.core.model.MemorialRecord

@Entity(tableName = "memorial_graveyard")
data class MemorialEntity(
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
    fun toDomain(): MemorialRecord = MemorialRecord(
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
        fun fromDomain(domain: MemorialRecord): MemorialEntity = MemorialEntity(
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
