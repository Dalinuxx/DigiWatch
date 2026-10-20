package com.digiwear.core

import com.digiwear.core.engine.EvolutionAlertEngine
import com.digiwear.core.engine.EvolutionEngine
import com.digiwear.core.engine.PetSimulationEngine
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.PetState
import com.digiwear.core.model.SpriteFrame
import com.digiwear.core.render.SpriteMatrixRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Comprehensive Unit Test Suite for Milestone 2 (M2.3):
 * Background Evolution Alert Engine, Mutation Detection, Species/Stage Resolution,
 * Dot-Matrix Sprite Integrity, and Edge Case Suppression (Frozen, Deceased, Fresh Hatch, Idempotency).
 */
class EvolutionAlertEngineTest {

    private val validDotChars = setOf('.', '#', 'B', 'K', 'E', 'W', 'T', 'S', 'R', 'G', 'C')

    /**
     * Test 1: Decaying PetState from species 201 (Agumon) with sufficient age produces species 301 (Greymon),
     * triggering finalSpeciesId != initialSpeciesId.
     */
    @Test
    fun testAgumonDecayToGreymonTriggersEvolutionAlert() {
        val initialAgumon = PetState(
            speciesId = 201, // Agumon (Child)
            ageSeconds = 90000L, // >= 86400s (24h)
            careMistakes = 1,    // 0..2 for Greymon
            effort = 3,          // >= 3
            weight = 32,         // >= 30
            lastProcessedTimestampUtc = 100000L
        )

        val currentUtc = 100060L
        val (finalState, alertPayload) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = initialAgumon,
            currentUtc = currentUtc
        )

        // Verify evolution mutation occurred
        assertEquals(301, finalState.speciesId)
        assertNotEquals(initialAgumon.speciesId, finalState.speciesId)
        assertTrue(finalState.speciesId != initialAgumon.speciesId)

