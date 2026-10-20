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
 * Adversarial and Empirical Verification Test Suite for Milestone 2 (R2):
 * Background Evolution Alerts & Digital Monster Call Alarm.
 *
 * Authored by Challenger 1.
 * Focus Areas:
 * 1. Boundary conditions (exact 86,400s threshold, 1s delta, Baby I/II thresholds, negative values).
 * 2. Multi-tier branching (Agumon -> Greymon, Tyrannomon, Meramon, Numemon; Gabumon branching).
 * 3. Life-cycle stasis suppression (Cold Sleep stasis, deceased state, death during decay).
 * 4. SharedPreferences idempotency state machine across WorkManager periodic cycles, retries, and reboots.
 * 5. Sprite matrix and palette verification across Champion (Adult), Ultimate (Perfect), and Mega tiers.
 * 6. Edge cases: cross-generation species reuse, invalid IDs, and payload string conformity.
 */
class AdversarialEvolutionAlertEngineTest {

    private val validBandaiDotChars = setOf('.', '#', 'B', 'K', 'E', 'W', 'T', 'S', 'R', 'G', 'C')

    // =========================================================================
    // 1. BOUNDARY CONDITIONS: AGING SECONDS THRESHOLDS
    // =========================================================================

    @Test
    fun testExact86400sBoundaryPrecisionForChildToAdult() {
        val baseAgumon = PetState(
            speciesId = 201, // Agumon (Child)
            careMistakes = 1,
            effort = 3,
            weight = 32,
            lastProcessedTimestampUtc = 1_000_000L
        )

        // Case 1: 1 second before the 24h threshold (86,399s)
        val underageAgumon = baseAgumon.copy(ageSeconds = 86_399L)
        val (stateUnder, alertUnder) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = underageAgumon,
            currentUtc = 1_000_000L // 0s elapsed
        )
        assertEquals("Species must not evolve at 86,399s", 201, stateUnder.speciesId)
        assertNull("Alert payload must be null when underage", alertUnder)

