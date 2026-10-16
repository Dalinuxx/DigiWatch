package com.digiwatch.core.model

import kotlinx.serialization.Serializable

/**
 * Combatant representation for deterministic offline battle simulation.
 */
@Serializable
data class BattleFighter(
    val id: String,
    val speciesId: Int,
    val name: String,
    val stage: PetStage,
    val attribute: ElementalAttribute,
    val maxHp: Int,
    val currentHp: Int,
    val basePower: Int,
    val victories: Int,
    val effort: Int,
    val battleSeed: Int
)

/**
 * Individual turn outcome within a combat round.
 */
@Serializable
data class BattleTurn(
    val round: Int,
    val attackerIndex: Int,
    val attackerName: String,
    val defenderIndex: Int,
    val defenderName: String,
    val damage: Int,
    val defenderRemainingHp: Int,
    val isAdvantage: Boolean,
    val isCritical: Boolean
)

/**
 * Complete outcome of a deterministic 10-round combat simulation.
 */
@Serializable
data class BattleResult(
    val fighterA: BattleFighter,
    val fighterB: BattleFighter,
    val turns: List<BattleTurn>,
    val totalRounds: Int,
    val winnerIndex: Int, // 0 = Fighter A, 1 = Fighter B, -1 = Draw
    val fighterAFinalHp: Int,
    val fighterBFinalHp: Int
) {
    val isDraw: Boolean
        get() = winnerIndex == -1

    val winnerName: String?
        get() = when (winnerIndex) {
            0 -> fighterA.name
            1 -> fighterB.name
            else -> null
        }
}
