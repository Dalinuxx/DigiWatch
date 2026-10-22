package com.digiwear.core.model

import kotlinx.serialization.Serializable

/**
 * Procedural LCD dot-matrix pattern types for custom themes.
 */
@Serializable
enum class LcdPattern(val id: String, val displayName: String) {
    DOT_MATRIX("DOT_MATRIX", "Dot Matrix"),
    GRID("GRID", "Digital Grid"),
    SCANLINES("SCANLINES", "Scanlines"),
    OLED("OLED", "OLED Solid");

    companion object {
        fun fromId(id: String?): LcdPattern {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: DOT_MATRIX
        }
    }
}

/**
 * Dynamic configuration for custom LCD backlight colors and procedural pattern matrices.
 * Enables user-tailored retro handheld display aesthetics.
 */
@Serializable
data class CustomThemeConfig(
    val primaryColorHex: Long = 0xFF8B956D,   // Backlight color
    val secondaryColorHex: Long = 0xFF7A845C, // Shading / scanline secondary color
    val gridColorHex: Long = 0xFF9CA67E,      // Matrix dots / grid wire color
    val pattern: LcdPattern = LcdPattern.DOT_MATRIX
) {
    /**
     * Serializes this custom theme configuration into a compact URI-like identifier
     * suitable for watch datalayer settings sync: `custom_theme:<primaryHex>_<secondaryHex>_<gridHex>_<pattern>`
     */
    fun toThemeId(): String {
        val primHex = primaryColorHex.toString(16)
        val secHex = secondaryColorHex.toString(16)
        val gridHex = gridColorHex.toString(16)
        return "$PREFIX${primHex}_${secHex}_${gridHex}_${pattern.id}"
    }

    companion object {
        const val PREFIX = "custom_theme:"
        const val WALLPAPER_THEME_ID = "custom_wallpaper"
        const val MATRIX_THEME_ID = "custom_matrix"

        fun isCustomTheme(themeId: String?): Boolean {
            if (themeId == null) return false
            return themeId.startsWith(PREFIX, ignoreCase = true) ||
                    themeId.equals(WALLPAPER_THEME_ID, ignoreCase = true) ||
                    themeId.equals(MATRIX_THEME_ID, ignoreCase = true)
        }

        fun parse(themeId: String?): CustomThemeConfig? {
            if (themeId == null || !themeId.startsWith(PREFIX, ignoreCase = true)) return null
            val payload = themeId.substring(PREFIX.length)
            val parts = payload.split("_")
            if (parts.size >= 4) {
                val prim = parts[0].toLongOrNull(16) ?: return null
                val sec = parts[1].toLongOrNull(16) ?: return null
                val grid = parts[2].toLongOrNull(16) ?: return null
                val pat = LcdPattern.fromId(parts[3])
                return CustomThemeConfig(
                    primaryColorHex = prim and 0xFFFFFFFFL,
                    secondaryColorHex = sec and 0xFFFFFFFFL,
                    gridColorHex = grid and 0xFFFFFFFFL,
                    pattern = pat
                )
            }
            return null
        }

        /**
         * Helper to compute an aesthetically pleasing secondary shade given primary and grid colors.
         */
        fun deriveSecondaryColorHex(primaryHex: Long, gridHex: Long): Long {
            val a = (primaryHex shr 24) and 0xFF
            val r = ((((primaryHex shr 16) and 0xFF) * 0.82f) + (((gridHex shr 16) and 0xFF) * 0.18f)).toLong().coerceIn(0, 255)
            val g = ((((primaryHex shr 8) and 0xFF) * 0.82f) + (((gridHex shr 8) and 0xFF) * 0.18f)).toLong().coerceIn(0, 255)
            val b = (((primaryHex and 0xFF) * 0.82f) + ((gridHex and 0xFF) * 0.18f)).toLong().coerceIn(0, 255)
            return ((a shl 24) or (r shl 16) or (g shl 8) or b) and 0xFFFFFFFFL
        }
    }
}
