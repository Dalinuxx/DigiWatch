package com.digiwatch.companion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val syncedTimestampUtc: Long = System.currentTimeMillis() / 1000L
)

@Entity(tableName = "custom_campaigns")
data class CustomCampaignEntity(
    @PrimaryKey
    val campaignId: String,
    val title: String,
    val jsonContent: String,
    val updatedAtUtc: Long = System.currentTimeMillis() / 1000L
)

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

