package com.digiwear.core.model

import kotlinx.serialization.Serializable

/**
 * Memorial Graveyard record commemorating a departed Digimon partner across generations.
 * Displays the classic Bandai 1997 pixel memorial headstone, victory stats, and generational lineage.
 */
@Serializable
data class MemorialRecord(
    val id: Long = 0,
    val speciesId: Int,
    val speciesName: String,
    val customNickname: String = "",
    val generation: Int = 1,
    val finalStage: String = "",
    val ageDays: Int = 0,
    val battlesWon: Int = 0,
    val battlesTotal: Int = 0,
    val causeOfDeath: String = "OLD_AGE", // "OLD_AGE", "NATURAL_SUCCESSION", "HONORABLE_RETIREMENT"
    val epitaph: String = "Forever encoded in the Digital World.",
    val timestampUtc: Long = System.currentTimeMillis() / 1000L,
    val traitedEggBonusGiven: Int = 1
) {
    val winRatio: Float
        get() = if (battlesTotal > 0) battlesWon.toFloat() / battlesTotal else 0f
}
