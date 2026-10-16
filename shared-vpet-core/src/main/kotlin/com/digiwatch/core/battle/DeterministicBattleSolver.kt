package com.digiwatch.core.battle

import com.digiwatch.core.model.BattleFighter
import com.digiwatch.core.model.BattleResult
import com.digiwatch.core.model.BattleTurn
import java.util.Random
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Deterministic turn-based combat solver.
 * Produces identical round-by-round combat logs on disconnected devices without network communication.
 */
object DeterministicBattleSolver {

    private const val MAX_ROUNDS = 10

    fun resolveBattle(fighterA: BattleFighter, fighterB: BattleFighter): BattleResult {
        // Build symmetric combined seed so resolution order is identical regardless of which device runs solver
        val seed1 = fighterA.battleSeed and 0xFFFF
        val seed2 = fighterB.battleSeed and 0xFFFF
        val minSeed = min(seed1, seed2).toLong()
        val maxSeed = max(seed1, seed2).toLong()
        val unifiedSeed = (minSeed shl 16 or maxSeed) xor 0x5EEDCAFEL

        val prng = Random(unifiedSeed)

        var hpA = fighterA.currentHp
        var hpB = fighterB.currentHp
        val turns = mutableListOf<BattleTurn>()

        for (round in 1..MAX_ROUNDS) {
            // Determine turn order for this round
            val initiativeRoll = prng.nextInt(100)
            val aFirst = if (initiativeRoll < 50) true else false

            val (firstFighter, firstIdx, firstHp, secondFighter, secondIdx, secondHp) = if (aFirst) {
                Order(fighterA, 0, hpA, fighterB, 1, hpB)
            } else {
                Order(fighterB, 1, hpB, fighterA, 0, hpA)
            }

            // First attacker strikes
            val multFirst = firstFighter.attribute.damageMultiplierAgainst(secondFighter.attribute)
            val isAdvFirst = multFirst > 1.0f
            val isCritFirst = prng.nextInt(100) < 15
            val varianceFirst = 0.9f + (prng.nextInt(21) / 100f) // 0.90 to 1.10

            var dmgFirst = (firstFighter.basePower * 0.35f + firstFighter.effort * 2f) * multFirst * varianceFirst
            if (isCritFirst) dmgFirst *= 1.5f
            val finalDmgFirst = max(5, dmgFirst.roundToInt())

            val remainingSecondHp = max(0, secondHp - finalDmgFirst)
            turns.add(
                BattleTurn(
                    round = round,
                    attackerIndex = firstIdx,
                    attackerName = firstFighter.name,
                    defenderIndex = secondIdx,
                    defenderName = secondFighter.name,
                    damage = finalDmgFirst,
                    defenderRemainingHp = remainingSecondHp,
                    isAdvantage = isAdvFirst,
                    isCritical = isCritFirst
                )
            )

            if (firstIdx == 0) {
                hpB = remainingSecondHp
            } else {
                hpA = remainingSecondHp
            }

            if (remainingSecondHp <= 0) {
                // Combat ended
                break
            }

            // Second attacker retaliates
            val multSecond = secondFighter.attribute.damageMultiplierAgainst(firstFighter.attribute)
            val isAdvSecond = multSecond > 1.0f
            val isCritSecond = prng.nextInt(100) < 15
            val varianceSecond = 0.9f + (prng.nextInt(21) / 100f)

            var dmgSecond = (secondFighter.basePower * 0.35f + secondFighter.effort * 2f) * multSecond * varianceSecond
            if (isCritSecond) dmgSecond *= 1.5f
            val finalDmgSecond = max(5, dmgSecond.roundToInt())

            val currentFirstHp = if (firstIdx == 0) hpA else hpB
            val remainingFirstHp = max(0, currentFirstHp - finalDmgSecond)

            turns.add(
                BattleTurn(
                    round = round,
                    attackerIndex = secondIdx,
                    attackerName = secondFighter.name,
                    defenderIndex = firstIdx,
                    defenderName = firstFighter.name,
                    damage = finalDmgSecond,
                    defenderRemainingHp = remainingFirstHp,
                    isAdvantage = isAdvSecond,
                    isCritical = isCritSecond
                )
            )

            if (firstIdx == 0) {
                hpA = remainingFirstHp
            } else {
                hpB = remainingFirstHp
            }

            if (remainingFirstHp <= 0) {
                break
            }
        }

        val winnerIndex = when {
            hpA <= 0 && hpB > 0 -> 1
            hpB <= 0 && hpA > 0 -> 0
            hpA <= 0 && hpB <= 0 -> -1
            else -> {
                // Higher HP ratio wins if both survive 10 rounds
                val ratioA = hpA.toFloat() / fighterA.maxHp
                val ratioB = hpB.toFloat() / fighterB.maxHp
                when {
                    ratioA > ratioB -> 0
                    ratioB > ratioA -> 1
                    else -> -1
                }
            }
        }

        return BattleResult(
            fighterA = fighterA,
            fighterB = fighterB,
            turns = turns,
            totalRounds = turns.lastOrNull()?.round ?: 0,
            winnerIndex = winnerIndex,
            fighterAFinalHp = hpA,
            fighterBFinalHp = hpB
        )
    }

    private data class Order(
        val first: BattleFighter,
        val firstIdx: Int,
        val firstHp: Int,
        val second: BattleFighter,
        val secondIdx: Int,
        val secondHp: Int
    )
}
