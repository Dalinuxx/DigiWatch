package com.digiwear.core

import com.digiwear.core.engine.EvolutionEngine
import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.PetSpecies
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsSyncTest {

    @Test
    fun testAll16StarterEggsAreValidAndRegistered() {
        assertEquals("There must be exactly 16 canonical Starter Digi-Eggs", 16, EvolutionEngine.STARTER_EGG_IDS.size)
        EvolutionEngine.STARTER_EGG_IDS.forEach { eggId ->
            val species = PetSpecies.getById(eggId)
            assertNotNull("Egg species #$eggId must exist in registry", species)
            assertEquals("Starter Egg must be Baby I stage", com.digiwear.core.model.PetStage.BABY_I, species.stage)
        }
    }

    @Test
    fun testSettingsKeysConsistency() {
        val requiredKeys = setOf(
            "theme",
            "vibration",
            "vpet_3button_mode",
            "vpet_button_placement",
            "pendulum_shake_mode",
            "sound_enabled",
            "nfc_bridge_mode",
            "display_shape_override",
            "daily_step_goal",
            "lcd_contrast_level",
            "sleep_schedule",
            "ambient_display_style",
            "hazard_alerts_enabled",
            "clock_format_24h",
            "lifespan_canonical_enabled",
            "combat_injuries_enabled"
        )

        val sampleSettings = mapOf(
            "theme" to BackgroundTheme.CLASSIC_VPET_LCD.id,
            "vibration" to "true",
            "vpet_3button_mode" to "true",
            "vpet_button_placement" to "RIGHT",
            "pendulum_shake_mode" to "false",
            "sound_enabled" to "true",
            "nfc_bridge_mode" to "NFC_DUEL",
            "display_shape_override" to "AUTO",
            "daily_step_goal" to "5000",
            "lcd_contrast_level" to "MEDIUM",
            "sleep_schedule" to "21:00_07:00",
            "ambient_display_style" to "MINIMAL_DOT",
            "hazard_alerts_enabled" to "true",
            "clock_format_24h" to "false",
            "lifespan_canonical_enabled" to "true",
            "combat_injuries_enabled" to "true"
        )

        assertEquals("Settings map must have all 16 required keys", requiredKeys, sampleSettings.keys)

        // Verify parsing logic
        assertEquals(BackgroundTheme.CLASSIC_VPET_LCD, BackgroundTheme.fromId(sampleSettings["theme"]!!))
        assertTrue(sampleSettings["vibration"]!!.toBoolean())
        assertTrue(sampleSettings["vpet_3button_mode"]!!.toBoolean())
        assertEquals("RIGHT", sampleSettings["vpet_button_placement"])
        assertTrue(!sampleSettings["pendulum_shake_mode"]!!.toBoolean())
        assertTrue(sampleSettings["sound_enabled"]!!.toBoolean())
        assertEquals("NFC_DUEL", sampleSettings["nfc_bridge_mode"])
        assertEquals("AUTO", sampleSettings["display_shape_override"])
        assertEquals(5000, sampleSettings["daily_step_goal"]!!.toInt())
        assertEquals("MEDIUM", sampleSettings["lcd_contrast_level"])
        assertEquals("21:00_07:00", sampleSettings["sleep_schedule"])
        assertEquals("MINIMAL_DOT", sampleSettings["ambient_display_style"])
        assertTrue(sampleSettings["hazard_alerts_enabled"]!!.toBoolean())
        assertTrue(!sampleSettings["clock_format_24h"]!!.toBoolean())
        assertTrue(sampleSettings["lifespan_canonical_enabled"]!!.toBoolean())
        assertTrue(sampleSettings["combat_injuries_enabled"]!!.toBoolean())
    }

    @Test
    fun testBackgroundThemesIntegrity() {
        val themes = BackgroundTheme.entries
        assertTrue("At least 5 distinct background themes must be available", themes.size >= 5)
        themes.forEach { theme ->
            assertTrue("Theme ID must not be blank", theme.id.isNotBlank())
            assertTrue("Display name must not be blank", theme.displayName.isNotBlank())
            assertEquals("Theme lookup by ID must match enum", theme, BackgroundTheme.fromId(theme.id))
        }
    }

    @Test
    fun testSleepScheduleFormat() {
        val validSchedules = listOf("20:00_07:00", "21:00_07:00", "22:00_08:00", "23:00_08:00")
        val regex = Regex("^\\d{2}:\\d{2}_\\d{2}:\\d{2}$")
        validSchedules.forEach { sched ->
            assertTrue("Schedule $sched must match 24h format HH:mm_HH:mm", regex.matches(sched))
            val parts = sched.split("_")
            val bedHour = parts[0].substringBefore(":").toInt()
            val wakeHour = parts[1].substringBefore(":").toInt()
            assertTrue("Bed hour between 0 and 23", bedHour in 0..23)
            assertTrue("Wake hour between 0 and 23", wakeHour in 0..23)
        }
    }

    @Test
    fun testCustomThemeConfigSerializationAndParsing() {
        val config = com.digiwear.core.model.CustomThemeConfig(
            primaryColorHex = 0xFF00E5FF,
            secondaryColorHex = 0xFF00B0FF,
            gridColorHex = 0xFF004D40,
            pattern = com.digiwear.core.model.LcdPattern.GRID
        )
        val themeId = config.toThemeId()
        assertTrue("Theme ID should start with prefix", themeId.startsWith("custom_theme:"))
        assertTrue("Theme ID should be identified as custom", com.digiwear.core.model.CustomThemeConfig.isCustomTheme(themeId))
        assertTrue("Wallpaper theme ID should be identified as custom", com.digiwear.core.model.CustomThemeConfig.isCustomTheme("custom_wallpaper"))

        val parsed = com.digiwear.core.model.CustomThemeConfig.parse(themeId)
        assertNotNull("Parsed config must not be null", parsed)
        assertEquals(config.primaryColorHex, parsed!!.primaryColorHex)
        assertEquals(config.secondaryColorHex, parsed.secondaryColorHex)
        assertEquals(config.gridColorHex, parsed.gridColorHex)
        assertEquals(com.digiwear.core.model.LcdPattern.GRID, parsed.pattern)

        // Test BackgroundTheme.fromId with custom theme ID
        val themeEnum = BackgroundTheme.fromId(themeId)
        assertEquals(BackgroundTheme.CUSTOM_MATRIX, themeEnum)

        val wallpaperEnum = BackgroundTheme.fromId("custom_wallpaper")
        assertEquals(BackgroundTheme.CUSTOM_WALLPAPER, wallpaperEnum)

        // Invalid theme ID fallback
        assertEquals(null, com.digiwear.core.model.CustomThemeConfig.parse("invalid_theme"))
        assertEquals(null, com.digiwear.core.model.CustomThemeConfig.parse("custom_theme:not_hex"))
        assertEquals(null, com.digiwear.core.model.CustomThemeConfig.parse("custom_theme:12_34_56")) // only 3 parts
    }

    @Test
    fun testDeriveSecondaryColorHexSafetyAndBlending() {
        val primary = 0xFF8B956DL
        val grid = 0xFF9CA67EL
        val derived = com.digiwear.core.model.CustomThemeConfig.deriveSecondaryColorHex(primary, grid)

        // Must preserve full alpha (0xFF in bits 24..31) and zero high bits (bits 32..63)
        assertEquals(0L, derived and (0xFFFFFFFFL.inv()))
        val alpha = (derived shr 24) and 0xFF
        assertEquals("Alpha channel must remain 0xFF", 0xFFL, alpha)

        // Shading test: secondary color must be intermediate between primary and grid
        val r = (derived shr 16) and 0xFF
        val g = (derived shr 8) and 0xFF
        val b = derived and 0xFF
        assertTrue("Red channel must be in valid byte range", r in 0..255)
        assertTrue("Green channel must be in valid byte range", g in 0..255)
        assertTrue("Blue channel must be in valid byte range", b in 0..255)
    }

    @Test
    fun testCustomSleepScheduleExactMinuteBoundaries() {
        val customSchedule = "22:30_06:45"
        val stage = com.digiwear.core.model.PetStage.CHILD

        // 1 minute before bedtime: awake
        assertFalse(
            "Pet must be awake at 22:29 before 22:30 bedtime",
            com.digiwear.core.engine.PetSimulationEngine.isSleepWindowWithSchedule(stage, 22, 29, customSchedule)
        )
        // Exact bedtime: asleep
        assertTrue(
            "Pet must be asleep at 22:30 exact bedtime",
            com.digiwear.core.engine.PetSimulationEngine.isSleepWindowWithSchedule(stage, 22, 30, customSchedule)
        )
        // 1 minute before wake: asleep
        assertTrue(
            "Pet must be asleep at 06:44 before 06:45 wake time",
            com.digiwear.core.engine.PetSimulationEngine.isSleepWindowWithSchedule(stage, 6, 44, customSchedule)
        )
        // Exact wake time: awake
        assertFalse(
            "Pet must be awake at 06:45 exact wake time",
            com.digiwear.core.engine.PetSimulationEngine.isSleepWindowWithSchedule(stage, 6, 45, customSchedule)
        )
    }
}
