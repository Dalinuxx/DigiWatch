package com.digiwear.core

import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.fusion.FusionType
import com.digiwear.core.fusion.JogressEngine
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JogressEngineTest {

    @Test
    fun testOmnimonJogressCommutative() {
        val recipeForward = JogressEngine.findFusionRecipe(501, 503) // WarGreymon + MetalGarurumon
        val recipeReverse = JogressEngine.findFusionRecipe(503, 501) // MetalGarurumon + WarGreymon

        assertNotNull(recipeForward)
        assertNotNull(recipeReverse)
        assertEquals(510, recipeForward?.targetSpeciesId)
        assertEquals(510, recipeReverse?.targetSpeciesId)
        assertEquals(FusionType.DNA_JOGRESS, recipeForward?.fusionType)
    }

    @Test
    fun testMastemonLightAndDarkJogress() {
        val recipe = JogressEngine.findFusionRecipe(427, 428) // Angewomon + LadyDevimon
        assertNotNull(recipe)
        assertEquals(537, recipe?.targetSpeciesId)
        assertEquals("Mastemon", PetSpecies.getById(537).name)
    }

    @Test
    fun testPaildramonDNAJogress() {
        val recipe = JogressEngine.findFusionRecipe(332, 333) // Flamedramon + Stingmon
        assertNotNull(recipe)
        assertEquals(423, recipe?.targetSpeciesId)
        assertEquals("Paildramon", PetSpecies.getById(423).name)
    }

    @Test
    fun testFusionExecutionStateAndStatBonus() {
        val warGreymonState = PetState(
            speciesId = 501,
            maxHp = 290,
            currentHp = 290,
            weight = 30,
            careMistakes = 3,
            effort = 3,
            overfeedCount = 2,
            totalBattles = 25,
            victories = 20
        )

        val fusedState = JogressEngine.executeFusion(
            primary = warGreymonState,
            secondarySpeciesId = 503, // MetalGarurumon
            secondaryVictories = 15,
            secondaryEffort = 4,
            currentTimestampUtc = 1700000000L
        )

        assertEquals(510, fusedState.speciesId) // Omnimon
        assertEquals(35, fusedState.victories) // 20 + 15
        assertEquals(0, fusedState.careMistakes) // Reset on ascension
        assertEquals(0, fusedState.overfeedCount)
        assertEquals(4, fusedState.effort) // Max effort inherited
        // Synergy HP: Omnimon baseHp 620 * 1.10 = 682
        assertEquals(682, fusedState.maxHp)
        assertEquals(682, fusedState.currentHp)
    }

    @Test
    fun testDigimon0nl1neCommCodeCompatibilityWithJogressResult() {
        // Create an Omnimon resulting from Jogress
        val omnimon = PetSpecies.getById(510)
        val wins = 42
        val effort = 4
        val seed = 0x5432

        // Encode 128-bit Comm Code exactly as transmitted to Digimon 0nl1ne / D-Com
        val commCode = CommCodeCodec.encode128(
            monsterId = omnimon.id,
            stage = omnimon.stage,
            attribute = omnimon.attribute,
            basePowerHp = omnimon.basePower,
            historicalVictories = wins,
            trainingEffort = effort,
            battleRngSeed = seed
        )

        assertNotNull(commCode)
        assertEquals(26, commCode.length)

        // Verify that standard Digimon 0nl1ne decoder decodes and verifies HMAC without error
        val payload = CommCodeCodec.decode128(commCode)
        assertEquals(510, payload.monsterId)
        assertEquals(omnimon.stage, payload.stage)
        assertEquals(omnimon.attribute, payload.attribute)
        assertEquals(omnimon.basePower, payload.basePowerHp)
        assertEquals(wins, payload.historicalVictories)
        assertEquals(effort, payload.trainingEffort)
        assertEquals(seed, payload.battleRngSeed)
    }
}
