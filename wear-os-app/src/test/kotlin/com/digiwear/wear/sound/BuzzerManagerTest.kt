package com.digiwear.wear.sound

import com.digiwear.core.model.SoundpackProfile
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite validating BuzzerManager configuration,
 * default state safety, SoundpackProfile assignment, and muted execution safety.
 */
class BuzzerManagerTest {

    @Test
    fun testDefaultStateIsMutedAndOriginal1997() {
        val buzzer = BuzzerManager()
        try {
            assertFalse("BuzzerManager must default to disabled/muted", buzzer.isEnabled)
            assertEquals("Default profile must be ORIGINAL_1997", SoundpackProfile.ORIGINAL_1997, buzzer.soundpack)
        } finally {
            buzzer.release()
        }
    }

    @Test
    fun testSoundpackProfileAssignment() {
        val buzzer = BuzzerManager()
        try {
            buzzer.soundpack = SoundpackProfile.DIGIVICE_1999
            assertEquals(SoundpackProfile.DIGIVICE_1999, buzzer.soundpack)

            buzzer.soundpack = SoundpackProfile.CHIPTUNE_RETRO
            assertEquals(SoundpackProfile.CHIPTUNE_RETRO, buzzer.soundpack)

            buzzer.soundpack = SoundpackProfile.ORIGINAL_1997
            assertEquals(SoundpackProfile.ORIGINAL_1997, buzzer.soundpack)
        } finally {
            buzzer.release()
        }
    }

    @Test
    fun testMutedCallsDoNotThrowOrInitializeHardware() = runBlocking {
        val buzzer = BuzzerManager().apply {
            isEnabled = false
        }
        try {
            for (profile in SoundpackProfile.entries) {
                buzzer.soundpack = profile
                buzzer.playButtonBeep()
                buzzer.playButtonBeepSuspend()
                buzzer.playAttentionBeep()
                buzzer.playAttentionBeepSuspend()
                buzzer.playAttackImpactTone()
                buzzer.playEncounterAlarm()
                buzzer.playCriticalCutInTone()
                buzzer.playQteClickTone()
                buzzer.playEvolutionGlitchTone()
                buzzer.playFlushTone()
                buzzer.playFlushToneSuspend()
                buzzer.playHealTone()
                buzzer.playVictoryFanfare()
                buzzer.playDefeatJingle()
                buzzer.playDeathSiren()
                buzzer.playEvolutionAlert()
                buzzer.playEvolutionAlertAsync()
            }
        } finally {
            buzzer.release()
        }
    }

    @Test
    fun testSuspendingToneExecutionSafety() = runBlocking {
        val buzzer = BuzzerManager().apply {
            isEnabled = false
            soundpack = SoundpackProfile.ORIGINAL_1997
        }
        try {
            buzzer.playButtonBeepSuspend()
            buzzer.playFlushToneSuspend()
            buzzer.playAttentionBeepSuspend()
        } finally {
            buzzer.release()
        }
    }

    @Test
    fun testEnablementFlagToggle() {
        val buzzer = BuzzerManager()
        try {
            assertFalse(buzzer.isEnabled)
            buzzer.isEnabled = true
            assertTrue(buzzer.isEnabled)
            buzzer.isEnabled = false
            assertFalse(buzzer.isEnabled)
        } finally {
            buzzer.release()
        }
    }
}
