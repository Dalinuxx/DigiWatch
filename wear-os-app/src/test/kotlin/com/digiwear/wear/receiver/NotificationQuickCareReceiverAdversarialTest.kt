package com.digiwear.wear.receiver

import android.content.ContextWrapper
import android.content.Intent
import com.digiwear.core.engine.PetSimulationEngine
import com.digiwear.core.model.PetState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Adversarial test harness stress-testing NotificationQuickCareReceiver against
 * tampered actions, null/malformed intents, and the complete dismissal matrix.
 */
class NotificationQuickCareReceiverAdversarialTest {

    @Test
    fun testExactActionMatching() {
        assertEquals(
            QuickCareAction.FEED,
            QuickCareAction.fromAction("com.digiwear.wear.action.QUICK_FEED")
        )
        assertEquals(
            QuickCareAction.CLEAN,
            QuickCareAction.fromAction("com.digiwear.wear.action.QUICK_CLEAN")
        )
    }

    @Test
    fun testTamperedAndAdversarialActionsMapToUnknown() {
        val adversarialActions = listOf(
            null,
            "",
            " ",
            "\t",
            "\n",
            "com.digiwear.wear.action.QUICK_FEED ",
            " com.digiwear.wear.action.QUICK_FEED",
            "COM.DIGIWEAR.WEAR.ACTION.QUICK_FEED",
            "com.digiwear.wear.action.quick_feed",
            "com.digiwear.wear.action.QUICK_FEED\u0000",
            "com.digiwear.wear.action.QUICK_FEED; DROP TABLE pet_state;",
            "com.digiwear.wear.action.QUICK_FEED_EXTRA",
            "com.digiwear.wear.action.QUICK_CLEAN_EXTRA",
            "com.digiwear.wear.action.FEED",
            "com.digiwear.wear.action.CLEAN",
            "FEED",
            "CLEAN",
            "ACTION_QUICK_FEED",
            "ACTION_QUICK_CLEAN",
            "android.intent.action.BOOT_COMPLETED",
            "android.intent.action.VIEW",
            "a".repeat(1000)
        )

        for (action in adversarialActions) {
            assertEquals(
                "Adversarial action '$action' must map to QuickCareAction.UNKNOWN",
                QuickCareAction.UNKNOWN,
                QuickCareAction.fromAction(action)
            )
        }
    }

    @Test
    fun testOnReceiveResilienceUnderTamperedAndNullIntents() {
        val receiver = NotificationQuickCareReceiver()
        val dummyContext = DummyContext()

        // 1. Null intent
        receiver.onReceive(dummyContext, null)

        // 2. Intent with null action
        receiver.onReceive(dummyContext, Intent())

        // 3. Intent with empty action
        receiver.onReceive(dummyContext, Intent(""))

        // 4. Intent with tampered actions
        receiver.onReceive(dummyContext, Intent("com.digiwear.wear.action.QUICK_FEED "))
        receiver.onReceive(dummyContext, Intent("FEED"))
        receiver.onReceive(dummyContext, Intent("UNKNOWN_ACTION_9999"))
        receiver.onReceive(dummyContext, Intent("a".repeat(500)))

        // None of these should throw uncaught exceptions
    }

    @Test
    fun testDismissalMatrix_exhaustive16States() {
        // True = distressed factor active; False = factor cleared
        val booleanStates = listOf(false, true)

        var countTrueDismissal = 0
        var countFalseDismissal = 0

        for (isHungry in booleanStates) {
            for (isDirty in booleanStates) {
                for (isInjured in booleanStates) {
                    for (isAttention in booleanStates) {
                        val state = PetState(
                            hunger = if (isHungry) 0 else 4,
                            wasteCount = if (isDirty) 4 else 0,
                            isInjured = isInjured,
                            attentionCallActive = isAttention
                        )

                        val canDismiss = NotificationQuickCareReceiver.shouldDismissNotification(state)

                        val expectedDismiss = !isHungry && !isDirty && !isInjured && !isAttention
                        assertEquals(
                            "Dismissal error for state (hungry=$isHungry, dirty=$isDirty, injured=$isInjured, attention=$isAttention)",
                            expectedDismiss,
                            canDismiss
                        )

                        if (canDismiss) countTrueDismissal++ else countFalseDismissal++
                    }
                }
            }
        }

        // Out of 16 states, EXACTLY 1 state should dismiss (all 4 clear) and 15 must NOT dismiss.
        assertEquals("Exactly 1 permutation should permit notification dismissal", 1, countTrueDismissal)
        assertEquals("Exactly 15 permutations must suppress notification dismissal", 15, countFalseDismissal)
    }

    @Test
    fun testStepwiseDistressResolutionSequence() {
        val now = 2000000L
        // Pet begins with multiple distress causes: starving and dirty
        var state = PetState(
            hunger = 0,
            wasteCount = 4,
            attentionCallActive = true,
            isInjured = false
        )

        // Before any action: cannot dismiss
        assertFalse("Initial state cannot dismiss notification", NotificationQuickCareReceiver.shouldDismissNotification(state))

        // Step 1: User feeds meal
        state = PetSimulationEngine.feedMeal(state, now)
        assertEquals(1, state.hunger)
        assertFalse("attentionCallActive resolved by feed", state.attentionCallActive)
        assertEquals(4, state.wasteCount) // Waste still present!
        assertFalse("Partially resolved state (still poopy) must NOT dismiss notification", NotificationQuickCareReceiver.shouldDismissNotification(state))

        // Step 2: User cleans waste
        state = PetSimulationEngine.cleanWaste(state)
        assertEquals(0, state.wasteCount)
        assertEquals(1, state.hunger)
        assertFalse(state.isInjured)
        assertFalse(state.attentionCallActive)

        // Now all distress factors are eliminated -> must dismiss
        assertTrue("Fully resolved state MUST dismiss notification", NotificationQuickCareReceiver.shouldDismissNotification(state))
    }

    private class DummyContext : ContextWrapper(null)
}
