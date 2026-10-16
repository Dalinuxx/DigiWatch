package com.digiwatch.core.model

import kotlinx.serialization.Serializable

/**
 * Visual background environments for the virtual pet device.
 * Enforces OLED-safe black background in ambient mode, with stylized retro cyber aesthetics in interactive mode.
 */
@Serializable
enum class BackgroundTheme(
    val id: String,
    val displayName: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val gridColorHex: Long,
    val description: String
) {
    CLASSIC_VPET_LCD(
        id = "classic_lcd",
        displayName = "Classic LCD",
        primaryColorHex = 0xFF8B956D, // Authentic 1997 olive green LCD
        secondaryColorHex = 0xFF7A845C,
        gridColorHex = 0xFF9CA67E,
        description = "Authentic olive-green dot-matrix LCD from original 1997 release"
    ),
    CYBER_FOREST(
        id = "cyber_forest",
        displayName = "Cyber Forest",
        primaryColorHex = 0xFF14241B,
        secondaryColorHex = 0xFF1B3828,
        gridColorHex = 0xFF2A523A,
        description = "Dense digital canopy with neon matrix rain"
    ),
    DIGITAL_OCEAN(
        id = "digital_ocean",
        displayName = "Digital Ocean",
        primaryColorHex = 0xFF0D1B2A,
        secondaryColorHex = 0xFF1B263B,
        gridColorHex = 0xFF415A77,
        description = "Deep cyan sea with animated packet waves"
    ),
    DATA_WASTELAND(
        id = "data_wasteland",
        displayName = "Data Wasteland",
        primaryColorHex = 0xFF2B1E16,
        secondaryColorHex = 0xFF3D2B1F,
        gridColorHex = 0xFF5C3D28,
        description = "Arid circuit badlands with rusted heat-haze"
    ),
    NEON_CORE_CITY(
        id = "neon_core_city",
        displayName = "Neon City",
        primaryColorHex = 0xFF1A0B2E,
        secondaryColorHex = 0xFF2D124D,
        gridColorHex = 0xFF6C227E,
        description = "Synthwave twilight city with violet perspective grid"
    ),
    DARK_NETWORK(
        id = "dark_network",
        displayName = "Dark Network",
        primaryColorHex = 0xFF1A1115,
        secondaryColorHex = 0xFF2D161F,
        gridColorHex = 0xFF4D1E2D,
        description = "Crimson-tinged abyssal zone corrupted by dark gears"
    ),
    GLITCH_VOID(
        id = "glitch_void",
        displayName = "Glitch Void",
        primaryColorHex = 0xFF151522,
        secondaryColorHex = 0xFF202035,
        gridColorHex = 0xFF3A3A5E,
        description = "Chromatic data breach with shifting scanlines"
    ),
    OLED_MINIMAL(
        id = "oled_minimal",
        displayName = "OLED Stealth",
        primaryColorHex = 0xFF000000,
        secondaryColorHex = 0xFF0A0A0A,
        gridColorHex = 0xFF141414,
        description = "Pitch black high-efficiency minimal display"
    );

    companion object {
        fun fromId(id: String?): BackgroundTheme {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: CLASSIC_VPET_LCD
        }
    }
}
