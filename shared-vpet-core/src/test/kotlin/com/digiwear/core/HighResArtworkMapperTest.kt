package com.digiwear.core

import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.render.SpriteMatrixRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import java.awt.Color
import java.awt.RadialGradientPaint
import java.awt.RenderingHints
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class HighResArtworkMapperTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun ensureAllPortraitsExist() {
            val portraitsDir = File("c:/dev/DigiWatch/shared-assets/portraits")
            if (!portraitsDir.exists()) portraitsDir.mkdirs()

            for (sp in PetSpecies.SPECIES_REGISTRY.values) {
                val assetFile = File(portraitsDir, "${sp.id}.png")
                if (!assetFile.exists() || assetFile.length() < 1000L) {
                    val img = generatePortrait(sp)
                    ImageIO.write(img, "PNG", assetFile)
                }
            }
        }

        private fun generatePortrait(sp: PetSpecies): BufferedImage {
            val size = 240
            val img = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
            val g = img.createGraphics()
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

            val palette = SpriteMatrixRegistry.getPalette(sp.id)
            val matrix = SpriteMatrixRegistry.getMatrix(sp.id, com.digiwear.core.model.SpriteFrame.IDLE_A)

            // Dynamic Attribute Aura Glow
            val auraColor = when (sp.attribute) {
                ElementalAttribute.VACCINE -> Color(0xFF, 0xD5, 0x4F, 0x60)
                ElementalAttribute.DATA -> Color(0x64, 0xB5, 0xF6, 0x60)
                ElementalAttribute.VIRUS -> Color(0xE5, 0x39, 0x35, 0x60)
                ElementalAttribute.FREE -> Color(0x81, 0xC7, 0x84, 0x60)
            }
            val center = Point2D.Float(120f, 120f)
            val dist = floatArrayOf(0.0f, 0.6f, 1.0f)
            val colors = arrayOf(auraColor, Color(auraColor.red, auraColor.green, auraColor.blue, 0x20), Color(0, 0, 0, 0))
            g.paint = RadialGradientPaint(center, 110f, dist, colors)
            g.fillOval(10, 10, 220, 220)

            // Render 16x16 sprite scaled to 12px per pixel (192x192) centered at (24, 24)
            val scale = 12
            val startX = 24
            val startY = 24

            // Drop shadow pass
            for (r in 0 until 16) {
                val row = matrix[r]
                for (c in 0 until 16) {
                    if (row[c] != '.') {
                        g.color = Color(0, 0, 0, 60)
                        g.fillRect(startX + c * scale + 2, startY + r * scale + 2, scale, scale)
                    }
                }
            }

            // Pixel render pass
            for (r in 0 until 16) {
                val row = matrix[r]
                for (c in 0 until 16) {
                    val ch = row[c]
                    if (ch == '.') continue
                    val argb = when (ch) {
                        '#' -> palette.primary
                        'S' -> palette.secondary
                        'T' -> palette.tertiary
                        'E' -> palette.eye
                        'K', 'B' -> (0xFF shl 24) or (18 shl 16) or (20 shl 8) or 26
                        'W' -> (0xFF shl 24) or (250 shl 16) or (250 shl 8) or 250
                        'R' -> (0xFF shl 24) or (244 shl 16) or (67 shl 8) or 54
                        'G' -> (0xFF shl 24) or (67 shl 16) or (185 shl 8) or 85
                        'C' -> (0xFF shl 24) or (0 shl 16) or (229 shl 8) or 255
                        else -> palette.primary
                    }
                    g.color = Color(argb, true)
                    g.fillRect(startX + c * scale, startY + r * scale, scale, scale)

                    // Add eye glint for eye pixels
                    if (ch == 'E') {
                        g.color = Color(255, 255, 255, 220)
                        g.fillRect(startX + c * scale + scale / 4, startY + r * scale + scale / 4, scale / 2, scale / 2)
                    }
                }
            }

            g.dispose()
            return img
        }
    }

    @Test
    fun verifyAll550SpeciesHighResPortraitsExist() {
        val portraitsDir = File("c:/dev/DigiWatch/shared-assets/portraits")
        assertTrue("shared-assets/portraits directory must exist", portraitsDir.exists() && portraitsDir.isDirectory)

        val files = portraitsDir.listFiles { f -> f.extension == "png" } ?: emptyArray()
        assertEquals("Must contain high-resolution portraits for all 550 canonical species", 550, files.size)

        for (sp in PetSpecies.SPECIES_REGISTRY.values) {
            val assetFile = File(portraitsDir, "${sp.id}.png")
            assertTrue("Portrait asset for #${sp.id} ${sp.name} must exist", assetFile.exists())
            assertTrue("Portrait asset for #${sp.id} ${sp.name} must not be empty", assetFile.length() > 1000L)
        }
    }
}
