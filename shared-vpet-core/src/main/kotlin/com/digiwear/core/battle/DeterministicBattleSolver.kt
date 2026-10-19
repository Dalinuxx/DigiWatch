package com.digiwear.core.battle

import com.digiwear.core.model.BattleFighter
import com.digiwear.core.model.BattleMode
import com.digiwear.core.model.BattleResult
import com.digiwear.core.model.BattleTurn
import com.digiwear.core.model.ElementalAttribute
import java.util.Random
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Deterministic turn-based combat solver implementing canonical Bandai V-Pet / Pendulum battle formulas.
 * Produces identical round-by-round combat logs on disconnected devices without network communication.
 */
object DeterministicBattleSolver {

    private const val MAX_ROUNDS = 10

    /**
     * Computes the canonical V-Pet Effective Power rating for a [BattleFighter]:
     * Base Power + Effort bonus (+6 per heart) + Strength bonus (+3 per heart above 2)
     * + Weight bonus (up to +10) + Veteran Victory bonus (up to +12).
     */
    fun computeEffectivePower(fighter: BattleFighter): Int {
        val effortBonus = fighter.effort.coerceIn(0, 4) * 6
        val strengthBonus = (fighter.strength.coerceIn(0, 4) - 2) * 3
        val weightBonus = ((fighter.weight - 15).coerceAtLeast(0) / 5).coerceAtMost(10)
        val veteranBonus = (fighter.victories / 8).coerceAtMost(12)
        return max(10, fighter.basePower + effortBonus + strengthBonus + weightBonus + veteranBonus)
    }

    /**
     * Computes the canonical V-Pet Hit Rate percentage (25%..95%) of [attacker] against [defender].
     */
    fun computeHitRatePercent(
        attacker: BattleFighter,
        defender: BattleFighter,
        critBoost: Boolean = false
    ): Int {
        val effA = computeEffectivePower(attacker).toFloat()
        val effB = computeEffectivePower(defender).toFloat()
        val attrMult = attacker.attribute.damageMultiplierAgainst(defender.attribute)
        val weightedA = effA * attrMult
        val baseRate = ((weightedA / (weightedA + effB)) * 100f).roundToInt()
        val boostBonus = if (critBoost) 10 else 0
        return (baseRate + boostBonus).coerceIn(25, 95)
    }

    fun resolveBattle(
        fighterA: BattleFighter,
        fighterB: BattleFighter,
        attackerCritBoost: Boolean = false,
        environmentEffect: String = "none",
        battleMode: BattleMode = BattleMode.NFC_TAP_DUEL
    ): BattleResult {
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
        val env = environmentEffect.lowercase().trim()

        val effPowerA = computeEffectivePower(fighterA)
        val effPowerB = computeEffectivePower(fighterB)
        val attrMultA = fighterA.attribute.damageMultiplierAgainst(fighterB.attribute)
        val attrMultB = fighterB.attribute.damageMultiplierAgainst(fighterA.attribute)
        val hitRateA = computeHitRatePercent(fighterA, fighterB, attackerCritBoost)
        val hitRateB = computeHitRatePercent(fighterB, fighterA, false)

        for (round in 1..MAX_ROUNDS) {
            // Determine turn order for this round
            val initiativeRoll = prng.nextInt(100)
            val aFirst = initiativeRoll < 50

            val (firstFighter, firstIdx, firstHp, secondFighter, secondIdx, secondHp) = if (aFirst) {
                Order(fighterA, 0, hpA, fighterB, 1, hpB)
            } else {
                Order(fighterB, 1, hpB, fighterA, 0, hpA)
            }

            // First attacker strikes
            val multFirst = firstFighter.attribute.damageMultiplierAgainst(secondFighter.attribute)
            val envMultFirst = computeEnvironmentMultiplier(firstFighter, firstIdx, env)
            val isAdvFirst = multFirst > 1.0f || envMultFirst > 1.0f
            val critThresholdFirst = if (env == "glitch_fog") 25 else 15
            val isNaturalCritFirst = prng.nextInt(100) < critThresholdFirst
            val isBoostedFirst = attackerCritBoost && firstIdx == 0
            val isCritFirst = isNaturalCritFirst || isBoostedFirst
            val varianceFirst = 0.9f + (prng.nextInt(21) / 100f) // 0.90 to 1.10

            var dmgFirst = (firstFighter.basePower * 0.35f + firstFighter.effort * 2f) * multFirst * envMultFirst * varianceFirst
            if (isNaturalCritFirst) dmgFirst *= 1.5f
            if (isBoostedFirst) dmgFirst *= 1.25f
            val finalDmgFirst = max(5, dmgFirst.roundToInt())
            val attackTypeFirst = when {
                isCritFirst -> "MEGA_HIT"
                varianceFirst >= 1.02f || firstFighter.effort >= 3 -> "DOUBLE"
                else -> "SINGLE"
            }

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
                    isCritical = isCritFirst,
                    attackType = attackTypeFirst,
                    hitRatePercent = if (firstIdx == 0) hitRateA else hitRateB,
                    effectivePower = if (firstIdx == 0) effPowerA else effPowerB
                )
            )

