package com.digiwatch.core

import com.digiwatch.core.battle.DeterministicBattleSolver
import com.digiwatch.core.model.BattleFighter
import com.digiwatch.core.model.ElementalAttribute
import com.digiwatch.core.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DeterministicBattleSolverTest {

    @Test
    fun testBattleSymmetryAndDeterminism() {
        val fighterA = BattleFighter(
            id = "fighter_a",
            speciesId = 301, // Greymon
            name = "Greymon",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            maxHp = 240,
            currentHp = 240,
            basePower = 65,
            victories = 10,
            effort = 3,
            battleSeed = 0x1337
        )

        val fighterB = BattleFighter(
            id = "fighter_b",
            speciesId = 303, // Devimon
            name = "Devimon",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VIRUS,
            maxHp = 230,
            currentHp = 230,
            basePower = 68,
            victories = 8,
            effort = 2,
            battleSeed = 0xBEEF
        )

        val result1 = DeterministicBattleSolver.resolveBattle(fighterA, fighterB)
        val result2 = DeterministicBattleSolver.resolveBattle(fighterA, fighterB)

        // Multiple runs with same seeds must produce identical turn count, damage, and winner
        assertEquals(result1.winnerIndex, result2.winnerIndex)
        assertEquals(result1.totalRounds, result2.totalRounds)
        assertEquals(result1.turns.size, result2.turns.size)

        for (i in result1.turns.indices) {
            val t1 = result1.turns[i]
            val t2 = result2.turns[i]
            assertEquals(t1.damage, t2.damage)
            assertEquals(t1.defenderRemainingHp, t2.defenderRemainingHp)
        }
    }

    @Test
    fun testVaccineOvercomesVirusAdvantage() {
        // Vaccine vs Virus multiplier is 1.2x
        val mult = ElementalAttribute.VACCINE.damageMultiplierAgainst(ElementalAttribute.VIRUS)
        assertEquals(1.2f, mult, 0.001f)

        // Virus vs Data multiplier is 1.2x
        val mult2 = ElementalAttribute.VIRUS.damageMultiplierAgainst(ElementalAttribute.DATA)
        assertEquals(1.2f, mult2, 0.001f)

        // Data vs Vaccine multiplier is 1.2x
        val mult3 = ElementalAttribute.DATA.damageMultiplierAgainst(ElementalAttribute.VACCINE)
        assertEquals(1.2f, mult3, 0.001f)
    }

    @Test
    fun testMax10RoundsEnforced() {
        // High HP, very low power fighters
        val tankA = BattleFighter(
            id = "tank_a",
            speciesId = 301,
            name = "Tank A",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.FREE,
            maxHp = 9999,
            currentHp = 9999,
            basePower = 1,
            victories = 0,
            effort = 0,
            battleSeed = 100
        )
        val tankB = BattleFighter(
            id = "tank_b",
            speciesId = 302,
            name = "Tank B",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.FREE,
            maxHp = 9999,
            currentHp = 9999,
            basePower = 1,
            victories = 0,
            effort = 0,
            battleSeed = 200
        )

        val result = DeterministicBattleSolver.resolveBattle(tankA, tankB)
        assertTrue("Rounds must not exceed 10", result.totalRounds <= 10)
    }

    @Test
    fun testAttackerCritBoostIncreasesPlayerDamageBy25Percent() {
        val fighterA = BattleFighter(
            id = "player",
            speciesId = 301,
            name = "Greymon",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            maxHp = 500,
            currentHp = 500,
            basePower = 100,
            victories = 10,
            effort = 4,
            battleSeed = 0x1234
        )
        val fighterB = BattleFighter(
            id = "opponent",
            speciesId = 303,
            name = "Devimon",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VIRUS,
            maxHp = 500,
            currentHp = 500,
            basePower = 80,
            victories = 5,
            effort = 2,
            battleSeed = 0x5678
        )

        val normalResult = DeterministicBattleSolver.resolveBattle(fighterA, fighterB, attackerCritBoost = false)
        val critResult = DeterministicBattleSolver.resolveBattle(fighterA, fighterB, attackerCritBoost = true)

        val firstPlayerTurnNormal = normalResult.turns.first { it.attackerIndex == 0 }
        val firstPlayerTurnCrit = critResult.turns.first { it.attackerIndex == 0 }

        assertTrue(
            "Crit boost damage (${firstPlayerTurnCrit.damage}) must exceed normal damage (${firstPlayerTurnNormal.damage})",
            firstPlayerTurnCrit.damage > firstPlayerTurnNormal.damage
        )
        assertEquals(
            firstPlayerTurnNormal.damage * 1.25f,
            firstPlayerTurnCrit.damage.toFloat(),
            2.0f
        )
    }

    @Test
    fun testEnvironmentalWeatherHazardsModifyElementalDamage() {
        val virusFighter = BattleFighter(
            id = "virus",
            speciesId = 303,
            name = "Devimon",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VIRUS,
            maxHp = 999,
            currentHp = 999,
            basePower = 100,
            victories = 10,
            effort = 4,
            battleSeed = 0x1111
        )
        val vaccineFighter = BattleFighter(
            id = "vaccine",
            speciesId = 301,
            name = "Greymon",
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            maxHp = 999,
            currentHp = 999,
            basePower = 100,
            victories = 10,
            effort = 4,
            battleSeed = 0x2222
        )

        val clearRes = DeterministicBattleSolver.resolveBattle(virusFighter, vaccineFighter, environmentEffect = "none")
        val glitchFogRes = DeterministicBattleSolver.resolveBattle(virusFighter, vaccineFighter, environmentEffect = "glitch_fog")
        val cosmicRes = DeterministicBattleSolver.resolveBattle(virusFighter, vaccineFighter, environmentEffect = "cosmic_nihilism")

        val virusDmgClear = clearRes.turns.first { it.attackerIndex == 0 }.damage
        val virusDmgGlitch = glitchFogRes.turns.first { it.attackerIndex == 0 }.damage
        val virusDmgCosmic = cosmicRes.turns.first { it.attackerIndex == 0 }.damage

        assertTrue("glitch_fog must boost Virus damage ($virusDmgGlitch > $virusDmgClear)", virusDmgGlitch > virusDmgClear)
        assertTrue("cosmic_nihilism must boost damage ($virusDmgCosmic > $virusDmgClear)", virusDmgCosmic > virusDmgClear)
    }
}