        // Verify alert payload is created
        assertNotNull(alertPayload)
        assertEquals(201, alertPayload!!.initialSpeciesId)
        assertEquals(301, alertPayload.finalSpeciesId)
    }

    /**
     * Test 2: Boundary test - Agumon age exactly at threshold vs 1s below.
     */
    @Test
    fun testAgumonAgeBoundaryCondition() {
        // 1 second below 24-hour threshold (86400s)
        val underageAgumon = PetState(
            speciesId = 201,
            ageSeconds = 86399L,
            careMistakes = 1,
            effort = 3,
            weight = 32,
            lastProcessedTimestampUtc = 100000L
        )
        val (_, underageAlert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = underageAgumon,
            currentUtc = 100000L // 0 delta
        )
        assertNull("Agumon under 86400s must not trigger evolution", underageAlert)

        // Exactly at 86400s threshold
        val matureAgumon = underageAgumon.copy(ageSeconds = 86400L)
        val (_, matureAlert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = matureAgumon,
            currentUtc = 100000L
        )
        assertNotNull("Agumon at 86400s must trigger evolution", matureAlert)
        assertEquals(301, matureAlert!!.finalSpeciesId)
    }

    /**
     * Test 3: High care mistakes branch - Agumon evolves to Numemon (species 305).
     */
    @Test
    fun testAgumonToNumemonBranchAlert() {
        val neglectedAgumon = PetState(
            speciesId = 201,
            ageSeconds = 90000L,
            careMistakes = 7, // >= 6 for Numemon
            lastProcessedTimestampUtc = 100000L
        )

        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = neglectedAgumon,
            currentUtc = 100060L
        )

        assertEquals(305, finalState.speciesId)
        assertNotNull(alert)
        assertEquals("Numemon", alert!!.speciesName)
        assertEquals("Adult", alert.stageDisplayName)
        assertEquals("Numemon (Adult)", alert.contentText)
    }

    /**
     * Test 4: Resolution of canonical species name and stage displayName ("Greymon", "Adult").
     */
    @Test
    fun testSpeciesNameAndStageResolution() {
        val species = PetSpecies.getById(301)
        assertEquals("Greymon", species.name)
        assertEquals(PetStage.ADULT, species.stage)
        assertEquals("Adult", species.stage.displayName)

        val childSpecies = PetSpecies.getById(201)
        assertEquals("Agumon", childSpecies.name)
        assertEquals(PetStage.CHILD, childSpecies.stage)
        assertEquals("Child", childSpecies.stage.displayName)
    }

    /**
     * Test 5: Formatted notification title, content text, and summary strings.
     */
    @Test
    fun testNotificationPayloadFormatting() {
        val initialAgumon = PetState(speciesId = 201, ageSeconds = 90000L, effort = 3, weight = 32)
        val finalGreymon = PetState(speciesId = 301)

        val alert = EvolutionAlertEngine.evaluateEvolutionAlert(initialAgumon, finalGreymon)
        assertNotNull(alert)
        assertEquals(EvolutionAlertEngine.NOTIFICATION_TITLE, alert!!.notificationTitle)
        assertEquals("DIGITAL EVOLUTION!", alert.notificationTitle)
        assertEquals("Greymon (Adult)", alert.contentText)
        assertEquals("Greymon • Adult", alert.summaryText)
        assertEquals(EvolutionAlertEngine.CHANNEL_ID_EVOLUTION, "pet_evolution_channel")
        assertEquals(EvolutionAlertEngine.NOTIFICATION_ID_EVOLUTION, 1002)
    }

    /**
     * Test 6: Pixel art matrix and palette produce valid non-null dot-matrix data for newly evolved species.
     */
    @Test
    fun testNewlyEvolvedSpeciesSpriteMatrixAndPalette() {
        val evolvedSpeciesId = 301 // Greymon
        val matrix = SpriteMatrixRegistry.getMatrix(evolvedSpeciesId, SpriteFrame.IDLE_A)

        // Matrix dimensions must be 16x16
        assertEquals("Matrix row count must be 16", 16, matrix.size)
        var dotCount = 0
        var outlineCount = 0
        var fillCount = 0

        for (r in 0 until 16) {
            val row = matrix[r]
            assertEquals("Row $r must be 16 characters wide", 16, row.length)
            for (c in 0 until 16) {
                val ch = row[c]
                assertTrue("Character '$ch' must be a valid dot character", ch in validDotChars)
                if (ch != '.') dotCount++
                if (ch == 'B' || ch == 'K') outlineCount++
                if (ch in setOf('#', 'S', 'T', 'R', 'G', 'C', 'W')) fillCount++
            }
        }

        assertTrue("Greymon must have >= 20 active pixels (had $dotCount)", dotCount >= 20)
        assertTrue("Greymon must not be a solid block (had $dotCount)", dotCount <= 245)
        assertTrue("Greymon must have outline pixels", outlineCount > 0)
        assertTrue("Greymon must have multi-tone fill pixels", fillCount > 0)

        // Palette verification
        val palette = SpriteMatrixRegistry.getPalette(evolvedSpeciesId)
        assertNotNull("Palette must not be null", palette)
        assertEquals("Primary color alpha must be 0xFF", 0xFF, (palette.primary ushr 24) and 0xFF)
        assertEquals("Secondary color alpha must be 0xFF", 0xFF, (palette.secondary ushr 24) and 0xFF)
        assertEquals("Tertiary color alpha must be 0xFF", 0xFF, (palette.tertiary ushr 24) and 0xFF)
    }

    /**
     * Test 7: Edge Case - Pet is frozen in cold sleep stasis (no evolution occurs).
     */
    @Test
    fun testFrozenPetSuppressesEvolution() {
        val frozenAgumon = PetState(
            speciesId = 201,
            isFrozen = true,
            ageSeconds = 90000L,
            careMistakes = 1,
            effort = 3,
            weight = 32,
            lastProcessedTimestampUtc = 100000L
        )

        // 8 hours pass while frozen
        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = frozenAgumon,
            currentUtc = 100000L + 28800L
        )

        assertEquals("Species must remain unchanged while frozen", 201, finalState.speciesId)
        assertEquals("Age must not advance while frozen", 90000L, finalState.ageSeconds)
        assertTrue("Pet must remain frozen", finalState.isFrozen)
        assertNull("Frozen pet must never trigger evolution alert", alert)
        assertEquals(frozenAgumon.speciesId, finalState.speciesId)
    }

    /**
     * Test 8: Edge Case - Pet is deceased (no evolution occurs).
     */
    @Test
    fun testDeceasedPetSuppressesEvolution() {
        val deadAgumon = PetState(
            speciesId = 201,
            isDeceased = true,
            ageSeconds = 90000L,
            lastProcessedTimestampUtc = 100000L
        )

        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = deadAgumon,
            currentUtc = 100000L + 3600L
        )

        assertEquals("Species must remain unchanged when deceased", 201, finalState.speciesId)
        assertTrue("Pet must remain deceased", finalState.isDeceased)
        assertNull("Deceased pet must never trigger evolution alert", alert)
    }

    /**
     * Test 9: Edge Case - Fresh hatch / normal decay cycle where initialSpeciesId == finalSpeciesId.
     */
    @Test
    fun testFreshHatchNormalDecaySuppressesEvolution() {
        val freshBotamon = PetState(
            speciesId = 101, // Botamon (Baby I, requires 3600s to evolve)
            ageSeconds = 300L,
            lastProcessedTimestampUtc = 100000L
        )

        // 15-minute periodic decay
        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = freshBotamon,
            currentUtc = 100000L + 900L
        )

        assertEquals("Species must remain Botamon", 101, finalState.speciesId)
        assertEquals("Age must advance to 1200s", 1200L, finalState.ageSeconds)
        assertEquals(freshBotamon.speciesId, finalState.speciesId)
        assertNull("Normal decay without mutation must not trigger evolution alert", alert)
    }

    /**
     * Test 10: Edge Case - Idempotent notification suppression via SharedPreferences key.
     */
    @Test
    fun testIdempotentNotificationSuppressionLogic() {
        val initialId = 201
        val evolvedId = 301
        var lastNotifiedSpeciesId = -1

        // Phase 1: First evolution event from 201 to 301
        val shouldNotifyFirst = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = lastNotifiedSpeciesId,
            initialSpeciesId = initialId,
            finalSpeciesId = evolvedId
        )
        assertTrue("First evolution must trigger notification", shouldNotifyFirst)

        // Update preference key
        lastNotifiedSpeciesId = evolvedId

        // Phase 2: Next WorkManager run 15 minutes later (pet is now 301 -> 301)
        val shouldNotifyNextRun = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = lastNotifiedSpeciesId,
            initialSpeciesId = evolvedId,
            finalSpeciesId = evolvedId
        )
        assertFalse("Unchanged species must not trigger notification", shouldNotifyNextRun)

        // Phase 3: WorkManager re-runs with restored state or retry (201 -> 301) but pref already set
        val shouldNotifyRedundant = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = lastNotifiedSpeciesId,
            initialSpeciesId = initialId,
            finalSpeciesId = evolvedId
        )
        assertFalse("Already-notified species must be suppressed idempotently", shouldNotifyRedundant)

        // Phase 4: Subsequent evolution in future (301 -> 401 MetalGreymon)
        val nextEvolvedId = 401
        val shouldNotifyFuture = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = lastNotifiedSpeciesId,
            initialSpeciesId = evolvedId,
            finalSpeciesId = nextEvolvedId
        )
        assertTrue("Subsequent evolution to MetalGreymon must trigger notification", shouldNotifyFuture)
    }
}
