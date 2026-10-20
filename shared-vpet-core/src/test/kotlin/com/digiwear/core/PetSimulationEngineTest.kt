package com.digiwear.core

import com.digiwear.core.engine.PetSimulationEngine
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.PetState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneOffset

class PetSimulationEngineTest {

    @Test
    fun testBabyIDecayAndNoSleep() {
        // Baby I: Hunger and Strength decay every 3 minutes (180s); waste every 15 minutes (900s)
        val initial = PetState(
            speciesId = 101, // Botamon (Baby I)
            hunger = 4,
            strength = 4,
            wasteCount = 0,
            lastProcessedTimestampUtc = 43200L // 12:00 PM UTC
        )

        // Advance 3 minutes (180s)
        val after3Min = PetSimulationEngine.processDelta(initial, 43380L, ZoneOffset.UTC)
        assertEquals(3, after3Min.hunger)
        assertEquals(3, after3Min.strength)
        assertEquals(0, after3Min.wasteCount)

        // Advance 15 minutes total (900s)
        val after15Min = PetSimulationEngine.processDelta(initial, 44100L, ZoneOffset.UTC)
        assertEquals(0, after15Min.hunger)
        assertEquals(0, after15Min.strength)
        assertEquals(1, after15Min.wasteCount)

        // Baby I never sleeps
        assertFalse(after15Min.isSleeping)
        // Baby I is exempt from care mistakes
        assertEquals(0, after15Min.careMistakes)
    }

    @Test
    fun testBabyIICareMistakeGraceWindow() {
        // Baby II (Koromon): Grace window is 10 minutes (600s)
        // Start at 12:00 PM UTC (awake)
        val initial = PetState(
            speciesId = 102, // Koromon
            hunger = 1,
            strength = 1,
            careMistakes = 0,
            lastProcessedTimestampUtc = 43200L // 12:00:00
        )

        // 10 minutes pass (12:10:00 = 43800L): hunger drops to 0, triggering attention call
        val atZeroHunger = PetSimulationEngine.processDelta(initial, 43800L, ZoneOffset.UTC)
        assertEquals(0, atZeroHunger.hunger)
        assertTrue(atZeroHunger.attentionCallActive)
        assertEquals(0, atZeroHunger.careMistakes)

        // 5 minutes later (12:15:00 = 44100L, still within 10 min grace window)
        val withinGrace = PetSimulationEngine.processDelta(atZeroHunger, 44100L, ZoneOffset.UTC)
        assertTrue(withinGrace.attentionCallActive)
        assertEquals(0, withinGrace.careMistakes)

        // 11 minutes after call triggered (12:21:00 = 44460L): Grace window expired! Care mistake recorded!
        val pastGrace = PetSimulationEngine.processDelta(atZeroHunger, 44460L, ZoneOffset.UTC)
        assertEquals(1, pastGrace.careMistakes)
    }

    @Test
    fun testSleepWindowPausesMetabolicDecay() {
        // Koromon sleeps 20:00 to 07:00
        // Timestamp at 22:00 UTC = 22 * 3600 = 79200L
        val sleepingKoromon = PetState(
            speciesId = 102,
            hunger = 2,
            strength = 2,
            lastProcessedTimestampUtc = 79200L
        )

        // 30 minutes pass during sleep (79200 + 1800 = 81000L)
        val after30Min = PetSimulationEngine.processDelta(sleepingKoromon, 81000L, ZoneOffset.UTC)
        assertTrue(after30Min.isSleeping)
        // Hunger remains paused during sleep
        assertEquals(2, after30Min.hunger)
    }

    @Test
    fun testOverfeedMechanism() {
        val initial = PetState(
            speciesId = 201, // Agumon
            hunger = 4,
            weight = 20,
            overfeedCount = 0
        )

        val afterFeed = PetSimulationEngine.feedMeal(initial, 43200L)
        assertEquals(4, afterFeed.hunger)
        assertEquals(21, afterFeed.weight)
        assertEquals(1, afterFeed.overfeedCount)

        val afterSecondFeed = PetSimulationEngine.feedMeal(afterFeed, 43201L)
        assertEquals(2, afterSecondFeed.overfeedCount)
        assertEquals(22, afterSecondFeed.weight)
    }

    @Test
    fun testWasteAccumulationTriggersInjury() {
        val initial = PetState(
            speciesId = 201, // Agumon (Child: waste every 60 min = 3600s)
            wasteCount = 3,
            isInjured = false,
            lastProcessedTimestampUtc = 43200L
        )

        // 1 hour passes -> 4th waste unit generated -> triggers injury!
        val afterHour = PetSimulationEngine.processDelta(initial, 46800L, ZoneOffset.UTC)
        assertEquals(4, afterHour.wasteCount)
        assertTrue(afterHour.isInjured)

        // Cleaning waste
        val cleaned = PetSimulationEngine.cleanWaste(afterHour)
        assertEquals(0, cleaned.wasteCount)

        // Curing injury
        val cured = PetSimulationEngine.cureInjury(cleaned)
        assertFalse(cured.isInjured)
    }

    @Test
    fun testColdSleepPausesAllDecay() {
        val initial = PetState(
            speciesId = 201, // Agumon
            hunger = 4,
            strength = 4,
            wasteCount = 0,
            isFrozen = true,
            lastProcessedTimestampUtc = 43200L
        )

        // 8 hours pass while frozen in cold sleep stasis
        val after8Hours = PetSimulationEngine.processDelta(initial, 43200L + 28800L, ZoneOffset.UTC)
        assertEquals(4, after8Hours.hunger)
        assertEquals(4, after8Hours.strength)
        assertEquals(0, after8Hours.wasteCount)
        assertEquals(0L, after8Hours.ageSeconds)
        assertFalse(after8Hours.attentionCallActive)
        assertEquals(0, after8Hours.careMistakes)
        assertTrue(after8Hours.isFrozen)

        // Waking up from cold sleep
        val awakened = PetSimulationEngine.toggleColdSleep(after8Hours, 43200L + 28800L)
        assertFalse(awakened.isFrozen)
    }
}
