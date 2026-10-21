package com.digiwear.companion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistent record of a discovered digital monster species in the 550-species DigiDex.
 * Records when the species was first registered, how many times it was raised, and how many times defeated.
 */
@Entity(tableName = "discovered_species")
data class DiscoveredSpeciesEntity(
    @PrimaryKey
    val speciesId: Int,
    val discoveredTimestampUtc: Long = System.currentTimeMillis() / 1000L,
    val timesRaised: Int = 0,
    val timesDefeated: Int = 0
)
