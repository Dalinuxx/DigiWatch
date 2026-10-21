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

        // Canonicalize fighter order so resolution order and initiative are 100% symmetric
        // regardless of which peer device passes its local pet as fighterA vs fighterB.
        val aIsPrimary = when {
            seed1 != seed2 -> seed1 < seed2
            fighterA.speciesId != fighterB.speciesId -> fighterA.speciesId < fighterB.speciesId
            else -> fighterA.id <= fighterB.id
        }

        val primaryFighter = if (aIsPrimary) fighterA else fighterB
        val secondaryFighter = if (aIsPrimary) fighterB else fighterA
        val primaryCritBoost = if (aIsPrimary) attackerCritBoost else false
        val secondaryCritBoost = if (!aIsPrimary) attackerCritBoost else false

        var canonHp0 = primaryFighter.currentHp
        var canonHp1 = secondaryFighter.currentHp
        val turns = mutableListOf<BattleTurn>()
        val env = environmentEffect.lowercase().trim().replace(Regex("[\\s-]+"), "_")

        val effPower0 = computeEffectivePower(primaryFighter)
        val effPower1 = computeEffectivePower(secondaryFighter)
        val attrMult0 = primaryFighter.attribute.damageMultiplierAgainst(secondaryFighter.attribute)
        val attrMult1 = secondaryFighter.attribute.damageMultiplierAgainst(primaryFighter.attribute)
        val hitRate0 = computeHitRatePercent(primaryFighter, secondaryFighter, primaryCritBoost)
        val hitRate1 = computeHitRatePercent(secondaryFighter, primaryFighter, secondaryCritBoost)

        val effPowerA = if (aIsPrimary) effPower0 else effPower1
        val effPowerB = if (aIsPrimary) effPower1 else effPower0
        val attrMultA = if (aIsPrimary) attrMult0 else attrMult1
        val attrMultB = if (aIsPrimary) attrMult1 else attrMult0
        val hitRateA = if (aIsPrimary) hitRate0 else hitRate1
        val hitRateB = if (aIsPrimary) hitRate1 else hitRate0

        for (round in 1..MAX_ROUNDS) {
            // Determine turn order for this round
            val initiativeRoll = prng.nextInt(100)
            val primaryFirst = initiativeRoll < 50

            val (firstFighter, firstIdx, firstHp, secondFighter, secondIdx, secondHp) = if (primaryFirst) {
                Order(primaryFighter, 0, canonHp0, secondaryFighter, 1, canonHp1)
            } else {
                Order(secondaryFighter, 1, canonHp1, primaryFighter, 0, canonHp0)
            }

            // First attacker strikes
            val multFirst = firstFighter.attribute.damageMultiplierAgainst(secondFighter.attribute)
            val envMultFirst = computeEnvironmentMultiplier(firstFighter, firstIdx, env)
            val isAdvFirst = multFirst > 1.0f || envMultFirst > 1.0f
            val critThresholdFirst = if (env == "glitch_fog" || env == "x_program_fog") 25 else 15
            val isNaturalCritFirst = prng.nextInt(100) < critThresholdFirst
            val isBoostedFirst = if (firstIdx == 0) primaryCritBoost else secondaryCritBoost
            val isCritFirst = isNaturalCritFirst || isBoostedFirst
            val varianceFirst = 0.9f + (prng.nextInt(21) / 100f) // 0.90 to 1.10

            val effFirst = if (firstIdx == 0) effPower0 else effPower1
            var dmgFirst = (effFirst * 0.35f) * multFirst * envMultFirst * varianceFirst
            if (isNaturalCritFirst) dmgFirst *= 1.5f
            if (isBoostedFirst) dmgFirst *= 1.25f
            val finalDmgFirst = max(5, dmgFirst.roundToInt())
            val attackTypeFirst = when {
                isCritFirst -> "MEGA_HIT"
                varianceFirst >= 1.02f || firstFighter.effort >= 3 -> "DOUBLE"
                else -> "SINGLE"
            }

            val remainingSecondHp = max(0, secondHp - finalDmgFirst)
            val moveNameFirst = SignatureAttackRegistry.getAttackName(
                speciesId = firstFighter.speciesId,
                isCritical = isCritFirst,
                attackType = attackTypeFirst
            )

            val callerAttackerIdxFirst = if (aIsPrimary) firstIdx else (1 - firstIdx)
            val callerDefenderIdxFirst = if (aIsPrimary) secondIdx else (1 - secondIdx)

            turns.add(
                BattleTurn(
                    round = round,
                    attackerIndex = callerAttackerIdxFirst,
                    attackerName = firstFighter.name,
                    defenderIndex = callerDefenderIdxFirst,
                    defenderName = secondFighter.name,
                    damage = finalDmgFirst,
                    defenderRemainingHp = remainingSecondHp,
                    isAdvantage = isAdvFirst,
                    isCritical = isCritFirst,
                    attackType = attackTypeFirst,
                    hitRatePercent = if (firstIdx == 0) hitRate0 else hitRate1,
                    effectivePower = if (firstIdx == 0) effPower0 else effPower1,
                    moveName = moveNameFirst
                )
            )

            if (firstIdx == 0) {
                canonHp1 = remainingSecondHp
            } else {
                canonHp0 = remainingSecondHp
            }

            if (remainingSecondHp <= 0) {
                break
            }

            // Second attacker retaliates
            val multSecond = secondFighter.attribute.damageMultiplierAgainst(firstFighter.attribute)
            val envMultSecond = computeEnvironmentMultiplier(secondFighter, secondIdx, env)
            val isAdvSecond = multSecond > 1.0f || envMultSecond > 1.0f
            val critThresholdSecond = if (env == "glitch_fog" || env == "x_program_fog") 25 else 15
            val isNaturalCritSecond = prng.nextInt(100) < critThresholdSecond
            val isBoostedSecond = if (secondIdx == 0) primaryCritBoost else secondaryCritBoost
            val isCritSecond = isNaturalCritSecond || isBoostedSecond
            val varianceSecond = 0.9f + (prng.nextInt(21) / 100f)

            val effSecond = if (secondIdx == 0) effPower0 else effPower1
            var dmgSecond = (effSecond * 0.35f) * multSecond * envMultSecond * varianceSecond
            if (isNaturalCritSecond) dmgSecond *= 1.5f
            if (isBoostedSecond) dmgSecond *= 1.25f
            val finalDmgSecond = max(5, dmgSecond.roundToInt())
            val attackTypeSecond = when {
                isCritSecond -> "MEGA_HIT"
                varianceSecond >= 1.02f || secondFighter.effort >= 3 -> "DOUBLE"
                else -> "SINGLE"
            }

            val currentFirstHp = if (firstIdx == 0) canonHp0 else canonHp1
            val remainingFirstHp = max(0, currentFirstHp - finalDmgSecond)

            val moveNameSecond = SignatureAttackRegistry.getAttackName(
                speciesId = secondFighter.speciesId,
                isCritical = isCritSecond,
                attackType = attackTypeSecond
            )

            val callerAttackerIdxSecond = if (aIsPrimary) secondIdx else (1 - secondIdx)
            val callerDefenderIdxSecond = if (aIsPrimary) firstIdx else (1 - firstIdx)

            turns.add(
                BattleTurn(
                    round = round,
                    attackerIndex = callerAttackerIdxSecond,
                    attackerName = secondFighter.name,
                    defenderIndex = callerDefenderIdxSecond,
                    defenderName = firstFighter.name,
                    damage = finalDmgSecond,
                    defenderRemainingHp = remainingFirstHp,
                    isAdvantage = isAdvSecond,
                    isCritical = isCritSecond,
                    attackType = attackTypeSecond,
                    hitRatePercent = if (secondIdx == 0) hitRate0 else hitRate1,
                    effectivePower = if (secondIdx == 0) effPower0 else effPower1,
                    moveName = moveNameSecond
                )
            )

            if (firstIdx == 0) {
                canonHp0 = remainingFirstHp
            } else {
                canonHp1 = remainingFirstHp
            }

            if (remainingFirstHp <= 0) {
                break
            }
        }

        val hpA = if (aIsPrimary) canonHp0 else canonHp1
        val hpB = if (aIsPrimary) canonHp1 else canonHp0

        val canonWinnerIndex = when {
            canonHp0 <= 0 && canonHp1 > 0 -> 1
            canonHp1 <= 0 && canonHp0 > 0 -> 0
            canonHp0 <= 0 && canonHp1 <= 0 -> -1
            else -> {
                val ratio0 = canonHp0.toFloat() / primaryFighter.maxHp
                val ratio1 = canonHp1.toFloat() / secondaryFighter.maxHp
                when {
                    ratio0 > ratio1 -> 0
                    ratio1 > ratio0 -> 1
                    else -> -1
                }
            }
        }

        val winnerIndex = when (canonWinnerIndex) {
            0 -> if (aIsPrimary) 0 else 1
            1 -> if (aIsPrimary) 1 else 0
            else -> -1
        }

        val attrDesc = when {
            attrMultA > 1.0f -> "${fighterA.attribute.displayName} > ${fighterB.attribute.displayName} (1.20x)"
            attrMultB > 1.0f -> "${fighterB.attribute.displayName} > ${fighterA.attribute.displayName} (1.20x)"
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
        return when (env.lowercase().trim().replace(Regex("[\\s-]+"), "_")) {
            "glitch_fog", "x_program_fog", "x_virus_rot", "dark_miasma", "miasma_fog" -> if (fighter.attribute == ElementalAttribute.VIRUS) 1.15f else 1.0f
            "subzero_frost", "cocytus_blizzard", "absolute_aurora", "fenrir_frost", "tidal_current", "data_storm" -> if (fighter.attribute == ElementalAttribute.DATA) 1.15f else 0.90f
            "pyroclastic_flow", "heat_wave", "magma_heat", "ancient_inferno", "hellfire_storm", "solar_flare", "gehenna_flames", "thunderstorm" -> if (fighter.attribute == ElementalAttribute.VACCINE) 1.15f else 1.10f
            "cosmic_nihilism", "quantum_erasure", "quantum_singularity", "dark_gravity", "zero_gravity", "apocalymon_void", "dark_annihilation", "grb_cataclysm", "grb_wave_cataclysm", "seven_sins_maw", "dark_area_sin_gates", "temporal_singularity" -> {
                if (fighterIdx == 0 && fighter.effort >= 4) 1.15f
                else if (fighterIdx == 1) 1.10f
                else 1.0f
            }
            "seraphic_grace", "holy_shield_aura", "divine_seals", "merciful_aurora", "aurora_blessing" -> if (fighter.attribute == ElementalAttribute.VACCINE) 1.20f else 0.95f
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