        // Case 2: Exactly at the 24h threshold (86,400s)
        val matureAgumon = baseAgumon.copy(ageSeconds = 86_400L)
        val (stateExact, alertExact) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = matureAgumon,
            currentUtc = 1_000_000L
        )
        assertEquals("Species must evolve to Greymon exactly at 86,400s", 301, stateExact.speciesId)
        assertNotNull("Alert payload must be present at 86,400s", alertExact)
        assertEquals("DIGITAL EVOLUTION!", alertExact!!.notificationTitle)
        assertEquals("Greymon (Adult)", alertExact.contentText)

        // Case 3: 1 second past threshold (86,401s)
        val overAgumon = baseAgumon.copy(ageSeconds = 86_401L)
        val (stateOver, alertOver) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = overAgumon,
            currentUtc = 1_000_000L
        )
        assertEquals("Species must evolve to Greymon at 86,401s", 301, stateOver.speciesId)
        assertNotNull("Alert payload must be present at 86,401s", alertOver)
    }

    @Test
    fun testBabyStagesAgingThresholdBoundaries() {
        // Baby I (Botamon #101) evolves to Baby II (Koromon #102) at 3,600s (1 hour)
        val freshBotamon = PetState(
            speciesId = 101,
            ageSeconds = 3_599L,
            lastProcessedTimestampUtc = 100_000L
        )

        val (botamonUnder, alertBabyUnder) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = freshBotamon,
            currentUtc = 100_000L
        )
        assertEquals(101, botamonUnder.speciesId)
        assertNull("Botamon at 3,599s must not trigger evolution", alertBabyUnder)

        // Botamon with 0 mistakes evolves to Sakuttomon (#1103, Baby II)
        val matureBotamon = freshBotamon.copy(ageSeconds = 3_600L)
        val (botamonExact, alertBabyExact) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = matureBotamon,
            currentUtc = 100_000L
        )
        assertEquals(1103, botamonExact.speciesId)
        assertNotNull("Botamon at 3,600s must trigger evolution alert", alertBabyExact)
        assertEquals("Sakuttomon", alertBabyExact!!.speciesName)
        assertEquals("Baby II", alertBabyExact.stageDisplayName)
        assertEquals("Sakuttomon (Baby II)", alertBabyExact.contentText)

        // Botamon with 3 mistakes evolves to Koromon (#102, Baby II)
        val neglectedBotamon = freshBotamon.copy(ageSeconds = 3_600L, careMistakes = 3)
        val (botamonKoromon, alertKoromonBaby) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = neglectedBotamon,
            currentUtc = 100_000L
        )
        assertEquals(102, botamonKoromon.speciesId)
        assertNotNull("Botamon with 3 mistakes must evolve to Koromon", alertKoromonBaby)
        assertEquals("Koromon", alertKoromonBaby!!.speciesName)
        assertEquals("Baby II", alertKoromonBaby.stageDisplayName)

        // Baby II (Koromon #102) evolves to Child (Agumon #201) at 43,200s (12 hours)
        val matureKoromon = PetState(
            speciesId = 102,
            ageSeconds = 43_200L,
            careMistakes = 1,
            effort = 2,
            lastProcessedTimestampUtc = 100_000L
        )
        val (koromonEvolved, alertKoromon) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = matureKoromon,
            currentUtc = 100_000L
        )
        assertEquals(201, koromonEvolved.speciesId)
        assertNotNull("Koromon at 43,200s must trigger evolution alert", alertKoromon)
        assertEquals("Agumon", alertKoromon!!.speciesName)
        assertEquals("Child", alertKoromon.stageDisplayName)
    }

    // =========================================================================
    // 2. CARE MISTAKE BRANCHING: NUMEMON VS CANONICAL EVOLUTIONS
    // =========================================================================

    @Test
    fun testCareMistakeBranchingMatrixForAgumon() {
        val baseState = PetState(
            speciesId = 201, // Agumon
            ageSeconds = 90_000L,
            weight = 35,
            effort = 4,
            lastProcessedTimestampUtc = 100_000L
        )

        // Branch 1: High care mistakes (>= 6) -> Numemon (#305)
        for (mistakes in listOf(6, 7, 10, 15, 20)) {
            val neglected = baseState.copy(careMistakes = mistakes)
            val (state, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(neglected, 100_000L)
            assertEquals("Agumon with $mistakes mistakes must evolve to Numemon (#305)", 305, state.speciesId)
            assertNotNull(alert)
            assertEquals("Numemon", alert!!.speciesName)
            assertEquals("Adult", alert.stageDisplayName)
            assertEquals("Numemon (Adult)", alert.contentText)
        }

        // Branch 2: Low care mistakes (0..2), high effort (>=3), weight >= 30 -> Greymon (#301)
        for (mistakes in listOf(0, 1, 2)) {
            val pristine = baseState.copy(careMistakes = mistakes, effort = 3, weight = 32)
            val (state, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(pristine, 100_000L)
            assertEquals("Agumon with $mistakes mistakes and high effort must evolve to Greymon (#301)", 301, state.speciesId)
            assertNotNull(alert)
            assertEquals("Greymon", alert!!.speciesName)
            assertEquals("Adult", alert.stageDisplayName)
        }
    }

    // =========================================================================
    // 3. LIFE-CYCLE STASIS & DECEASED SUPPRESSION
    // =========================================================================

    @Test
    fun testColdSleepFreezerStasisSuppressesEvolutionAlert() {
        val frozenAgumon = PetState(
            speciesId = 201,
            ageSeconds = 90_000L,
            isFrozen = true,
            effort = 3,
            weight = 32,
            lastProcessedTimestampUtc = 100_000L
        )

        // 12 hours of real time pass while watch is in freezer mode
        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = frozenAgumon,
            currentUtc = 100_000L + 43_200L
        )

        assertEquals("Species must not change while in cold sleep stasis", 201, finalState.speciesId)
        assertEquals("Age must remain frozen", 90_000L, finalState.ageSeconds)
        assertTrue("Pet must remain frozen", finalState.isFrozen)
        assertNull("EvolutionAlertEngine must return null alert for frozen pet", alert)
    }

    @Test
    fun testDeceasedPetSuppressesEvolutionAlert() {
        val deceasedAgumon = PetState(
            speciesId = 201,
            ageSeconds = 100_000L,
            isDeceased = true,
            lastProcessedTimestampUtc = 100_000L
        )

        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = deceasedAgumon,
            currentUtc = 100_000L + 3_600L
        )

        assertEquals("Deceased pet must remain deceased species", 201, finalState.speciesId)
        assertTrue("Pet must remain marked deceased", finalState.isDeceased)
        assertNull("Deceased pet must never generate evolution alert", alert)
    }

    @Test
    fun testDeathDuringDecayPreemptsEvolutionAlert() {
        // Pet reaches max lifespan and perishes in the same decay cycle
        val fragileAgumon = PetState(
            speciesId = 201,
            ageSeconds = 86_300L, // Approaching lifespan
            lifespanDays = 1,     // 86,400s max lifespan
            careMistakes = 20,
            isInjured = true,
            lastProcessedTimestampUtc = 100_000L
        )

        // Advance past lifespan with canonical lifespan enabled
        val (finalState, alert) = EvolutionAlertEngine.processDecayAndEvaluateAlert(
            initialState = fragileAgumon,
            currentUtc = 100_000L + 500L,
            isCanonicalLifespanEnabled = true
        )

        assertTrue("Pet must be marked deceased", finalState.isDeceased)
        assertNull("Evolution alert must be suppressed if pet died during decay cycle", alert)
    }

    // =========================================================================
    // 4. SHAREDPREFERENCES IDEMPOTENCY STATE MACHINE & REBOOTS
    // =========================================================================

    @Test
    fun testComprehensiveIdempotencyStateMachineAcrossCyclesAndReboots() {
        var prefsLastNotified = -1

        // Cycle 1: First evolution Child (Agumon #201) -> Adult (Greymon #301)
        val initialSpecies1 = 201
        val evolvedSpecies1 = 301
        val shouldNotifyCycle1 = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified,
            initialSpeciesId = initialSpecies1,
            finalSpeciesId = evolvedSpecies1
        )
        assertTrue("Cycle 1: Initial evolution must trigger notification", shouldNotifyCycle1)

        // SharedPreferences committed:
        prefsLastNotified = evolvedSpecies1
        assertEquals(301, prefsLastNotified)

        // Cycle 2: Periodic WorkManager awakens 15 minutes later. Pet remains Greymon (301 -> 301)
        val shouldNotifyCycle2 = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified,
            initialSpeciesId = 301,
            finalSpeciesId = 301
        )
        assertFalse("Cycle 2: Periodic decay with no mutation must be suppressed", shouldNotifyCycle2)

        // Cycle 3: Worker retry / rapid re-invocation with old pre-decay state (201 -> 301)
        val shouldNotifyRetry = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified,
            initialSpeciesId = 201,
            finalSpeciesId = 301
        )
        assertFalse("Cycle 3: Retry where target species matches lastNotified must be suppressed", shouldNotifyRetry)

        // Cycle 4: Device reboot / cold process restart. SharedPreferences reloaded from disk
        val reloadedPrefs = prefsLastNotified // Value 301 preserved across reboot
        val shouldNotifyPostReboot = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = reloadedPrefs,
            initialSpeciesId = 301,
            finalSpeciesId = 301
        )
        assertFalse("Cycle 4: Post-reboot steady state must be suppressed", shouldNotifyPostReboot)

        // Cycle 5: Subsequent evolution 36 hours later: Adult (Greymon #301) -> Perfect (MetalGreymon #401)
        val initialSpecies2 = 301
        val evolvedSpecies2 = 401
        val shouldNotifyCycle5 = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified,
            initialSpeciesId = initialSpecies2,
            finalSpeciesId = evolvedSpecies2
        )
        assertTrue("Cycle 5: Progression to MetalGreymon must trigger notification", shouldNotifyCycle5)
        prefsLastNotified = evolvedSpecies2

        // Cycle 6: Subsequent evolution to Mega: Perfect (MetalGreymon #401) -> Ultimate/Mega (WarGreymon #501)
        val initialSpecies3 = 401
        val evolvedSpecies3 = 501
        val shouldNotifyCycle6 = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified,
            initialSpeciesId = initialSpecies3,
            finalSpeciesId = evolvedSpecies3
        )
        assertTrue("Cycle 6: Progression to WarGreymon must trigger notification", shouldNotifyCycle6)
        prefsLastNotified = evolvedSpecies3

        // Cycle 7: Pet passes away, user hatches a new egg (Botamon #101).
        // Steady state decay for newborn (101 -> 101)
        val shouldNotifyNewborn = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified, // Still 501 unless explicitly reset
            initialSpeciesId = 101,
            finalSpeciesId = 101
        )
        assertFalse("Cycle 7: Newborn steady state must not notify", shouldNotifyNewborn)

        // Cycle 8: Newborn evolves to Koromon (101 -> 102)
        val shouldNotifyNewbornEvolve = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = prefsLastNotified,
            initialSpeciesId = 101,
            finalSpeciesId = 102
        )
        assertTrue("Cycle 8: Generation 2 first evolution must notify", shouldNotifyNewbornEvolve)
        prefsLastNotified = 102
    }

    @Test
    fun testCrossGenerationSameSpeciesEdgeCase() {
        // Critical Edge Case:
        // Generation 1 Greymon (#301) dies. SharedPreferences has lastNotifiedSpeciesId = 301.
        // User hatches Botamon (#101), evolves to Koromon (#102), then Agumon (#201) in foreground UI.
        // If foreground UI does not update or clear SharedPreferences, lastNotifiedSpeciesId remains 301!
        val stalePrefFromGen1 = 301
        val gen2Initial = 201 // Agumon
        val gen2Final = 301   // Greymon

        val shouldNotify = EvolutionAlertEngine.shouldNotifyEvolution(
            lastNotifiedSpeciesId = stalePrefFromGen1,
            initialSpeciesId = gen2Initial,
            finalSpeciesId = gen2Final
        )

        // Empirically document the limitation:
        // With current pure species ID comparison, if lastNotified is identical to the new species,
        // it returns false (suppressed).
        assertFalse(
            "Empirical Finding: When lastNotifiedSpeciesId matches finalSpeciesId, alert is suppressed even if generations differ",
            shouldNotify
        )
    }

    // =========================================================================
    // 5. SPRITE MATRICES & COLOR PALETTES ACROSS EVOLUTION TIERS
    // =========================================================================

    @Test
    fun testSpriteMatricesAndPalettesAcrossMultiTierEvolutionTargets() {
        // Test targets across Champion (Adult), Ultimate (Perfect), and Mega tiers:
        val testSpecies = listOf(
            // Champion / Adult
            301 to "Greymon",
            302 to "Tyrannomon",
            303 to "Devimon",
            304 to "Meramon",
            305 to "Numemon",
            306 to "Garurumon",
            307 to "Angemon",
            // Ultimate / Perfect
            401 to "MetalGreymon",
            402 to "Monzaemon",
            403 to "SkullGreymon",
            404 to "WereGarurumon",
            405 to "MagnaAngemon",
            406 to "Lillymon",
            // Mega / Ultimate
            501 to "WarGreymon",
            502 to "Machinedramon",
            503 to "MetalGarurumon",
            504 to "Seraphimon",
            505 to "Rosemon"
        )

        for ((speciesId, expectedName) in testSpecies) {
            val species = PetSpecies.getById(speciesId)
            assertEquals("Species ID $speciesId must resolve to $expectedName", expectedName, species.name)

            // Test payload generation
            val dummyInitial = PetState(speciesId = 201)
            val dummyFinal = PetState(speciesId = speciesId)
            val payload = EvolutionAlertEngine.evaluateEvolutionAlert(dummyInitial, dummyFinal)
            assertNotNull("Payload for species $speciesId ($expectedName) must not be null", payload)

            assertEquals("DIGITAL EVOLUTION!", payload!!.notificationTitle)
            assertEquals("${species.name} (${species.stage.displayName})", payload.contentText)
            assertEquals("${species.name} • ${species.stage.displayName}", payload.summaryText)

            // Verify Sprite Matrix
            val matrix = payload.matrix
            assertEquals("Matrix for $speciesId ($expectedName) must have 16 rows", 16, matrix.size)
            var dotCount = 0
            var outlineCount = 0
            var fillCount = 0

            for (r in 0 until 16) {
                val row = matrix[r]
                assertEquals("Row $r in $expectedName must be 16 characters wide", 16, row.length)
                for (c in 0 until 16) {
                    val ch = row[c]
                    assertTrue(
                        "Character '$ch' at ($r,$c) in $expectedName must be a valid Bandai pixel code",
                        ch in validBandaiDotChars
                    )
                    if (ch != '.') dotCount++
                    if (ch == 'B' || ch == 'K') outlineCount++
                    if (ch in setOf('#', 'S', 'T', 'R', 'G', 'C', 'W')) fillCount++
                }
            }

            assertTrue("Species $expectedName must have active pixels (had $dotCount)", dotCount >= 20)
            assertTrue("Species $expectedName must not be a solid box (had $dotCount)", dotCount <= 245)
            assertTrue("Species $expectedName must have dark outline pixels (had $outlineCount)", outlineCount > 0)
            assertTrue("Species $expectedName must have shaded fill pixels (had $fillCount)", fillCount > 0)

            // Verify Color Palette
            val palette = payload.palette
            assertNotNull("Palette for $expectedName must not be null", palette)
            assertEquals("Primary alpha must be 0xFF for $expectedName", 0xFF, (palette.primary ushr 24) and 0xFF)
            assertEquals("Secondary alpha must be 0xFF for $expectedName", 0xFF, (palette.secondary ushr 24) and 0xFF)
            assertEquals("Tertiary alpha must be 0xFF for $expectedName", 0xFF, (palette.tertiary ushr 24) and 0xFF)
            assertEquals("Eye alpha must be 0xFF for $expectedName", 0xFF, (palette.eye ushr 24) and 0xFF)
        }
    }

    // =========================================================================
    // 6. INVALID INPUTS & DEFENSIVE CONSTRAINTS
    // =========================================================================

    @Test
    fun testDefensiveHandlingOfInvalidAndIdenticalSpeciesIds() {
        val normalState = PetState(speciesId = 201)

        // Identical species ID (no evolution)
        assertNull(EvolutionAlertEngine.evaluateEvolutionAlert(normalState, normalState))
        assertFalse(EvolutionAlertEngine.shouldNotifyEvolution(-1, 201, 201))

        // Non-positive species IDs
        val zeroState = PetState(speciesId = 0)
        val negState = PetState(speciesId = -1)
        assertNull(EvolutionAlertEngine.evaluateEvolutionAlert(zeroState, normalState))
        assertNull(EvolutionAlertEngine.evaluateEvolutionAlert(normalState, zeroState))
        assertNull(EvolutionAlertEngine.evaluateEvolutionAlert(negState, normalState))
        assertNull(EvolutionAlertEngine.evaluateEvolutionAlert(normalState, negState))

        assertFalse(EvolutionAlertEngine.shouldNotifyEvolution(-1, 0, 301))
        assertFalse(EvolutionAlertEngine.shouldNotifyEvolution(-1, 201, 0))
        assertFalse(EvolutionAlertEngine.shouldNotifyEvolution(-1, -1, 301))
        assertFalse(EvolutionAlertEngine.shouldNotifyEvolution(-1, 201, -1))
    }
}
