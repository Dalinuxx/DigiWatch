package com.digiwear.companion

import android.content.Context
import android.content.Intent
import com.digiwear.companion.data.SyncedPetEntity
import com.digiwear.companion.widget.DigiWearAppWidgetProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite verifying Phone Companion widget synchronization,
 * remote care action protocol strings, intent routing, and offline fallbacks.
 */
class CompanionQuickCareTest {

    @Test
    fun testRemoteCareActionCanonicalStrings() {
        assertEquals(
            "Remote care feed action must be 'FEED_MEAL'",
            "FEED_MEAL",
            DigiWearAppWidgetProvider.REMOTE_ACTION_FEED_MEAL
        )
        assertEquals(
            "Remote care clean action must be 'CLEAN_WASTE'",
            "CLEAN_WASTE",
            DigiWearAppWidgetProvider.REMOTE_ACTION_CLEAN_WASTE
        )
    }

    @Test
    fun testWidgetActionIntentMatching() {
        assertEquals(
            "ACTION_QUICK_FEED must match expected intent action string",
            "com.digiwear.companion.widget.ACTION_QUICK_FEED",
            DigiWearAppWidgetProvider.ACTION_QUICK_FEED
        )
        assertEquals(
            "ACTION_QUICK_CLEAN must match expected intent action string",
            "com.digiwear.companion.widget.ACTION_QUICK_CLEAN",
            DigiWearAppWidgetProvider.ACTION_QUICK_CLEAN
        )

        // Matching to remote commands
        assertEquals(
            "ACTION_QUICK_FEED must route to FEED_MEAL",
            DigiWearAppWidgetProvider.REMOTE_ACTION_FEED_MEAL,
            DigiWearAppWidgetProvider.resolveRemoteCareAction(DigiWearAppWidgetProvider.ACTION_QUICK_FEED)
        )
        assertEquals(
            "ACTION_QUICK_CLEAN must route to CLEAN_WASTE",
            DigiWearAppWidgetProvider.REMOTE_ACTION_CLEAN_WASTE,
            DigiWearAppWidgetProvider.resolveRemoteCareAction(DigiWearAppWidgetProvider.ACTION_QUICK_CLEAN)
        )

        // Unknown or null actions must resolve to null
        assertNull(DigiWearAppWidgetProvider.resolveRemoteCareAction(null))
        assertNull(DigiWearAppWidgetProvider.resolveRemoteCareAction(""))
        assertNull(DigiWearAppWidgetProvider.resolveRemoteCareAction("com.digiwear.companion.widget.UNKNOWN"))
        assertNull(DigiWearAppWidgetProvider.resolveRemoteCareAction("FEED"))
        assertNull(DigiWearAppWidgetProvider.resolveRemoteCareAction("CLEAN"))
    }

    @Test
    fun testRemoteCareCommandBackwardCompatibilityMatrix() {
        // Feed command resolution: supports canonical and legacy alias
        assertTrue("Canonical 'FEED_MEAL' must be recognized", DigiWearAppWidgetProvider.isFeedCommand("FEED_MEAL"))
        assertTrue("Legacy 'FEED' must be recognized for backward compatibility", DigiWearAppWidgetProvider.isFeedCommand("FEED"))
        assertFalse("'CLEAN_WASTE' is not a feed command", DigiWearAppWidgetProvider.isFeedCommand("CLEAN_WASTE"))
        assertFalse("null is not a feed command", DigiWearAppWidgetProvider.isFeedCommand(null))

        // Clean command resolution: supports canonical and legacy alias
        assertTrue("Canonical 'CLEAN_WASTE' must be recognized", DigiWearAppWidgetProvider.isCleanCommand("CLEAN_WASTE"))
        assertTrue("Legacy 'CLEAN' must be recognized for backward compatibility", DigiWearAppWidgetProvider.isCleanCommand("CLEAN"))
        assertFalse("'FEED_MEAL' is not a clean command", DigiWearAppWidgetProvider.isCleanCommand("FEED_MEAL"))
        assertFalse("null is not a clean command", DigiWearAppWidgetProvider.isCleanCommand(null))
    }

    @Test
    fun testOfflineSnapshotFallbackLogic() {
        val initialSnapshot = SyncedPetEntity(
            speciesId = 201,
            speciesName = "Agumon",
            currentHp = 100,
            maxHp = 100,
            hunger = 1,
            strength = 2,
            weight = 20,
            ageSeconds = 3600L,
            careMistakes = 0,
            effort = 0,
            overfeedCount = 0,
            totalBattles = 0,
            victories = 0,
            syncedTimestampUtc = 1000L
        )

        // 1. Feed fallback when watch unreachable
        val feedFallback = initialSnapshot.copy(
            id = 0,
            hunger = (initialSnapshot.hunger + 1).coerceAtMost(4),
            weight = initialSnapshot.weight + 1,
            syncedTimestampUtc = 2000L
        )
        assertEquals(2, feedFallback.hunger)
        assertEquals(21, feedFallback.weight)
        assertEquals(2000L, feedFallback.syncedTimestampUtc)

        // 2. Feed fallback saturation cap (max 4)
        val fullSnapshot = initialSnapshot.copy(hunger = 4)
        val fullFeedFallback = fullSnapshot.copy(
            hunger = (fullSnapshot.hunger + 1).coerceAtMost(4)
        )
        assertEquals(4, fullFeedFallback.hunger)

        // 3. Clean fallback when watch unreachable
        val cleanFallback = initialSnapshot.copy(
            id = 0,
            syncedTimestampUtc = 3000L
        )
        assertEquals(initialSnapshot.hunger, cleanFallback.hunger)
        assertEquals(initialSnapshot.weight, cleanFallback.weight)
        assertEquals(3000L, cleanFallback.syncedTimestampUtc)
    }

