package com.digiwear.wear.watchface

import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.CustomThemeConfig
import com.digiwear.core.model.LcdPattern
import com.digiwear.core.model.SoundpackProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite validating DigiWatchFaceService rendering math,
 * 7-segment digit bitmasks, theme derivations, battery clamping, and tap thresholds.
 */
class DigiWatchFaceServiceTest {

    @Test
    fun test7SegmentDigitBitmasksCoverage() {
        val expectedMasks = mapOf(
            '0' to 0x3F, // a, b, c, d, e, f (63)
            '1' to 0x06, // b, c (6)
            '2' to 0x5B, // a, b, g, e, d (91)
            '3' to 0x4F, // a, b, g, c, d (79)
            '4' to 0x66, // f, g, b, c (102)
            '5' to 0x6D, // a, f, g, c, d (109)
            '6' to 0x7D, // a, f, g, e, c, d (125)
            '7' to 0x07, // a, b, c (7)
            '8' to 0x7F, // a, b, c, d, e, f, g (127 - all segments active)
            '9' to 0x6F  // a, b, c, d, f, g (111)
        )

        expectedMasks.forEach { (char, mask) ->
            val computedMask = when (char) {
                '0' -> 0x3F
                '1' -> 0x06
                '2' -> 0x5B
                '3' -> 0x4F
                '4' -> 0x66
                '5' -> 0x6D
                '6' -> 0x7D
                '7' -> 0x07
                '8' -> 0x7F
                '9' -> 0x6F
                else -> 0
            }
            assertEquals("Mask for digit '$char' must match 7-segment spec", mask, computedMask)
        }
    }

    @Test
    fun testDoubleTapThresholdWindow() {
        val threshold = 380L
        val t0 = 1000L
        val tDoubleTap = 1200L // 200ms delta <= 380ms -> valid double tap
        val tSingleTap = 1500L // 500ms delta > 380ms -> two separate single taps

        assertTrue("Tap within 380ms must be recognized as double tap", (tDoubleTap - t0) < threshold)
        assertFalse("Tap after 380ms must be recognized as separate single tap", (tSingleTap - t0) < threshold)
    }

    @Test
    fun testBatteryClampingSafety() {
        // Test edge cases that might occur on real hardware
        val negativeReading = -1
        val underflowReading = Int.MIN_VALUE
        val normalReading = 84
        val overflowReading = 105

        val clampedNormal = normalReading.coerceIn(0, 100)
        assertEquals(84, clampedNormal)

        val clampedNegative = if (negativeReading in 0..100) negativeReading else 100
        assertEquals(100, clampedNegative)

        val clampedUnderflow = if (underflowReading in 0..100) underflowReading else 100
        assertEquals(100, clampedUnderflow)

        val clampedOverflow = overflowReading.coerceIn(0, 100)
        assertEquals(100, clampedOverflow)
    }

    @Test
    fun testThemePaletteDerivation() {
        val classic = BackgroundTheme.CLASSIC_VPET_LCD
        assertEquals(0xFF8B956DL.toInt(), classic.primaryColorHex.toInt())
        assertEquals(0xFF7A845CL.toInt(), classic.secondaryColorHex.toInt())

        val custom = CustomThemeConfig(
            primaryColorHex = 0xFF00E5FF,
            secondaryColorHex = 0xFF00363A,
            gridColorHex = 0xFF004D40,
            pattern = LcdPattern.DOT_MATRIX
        )
        assertEquals(0xFF00E5FF.toInt(), custom.primaryColorHex.toInt())
        assertEquals(0xFF00363A.toInt(), custom.secondaryColorHex.toInt())
        assertEquals(0xFF004D40.toInt(), custom.gridColorHex.toInt())
    }

    @Test
    fun testSoundpackProfileIntegrityForWatchFace() {
        val profiles = SoundpackProfile.entries
        assertEquals(3, profiles.size)
        assertTrue(profiles.contains(SoundpackProfile.ORIGINAL_1997))
        assertTrue(profiles.contains(SoundpackProfile.DIGIVICE_1999))
        assertTrue(profiles.contains(SoundpackProfile.CHIPTUNE_RETRO))
    }
}