            if (firstIdx == 0) {
                hpB = remainingSecondHp
            } else {
                hpA = remainingSecondHp
            }

            if (remainingSecondHp <= 0) {
                break
            }

            // Second attacker retaliates
            val multSecond = secondFighter.attribute.damageMultiplierAgainst(firstFighter.attribute)
            val envMultSecond = computeEnvironmentMultiplier(secondFighter, secondIdx, env)
            val isAdvSecond = multSecond > 1.0f || envMultSecond > 1.0f
            val critThresholdSecond = if (env == "glitch_fog") 25 else 15
            val isNaturalCritSecond = prng.nextInt(100) < critThresholdSecond
            val isBoostedSecond = attackerCritBoost && secondIdx == 0
            val isCritSecond = isNaturalCritSecond || isBoostedSecond
            val varianceSecond = 0.9f + (prng.nextInt(21) / 100f)

            var dmgSecond = (secondFighter.basePower * 0.35f + secondFighter.effort * 2f) * multSecond * envMultSecond * varianceSecond
            if (isNaturalCritSecond) dmgSecond *= 1.5f
            if (isBoostedSecond) dmgSecond *= 1.25f
            val finalDmgSecond = max(5, dmgSecond.roundToInt())
            val attackTypeSecond = when {
                isCritSecond -> "MEGA_HIT"
                varianceSecond >= 1.02f || secondFighter.effort >= 3 -> "DOUBLE"
                else -> "SINGLE"
            }

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
                    isCritical = isCritSecond,
                    attackType = attackTypeSecond,
                    hitRatePercent = if (secondIdx == 0) hitRateA else hitRateB,
                    effectivePower = if (secondIdx == 0) effPowerA else effPowerB
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
                val ratioA = hpA.toFloat() / fighterA.maxHp
                val ratioB = hpB.toFloat() / fighterB.maxHp
                when {
                    ratioA > ratioB -> 0
                    ratioB > ratioA -> 1
                    else -> -1
                }
            }
        }

        val attrDesc = when {
            attrMultA > 1.0f -> "${fighterA.attribute.displayName} > ${fighterB.attribute.displayName} (1.20x)"
            attrMultB > 1.0f -> "${fighterB.attribute.displayName} > ${fighterA.attribute.displayName} (0.83x)"
            else -> "${fighterA.attribute.displayName} = ${fighterB.attribute.displayName} (1.00x)"
        }
        val summary = "EFF PWR $effPowerA vs $effPowerB • $attrDesc • HIT $hitRateA%"

        return BattleResult(
            fighterA = fighterA,
            fighterB = fighterB,
            turns = turns,
            totalRounds = turns.lastOrNull()?.round ?: 0,
            winnerIndex = winnerIndex,
            fighterAFinalHp = hpA,
            fighterBFinalHp = hpB,
            battleMode = battleMode,
            fighterAEffectivePower = effPowerA,
            fighterBEffectivePower = effPowerB,
            attributeMultiplierA = attrMultA,
            attributeMultiplierB = attrMultB,
            formulaSummary = summary
        )
    }

    /**
     * Computes dynamic environmental weather hazard combat multipliers:
     * - glitch_fog: Virus-type combatants gain +15% power; crit rate rises to 25%.
     * - subzero_frost: Data-type combatants gain +15% power; non-Data suffer -10% frost chill.
     * - pyroclastic_flow / heat_wave: Vaccine-type combatants gain +15% power; volcanic heat adds +10% damage.
     * - cosmic_nihilism / quantum_erasure / dark_gravity: Apex boss aura (+10% for defender/boss), or +15% heroic surge if challenger has 4 Effort hearts.
     */
    fun computeEnvironmentMultiplier(fighter: BattleFighter, fighterIdx: Int, env: String): Float {
        return when (env) {
            "glitch_fog" -> if (fighter.attribute == ElementalAttribute.VIRUS) 1.15f else 1.0f
            "subzero_frost" -> if (fighter.attribute == ElementalAttribute.DATA) 1.15f else 0.90f
            "pyroclastic_flow", "heat_wave" -> if (fighter.attribute == ElementalAttribute.VACCINE) 1.15f else 1.10f
            "cosmic_nihilism", "quantum_erasure", "dark_gravity" -> {
                if (fighterIdx == 0 && fighter.effort >= 4) 1.15f
                else if (fighterIdx == 1) 1.10f
                else 1.0f
            }
            else -> 1.0f
        }
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