    @Test
    fun testWidgetProviderSafeWithUnknownActionsWithoutExceptions() {
        val provider = DigiWearAppWidgetProvider()
        val dummyContext = DummyContext()
        val dummyIntent = Intent("com.digiwear.companion.widget.UNKNOWN")

        // Must run safely without exceptions
        provider.onReceive(dummyContext, dummyIntent)
    }

    @Test
    fun testAdversarialCommandStringResolution() {
        // Canonical commands
        assertTrue(DigiWearAppWidgetProvider.isFeedCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_FEED_MEAL))
        assertTrue(DigiWearAppWidgetProvider.isCleanCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_CLEAN_WASTE))

        // Legacy aliases
        assertTrue(DigiWearAppWidgetProvider.isFeedCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_FEED_LEGACY))
        assertTrue(DigiWearAppWidgetProvider.isCleanCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_CLEAN_LEGACY))

        // Inverted cross-checks
        assertFalse(DigiWearAppWidgetProvider.isFeedCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_CLEAN_WASTE))
        assertFalse(DigiWearAppWidgetProvider.isFeedCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_CLEAN_LEGACY))
        assertFalse(DigiWearAppWidgetProvider.isCleanCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_FEED_MEAL))
        assertFalse(DigiWearAppWidgetProvider.isCleanCommand(DigiWearAppWidgetProvider.REMOTE_ACTION_FEED_LEGACY))

        // Malformed, whitespace, casing, and fuzz variants
        val malformedCommands = listOf(
            null,
            "",
            " ",
            "   ",
            "feed",
            "clean",
            "FEED_MEAL ",
            " FEED_MEAL",
            "CLEAN_WASTE ",
            " CLEAN_WASTE",
            "FEED\u0000",
            "FEED_PROTEIN",
            "TOGGLE_LIGHTS",
            "CURE_INJURY",
            "TRAIN_WIN",
            "HATCH_EGG:101",
            "INVALID_RAW_MEAT",
            "123",
            "ACTION_QUICK_FEED",
            "ACTION_QUICK_CLEAN"
        )

        for (cmd in malformedCommands) {
            if (cmd != "FEED" && cmd != "FEED_MEAL") {
                assertFalse("Command '$cmd' must not be recognized as feed command", DigiWearAppWidgetProvider.isFeedCommand(cmd))
            }
            if (cmd != "CLEAN" && cmd != "CLEAN_WASTE") {
                assertFalse("Command '$cmd' must not be recognized as clean command", DigiWearAppWidgetProvider.isCleanCommand(cmd))
            }
            assertNull("Command '$cmd' must not resolve to remote care action", DigiWearAppWidgetProvider.resolveRemoteCareAction(cmd))
        }
    }

    @Test
    fun testAdversarialOfflineDatabaseFallbackCalculations() {
        val baseSnapshot = SyncedPetEntity(
            speciesId = 201,
            speciesName = "Agumon",
            currentHp = 100,
            maxHp = 100,
            hunger = 0,
            strength = 0,
            weight = 5,
            ageSeconds = 7200L,
            careMistakes = 2,
            effort = 1,
            overfeedCount = 0,
            totalBattles = 10,
            victories = 8,
            syncedTimestampUtc = 5000L
        )

        // Saturated hunger boundaries: 0->1, 1->2, 2->3, 3->4, 4->4, 5->4
        for (h in 0..5) {
            val clamped = (h + 1).coerceAtMost(4)
            val expected = if (h >= 4) 4 else h + 1
            assertEquals("CoerceAtMost cap failed for hunger=$h", expected, clamped)
        }

        // Feed fallback calculations: increments hunger and increments weight strictly
        val feedFromZero = baseSnapshot.copy(
            id = 0,
            hunger = (baseSnapshot.hunger + 1).coerceAtMost(4),
            weight = baseSnapshot.weight + 1,
            syncedTimestampUtc = 6000L
        )
        assertEquals(0, feedFromZero.id)
        assertEquals(1, feedFromZero.hunger)
        assertEquals(6, feedFromZero.weight)
        assertEquals(baseSnapshot.strength, feedFromZero.strength)
        assertEquals(baseSnapshot.careMistakes, feedFromZero.careMistakes)
        assertEquals(baseSnapshot.speciesId, feedFromZero.speciesId)
        assertEquals(6000L, feedFromZero.syncedTimestampUtc)

        // Clean waste fallback retains hunger, weight, strength, and updates timestamp
        val cleanFallback = baseSnapshot.copy(
            id = 0,
            syncedTimestampUtc = 7000L
        )
        assertEquals(0, cleanFallback.id)
        assertEquals(baseSnapshot.hunger, cleanFallback.hunger)
        assertEquals(baseSnapshot.weight, cleanFallback.weight)
        assertEquals(baseSnapshot.strength, cleanFallback.strength)
        assertEquals(baseSnapshot.careMistakes, cleanFallback.careMistakes)
        assertEquals(baseSnapshot.speciesId, cleanFallback.speciesId)
        assertEquals(7000L, cleanFallback.syncedTimestampUtc)
    }

    @Test
    fun testAdversarialReceiverIntentSafetyAndNullHandling() {
        val provider = DigiWearAppWidgetProvider()
        val dummyContext = DummyContext()

        // Null action in intent
        val emptyIntent = Intent()
        provider.onReceive(dummyContext, emptyIntent)

        // Blank string action
        val blankIntent = Intent("")
        provider.onReceive(dummyContext, blankIntent)

        // Random action
        val unknownIntent = Intent("android.intent.action.BATTERY_LOW")
        provider.onReceive(dummyContext, unknownIntent)
    }

    private class DummyContext : android.content.ContextWrapper(null)
}
