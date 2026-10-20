package com.digiwear.wear.worker

import com.digiwear.core.model.PetState
import com.digiwear.wear.receiver.NotificationQuickCareReceiver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Adversarial test harness stress-testing PetDecayWorker distress evaluation,
 * message formatting permutations, boundary values, and dismissal duality.
 */
class PetDecayWorkerAdversarialTest {

    @Test
    fun testEvaluateDistress_all16BooleanPermutations() {
        val hungerOptions = listOf(0 to true, 1 to false, 4 to false)
        val wasteOptions = listOf(0 to false, 3 to false, 4 to true, 5 to true)
        val injuredOptions = listOf(false, true)
        val attentionOptions = listOf(false, true)

        for ((hunger, isHungry) in hungerOptions) {
            for ((waste, isPoopy) in wasteOptions) {
                for (isInjured in injuredOptions) {
                    for (attention in attentionOptions) {
                        val state = PetState(
                            hunger = hunger,
                            wasteCount = waste,
                            isInjured = isInjured,
                            attentionCallActive = attention
                        )
                        val expectedDistress = isHungry || isPoopy || isInjured || attention
                        val actualDistress = PetDecayWorker.evaluateDistress(state)

                        assertEquals(
                            "Distress mismatch for state (hunger=$hunger, waste=$waste, injured=$isInjured, attention=$attention)",
                            expectedDistress,
                            actualDistress
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testEvaluateDistress_exactBoundaryValues() {
        val baseState = PetState(
            hunger = 4,
            wasteCount = 0,
            isInjured = false,
            attentionCallActive = false
        )

        // Hunger boundaries: 0 vs 1
        assertTrue("hunger=0 must trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(hunger = 0)))
        assertFalse("hunger=1 must not trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(hunger = 1)))

        // Waste count boundaries: 3 vs 4 vs 5
        assertFalse("waste=3 must not trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(wasteCount = 3)))
        assertTrue("waste=4 must trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(wasteCount = 4)))
        assertTrue("waste=5 must trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(wasteCount = 5)))

        // Injury boundary: false vs true
        assertFalse("isInjured=false must not trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(isInjured = false)))
        assertTrue("isInjured=true must trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(isInjured = true)))

        // Attention call boundary: false vs true
        assertFalse("attentionCallActive=false must not trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(attentionCallActive = false)))
        assertTrue("attentionCallActive=true must trigger distress", PetDecayWorker.evaluateDistress(baseState.copy(attentionCallActive = true)))

        // Combined extremes
        val allExtremeState = PetState(
            hunger = 0,
            wasteCount = 4,
            isInjured = true,
            attentionCallActive = true
        )
        assertTrue("All active distress causes must trigger distress", PetDecayWorker.evaluateDistress(allExtremeState))

        val allNormalBoundaryState = PetState(
            hunger = 1,
            wasteCount = 3,
            isInjured = false,
            attentionCallActive = false
        )
        assertFalse("Boundary healthy state (hunger=1, waste=3) must not trigger distress", PetDecayWorker.evaluateDistress(allNormalBoundaryState))
    }

    @Test
    fun testFormatDistressMessage_exhaustiveCausesMatrix() {
        val species = "Agumon"

        // 1. Zero causes (fallback)
        val healthyState = PetState(hunger = 3, wasteCount = 1, isInjured = false, attentionCallActive = false)
        assertEquals("Agumon requires immediate attention!", PetDecayWorker.formatDistressMessage(species, healthyState))

        val attentionOnlyState = PetState(hunger = 2, wasteCount = 2, isInjured = false, attentionCallActive = true)
        assertEquals("Agumon requires immediate attention!", PetDecayWorker.formatDistressMessage(species, attentionOnlyState))

        // 2. Single causes
        val starvingOnly = PetState(hunger = 0, wasteCount = 0, isInjured = false)
        assertEquals("Agumon is starving!", PetDecayWorker.formatDistressMessage(species, starvingOnly))

        val dirtyOnly4 = PetState(hunger = 3, wasteCount = 4, isInjured = false)
        assertEquals("Agumon is surrounded by poop!", PetDecayWorker.formatDistressMessage(species, dirtyOnly4))

        val dirtyOnly5 = PetState(hunger = 3, wasteCount = 5, isInjured = false)
        assertEquals("Agumon is surrounded by poop!", PetDecayWorker.formatDistressMessage(species, dirtyOnly5))

        val injuredOnly = PetState(hunger = 3, wasteCount = 0, isInjured = true)
        assertEquals("Agumon is injured!", PetDecayWorker.formatDistressMessage(species, injuredOnly))

        // 3. Double causes (all 3 combinations)
        val starvingAndDirty = PetState(hunger = 0, wasteCount = 4, isInjured = false)
        assertEquals("Agumon is starving and surrounded by poop!", PetDecayWorker.formatDistressMessage(species, starvingAndDirty))

        val starvingAndInjured = PetState(hunger = 0, wasteCount = 2, isInjured = true)
        assertEquals("Agumon is starving and injured!", PetDecayWorker.formatDistressMessage(species, starvingAndInjured))

        val dirtyAndInjured = PetState(hunger = 2, wasteCount = 4, isInjured = true)
        assertEquals("Agumon is surrounded by poop and injured!", PetDecayWorker.formatDistressMessage(species, dirtyAndInjured))

        // 4. Triple causes
        val allThree = PetState(hunger = 0, wasteCount = 4, isInjured = true)
        assertEquals("Agumon is starving, surrounded by poop, and injured!", PetDecayWorker.formatDistressMessage(species, allThree))

        // 5. Adversarial species names
        val emptySpecies = ""
        assertEquals(" requires immediate attention!", PetDecayWorker.formatDistressMessage(emptySpecies, healthyState))
        assertEquals(" is starving!", PetDecayWorker.formatDistressMessage(emptySpecies, starvingOnly))

        val complexSpecies = "WarGreymon (Level 6)"
        assertEquals("WarGreymon (Level 6) is starving and injured!", PetDecayWorker.formatDistressMessage(complexSpecies, starvingAndInjured))
    }

    @Test
    fun testMathematicalDualityBetweenEvaluateDistressAndDismissal() {
        val hungerCases = listOf(0, 1, 4)
        val wasteCases = listOf(0, 3, 4)
        val injuredCases = listOf(false, true)
        val attentionCases = listOf(false, true)

        for (h in hungerCases) {
            for (w in wasteCases) {
                for (inj in injuredCases) {
                    for (att in attentionCases) {
                        val state = PetState(
                            hunger = h,
                            wasteCount = w,
                            isInjured = inj,
                            attentionCallActive = att
                        )
                        val inDistress = PetDecayWorker.evaluateDistress(state)
                        val canDismiss = NotificationQuickCareReceiver.shouldDismissNotification(state)

                        assertEquals(
                            "Duality violated: inDistress=$inDistress should be exact negation of canDismiss=$canDismiss for (h=$h, w=$w, inj=$inj, att=$att)",
                            !inDistress,
                            canDismiss
                        )
                    }
                }
            }
        }
    }
}
