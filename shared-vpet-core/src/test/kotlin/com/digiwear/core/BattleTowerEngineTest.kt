package com.digiwear.core

import com.digiwear.core.battle.DeterministicBattleSolver
import com.digiwear.core.colosseum.BattleTowerEngine
import com.digiwear.core.model.BattleFighter
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BattleTowerEngineTest {

    @Test
    fun testAllHundredFloorsAreValid() {
        for (f in 1..BattleTowerEngine.MAX_FLOORS) {
            val floor = BattleTowerEngine.getFloor(f)
            assertEquals("Floor number must match requested", f, floor.floorNumber)
            assertTrue("Tier must be between 1 and 10", floor.tierNumber in 1..10)
            assertTrue("Tier name must not be blank", floor.tierName.isNotBlank())
            assertTrue("Opponent HP must be positive", floor.opponentHp > 0)
            assertTrue("Opponent Power must be positive", floor.opponentPower > 0)
            assertTrue("Reward description must not be blank", floor.rewardDescription.isNotBlank())

            val sp = PetSpecies.getById(floor.opponentSpeciesId)
            assertEquals("Opponent name matches species", sp.name, floor.opponentName)
            assertEquals("Opponent stage matches species", sp.stage, floor.opponentStage)
            assertEquals("Opponent attribute matches species", sp.attribute, floor.opponentAttribute)

            if (f % 10 == 0) {
                assertTrue("Milestone floor $f must be marked as boss", floor.isBossFloor)
            }
        }
    }

    @Test
    fun testMilestoneBosses() {
        val expectedBosses = mapOf(
            10 to 201,  // Agumon
            20 to 301,  // Greymon
            30 to 338,  // Gatomon
            40 to 401,  // MetalGreymon
            50 to 427,  // Angewomon
            60 to 501,  // WarGreymon
            70 to 510,  // Omnimon
            80 to 579,  // Gallantmon X
            90 to 567,  // Chaosmon
            100 to 597  // Proximamon
        )

        for ((floorNum, expectedSpeciesId) in expectedBosses) {
            val floor = BattleTowerEngine.getFloor(floorNum)
            assertEquals("Floor $floorNum boss species must match", expectedSpeciesId, floor.opponentSpeciesId)
            assertTrue("Floor $floorNum must be a boss floor", floor.isBossFloor)
        }
    }

    @Test
    fun testClampingOutOfBounds() {
        val fLow = BattleTowerEngine.getFloor(0)
        assertEquals("Floor 0 clamps to 1", 1, fLow.floorNumber)

        val fHigh = BattleTowerEngine.getFloor(150)
        assertEquals("Floor 150 clamps to 100", 100, fHigh.floorNumber)
    }

    @Test
    fun testCreateOpponentFighter() {
        val fighter = BattleTowerEngine.createOpponentFighter(50)
        assertEquals("tower_floor_50", fighter.id)
        assertEquals(427, fighter.speciesId)
        assertEquals("Angewomon", fighter.name)
        assertTrue(fighter.maxHp > 0)
        assertEquals(fighter.maxHp, fighter.currentHp)
        assertTrue(fighter.basePower > 0)
    }

    @Test
    fun testDeterministicBattleAgainstTowerOpponent() {
        val player = BattleFighter(
            id = "player_omnimon",
            speciesId = 510,
            name = "Omnimon",
            stage = PetStage.ULTIMATE,
            attribute = ElementalAttribute.VACCINE,
            maxHp = 300,
            currentHp = 300,
            basePower = 95,
            victories = 100,
            effort = 4,
            strength = 4,
            battleSeed = 12345
        )

        val opponent = BattleTowerEngine.createOpponentFighter(10) // Floor 10 Boss: Agumon
        val result = DeterministicBattleSolver.resolveBattle(player, opponent)

        assertNotNull(result)
        assertTrue("Log turns must not be empty", result.turns.isNotEmpty())
        assertEquals("Player Omnimon should defeat Floor 10 Agumon", 0, result.winnerIndex)
    }
}
