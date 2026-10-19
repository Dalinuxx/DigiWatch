package com.digiwear.core.render

/**
 * Platform-independent 32-bit ARGB color palette for virtual pet dot-matrix sprites.
 * Usable across pure Kotlin JVM modules (shared-vpet-core) and Android platforms (Wear OS & Companion).
 */
data class SpritePalette(
    val primary: Int,
    val secondary: Int,
    val tertiary: Int,
    val eye: Int
) {
    companion object {
        fun rgb(r: Int, g: Int, b: Int): Int =
            (0xFF shl 24) or ((r and 0xFF) shl 16) or ((g and 0xFF) shl 8) or (b and 0xFF)
    }
}
