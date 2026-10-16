package com.digiwatch.companion.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.digiwatch.core.model.SpriteFrame

/**
 * Authentic 64x64 pixel art sprite generator supporting all 205 authentic virtual pet species.
 * Covers Digital Monster Ver. 1-5, Ver. 20th, Pendulum, Digimon 0nl1ne / D-Com, Royal Knights, and Demon Lords.
 * Provides authentic 24-bit color frames for the Companion Mobile App.
 */
object PixelArtMatrices {

    private val cache = java.util.concurrent.ConcurrentHashMap<Int, ImageBitmap>()
    private val androidBitmapCache = java.util.concurrent.ConcurrentHashMap<Int, Bitmap>()

    fun getFrameBitmap(speciesId: Int, frame: SpriteFrame, isAmbient: Boolean = false): ImageBitmap {
        val key = (speciesId shl 5) or (frame.frameIndex shl 1) or (if (isAmbient) 1 else 0)
        return cache.getOrPut(key) {
            getAndroidBitmap(speciesId, frame, isAmbient).asImageBitmap()
        }
    }

    fun getAndroidBitmap(speciesId: Int, frame: SpriteFrame = SpriteFrame.IDLE_A, isAmbient: Boolean = false): Bitmap {
        val key = (speciesId shl 5) or (frame.frameIndex shl 1) or (if (isAmbient) 1 else 0)
        return androidBitmapCache.getOrPut(key) {
            val bmp = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            val paint = Paint().apply {
                isAntiAlias = false
                isDither = false
                isFilterBitmap = false
            }
            renderSpriteToCanvas(canvas, paint, speciesId, frame, isAmbient)
            if (!isAmbient) {
                applyVPetPixelOutline(bmp)
            }
            bmp
        }
    }

    /**
     * Post-processes a 64x64 24-bit sprite once at cache-creation time to add an authentic
     * 1-pixel Bandai Digimon Color / 0nl1ne dark pixel outline and top-edge pixel highlight.
     */
    private fun applyVPetPixelOutline(bmp: Bitmap) {
        val pixels = IntArray(64 * 64)
        val out = IntArray(64 * 64)
        bmp.getPixels(pixels, 0, 64, 0, 0, 64, 64)
        System.arraycopy(pixels, 0, out, 0, pixels.size)
        val outlineColor = Color.rgb(16, 18, 26)

        for (y in 1 until 63) {
            val row = y * 64
            for (x in 1 until 63) {
                val idx = row + x
                val c = pixels[idx]
                if ((c ushr 24) == 0) {
                    if ((pixels[idx - 1] ushr 24) != 0 ||
                        (pixels[idx + 1] ushr 24) != 0 ||
                        (pixels[idx - 64] ushr 24) != 0 ||
                        (pixels[idx + 64] ushr 24) != 0
                    ) {
                        out[idx] = outlineColor
                    }
                } else if ((pixels[idx - 64] ushr 24) == 0) {
                    val r = ((c shr 16) and 0xFF).let { (it + 20).coerceAtMost(255) }
                    val g = ((c shr 8) and 0xFF).let { (it + 20).coerceAtMost(255) }
                    val b = (c and 0xFF).let { (it + 20).coerceAtMost(255) }
                    out[idx] = (0xFF shl 24) or (r shl 16) or (g shl 8) or b
                }
            }
        }
        bmp.setPixels(out, 0, 64, 0, 0, 64, 64)
    }

    private data class Palette(
        val primary: Int,
        val secondary: Int,
        val tertiary: Int,
        val eye: Int
    )

    private fun getPalette(speciesId: Int): Palette = when (speciesId) {
        // ==========================================
        // Baby I (101..113)
        // ==========================================
        101 -> Palette(Color.rgb(28, 28, 34), Color.rgb(55, 55, 68), Color.rgb(80, 80, 95), Color.rgb(255, 230, 0)) // Botamon
        103 -> Palette(Color.rgb(220, 40, 50), Color.rgb(180, 25, 35), Color.rgb(255, 120, 130), Color.rgb(255, 255, 255)) // Punimon
        105 -> Palette(Color.rgb(180, 225, 245), Color.rgb(140, 195, 225), Color.rgb(220, 245, 255), Color.rgb(20, 30, 40)) // Poyomon
        107 -> Palette(Color.rgb(190, 190, 195), Color.rgb(130, 130, 140), Color.rgb(240, 240, 245), Color.rgb(15, 15, 20)) // Yuramon
        109 -> Palette(Color.rgb(45, 55, 45), Color.rgb(75, 90, 70), Color.rgb(115, 135, 105), Color.rgb(255, 215, 0)) // Zurumon
        111 -> Palette(Color.rgb(120, 120, 125), Color.rgb(80, 80, 85), Color.rgb(160, 160, 170), Color.rgb(240, 60, 40)) // Mokumon
        113 -> Palette(Color.rgb(105, 90, 155), Color.rgb(70, 55, 115), Color.rgb(160, 140, 210), Color.rgb(230, 30, 40)) // Kuramon

        // ==========================================
        // Baby II (102..119)
        // ==========================================
        102 -> Palette(Color.rgb(255, 145, 175), Color.rgb(225, 80, 115), Color.rgb(255, 190, 210), Color.rgb(20, 20, 25)) // Koromon
        104 -> Palette(Color.rgb(240, 210, 150), Color.rgb(160, 95, 40), Color.rgb(255, 180, 60), Color.rgb(30, 30, 30)) // Tsunomon
        106 -> Palette(Color.rgb(245, 235, 210), Color.rgb(215, 195, 160), Color.rgb(255, 150, 150), Color.rgb(25, 25, 30)) // Tokomon
        108 -> Palette(Color.rgb(100, 190, 90), Color.rgb(60, 140, 55), Color.rgb(230, 120, 180), Color.rgb(20, 20, 20)) // Tanemon
        110 -> Palette(Color.rgb(110, 85, 135), Color.rgb(75, 50, 95), Color.rgb(155, 125, 185), Color.rgb(225, 40, 40)) // Pagumon
        112 -> Palette(Color.rgb(240, 100, 30), Color.rgb(255, 180, 40), Color.rgb(200, 40, 20), Color.rgb(255, 255, 255)) // DemiMeramon
        114 -> Palette(Color.rgb(65, 145, 225), Color.rgb(225, 235, 245), Color.rgb(35, 95, 175), Color.rgb(220, 40, 40)) // DemiVeemon
        115 -> Palette(Color.rgb(225, 55, 45), Color.rgb(35, 35, 40), Color.rgb(245, 235, 215), Color.rgb(255, 215, 0)) // Gigimon
        116 -> Palette(Color.rgb(245, 205, 75), Color.rgb(245, 240, 220), Color.rgb(195, 155, 45), Color.rgb(40, 140, 220)) // Viximon
        117 -> Palette(Color.rgb(125, 215, 145), Color.rgb(85, 175, 105), Color.rgb(240, 150, 180), Color.rgb(25, 25, 30)) // Gummymon
        118 -> Palette(Color.rgb(165, 115, 75), Color.rgb(120, 75, 45), Color.rgb(225, 180, 145), Color.rgb(25, 25, 30)) // Chocomon
        119 -> Palette(Color.rgb(55, 45, 95), Color.rgb(85, 75, 135), Color.rgb(225, 60, 80), Color.rgb(255, 215, 0)) // Yaamon

        // ==========================================
        // Child (201..222)
        // ==========================================
        201 -> Palette(Color.rgb(255, 175, 35), Color.rgb(220, 125, 15), Color.rgb(255, 210, 120), Color.rgb(0, 180, 50)) // Agumon
        202 -> Palette(Color.rgb(35, 175, 110), Color.rgb(20, 120, 75), Color.rgb(240, 60, 60), Color.rgb(255, 60, 60)) // Betamon
        203 -> Palette(Color.rgb(240, 225, 170), Color.rgb(70, 120, 200), Color.rgb(230, 180, 50), Color.rgb(180, 30, 30)) // Gabumon
        204 -> Palette(Color.rgb(225, 45, 55), Color.rgb(60, 110, 210), Color.rgb(255, 220, 50), Color.rgb(20, 20, 25)) // Elecmon
        205 -> Palette(Color.rgb(240, 165, 80), Color.rgb(205, 125, 50), Color.rgb(255, 210, 160), Color.rgb(30, 90, 180)) // Patamon
        206 -> Palette(Color.rgb(255, 130, 170), Color.rgb(65, 140, 230), Color.rgb(220, 70, 110), Color.rgb(30, 120, 220)) // Biyomon
        207 -> Palette(Color.rgb(105, 195, 85), Color.rgb(235, 80, 130), Color.rgb(160, 100, 50), Color.rgb(20, 20, 20)) // Palmon
        208 -> Palette(Color.rgb(205, 60, 50), Color.rgb(140, 125, 100), Color.rgb(80, 170, 90), Color.rgb(0, 210, 70)) // Tentomon
        209 -> Palette(Color.rgb(100, 110, 135), Color.rgb(55, 65, 90), Color.rgb(215, 215, 225), Color.rgb(220, 40, 40)) // Gazimon
        210 -> Palette(Color.rgb(40, 150, 130), Color.rgb(215, 50, 60), Color.rgb(210, 225, 215), Color.rgb(225, 40, 40)) // Gizamon
        211 -> Palette(Color.rgb(240, 215, 50), Color.rgb(115, 45, 125), Color.rgb(225, 175, 40), Color.rgb(20, 20, 25)) // Kunemon
        212 -> Palette(Color.rgb(55, 130, 225), Color.rgb(240, 240, 245), Color.rgb(235, 185, 45), Color.rgb(215, 35, 45)) // Veemon
        213 -> Palette(Color.rgb(115, 195, 55), Color.rgb(65, 135, 35), Color.rgb(115, 75, 155), Color.rgb(40, 120, 210)) // Wormmon
        214 -> Palette(Color.rgb(225, 45, 40), Color.rgb(30, 30, 35), Color.rgb(245, 240, 230), Color.rgb(245, 205, 40)) // Guilmon
        215 -> Palette(Color.rgb(245, 205, 55), Color.rgb(105, 60, 145), Color.rgb(245, 245, 250), Color.rgb(40, 170, 220)) // Renamon
        216 -> Palette(Color.rgb(245, 245, 235), Color.rgb(90, 175, 80), Color.rgb(165, 120, 75), Color.rgb(25, 25, 30)) // Terriermon
        217 -> Palette(Color.rgb(195, 155, 115), Color.rgb(225, 110, 150), Color.rgb(135, 95, 60), Color.rgb(25, 25, 30)) // Lopmon
        218 -> Palette(Color.rgb(105, 65, 155), Color.rgb(225, 40, 40), Color.rgb(240, 235, 215), Color.rgb(225, 210, 40)) // Impmon
        219 -> Palette(Color.rgb(45, 45, 50), Color.rgb(25, 25, 30), Color.rgb(195, 40, 40), Color.rgb(220, 40, 40)) // BlackAgumon
        220 -> Palette(Color.rgb(40, 45, 55), Color.rgb(20, 25, 35), Color.rgb(180, 45, 45), Color.rgb(245, 205, 40)) // BlackGabumon
        221 -> Palette(Color.rgb(245, 235, 215), Color.rgb(245, 210, 50), Color.rgb(250, 250, 255), Color.rgb(45, 125, 225)) // Lucemon
        222 -> Palette(Color.rgb(240, 240, 245), Color.rgb(195, 35, 45), Color.rgb(135, 140, 150), Color.rgb(245, 205, 40)) // Hackmon

        // ==========================================
        // Adult (301..341)
        // ==========================================
        301 -> Palette(Color.rgb(255, 135, 20), Color.rgb(90, 50, 30), Color.rgb(40, 80, 160), Color.rgb(30, 100, 220)) // Greymon
        302 -> Palette(Color.rgb(220, 45, 40), Color.rgb(160, 30, 30), Color.rgb(240, 230, 210), Color.rgb(0, 180, 60)) // Tyrannomon
        303 -> Palette(Color.rgb(35, 35, 45), Color.rgb(140, 20, 45), Color.rgb(80, 80, 100), Color.rgb(240, 240, 240)) // Devimon
        304 -> Palette(Color.rgb(255, 90, 25), Color.rgb(220, 40, 10), Color.rgb(255, 210, 40), Color.rgb(255, 255, 255)) // Meramon
        305 -> Palette(Color.rgb(145, 200, 65), Color.rgb(105, 155, 40), Color.rgb(200, 240, 120), Color.rgb(255, 255, 255)) // Numemon
        306 -> Palette(Color.rgb(235, 240, 250), Color.rgb(50, 105, 205), Color.rgb(215, 60, 110), Color.rgb(255, 215, 0)) // Garurumon
        307 -> Palette(Color.rgb(245, 245, 250), Color.rgb(215, 180, 50), Color.rgb(60, 120, 220), Color.rgb(30, 80, 180)) // Angemon
        308 -> Palette(Color.rgb(65, 160, 75), Color.rgb(195, 45, 35), Color.rgb(240, 150, 40), Color.rgb(15, 15, 15)) // Togemon
        309 -> Palette(Color.rgb(235, 65, 35), Color.rgb(255, 175, 45), Color.rgb(200, 35, 20), Color.rgb(30, 100, 200)) // Birdramon
        310 -> Palette(Color.rgb(55, 95, 175), Color.rgb(35, 60, 125), Color.rgb(180, 170, 140), Color.rgb(255, 215, 0)) // Kabuterimon
        311 -> Palette(Color.rgb(115, 125, 135), Color.rgb(185, 45, 45), Color.rgb(65, 70, 75), Color.rgb(225, 40, 40)) // Monochromon
        312 -> Palette(Color.rgb(115, 165, 70), Color.rgb(75, 115, 45), Color.rgb(160, 130, 80), Color.rgb(255, 230, 40)) // Ogremon
        313 -> Palette(Color.rgb(215, 155, 75), Color.rgb(145, 95, 40), Color.rgb(180, 50, 50), Color.rgb(30, 150, 220)) // Leomon
        314 -> Palette(Color.rgb(50, 120, 205), Color.rgb(230, 195, 50), Color.rgb(25, 70, 145), Color.rgb(240, 50, 50)) // Seadramon
        315 -> Palette(Color.rgb(225, 195, 45), Color.rgb(175, 145, 25), Color.rgb(250, 230, 120), Color.rgb(255, 255, 255)) // Sukamon
        316 -> Palette(Color.rgb(40, 40, 45), Color.rgb(195, 35, 40), Color.rgb(225, 225, 220), Color.rgb(0, 210, 80)) // DarkTyrannomon
        317 -> Palette(Color.rgb(195, 35, 45), Color.rgb(65, 45, 95), Color.rgb(30, 30, 35), Color.rgb(245, 30, 30)) // Devidramon
        318 -> Palette(Color.rgb(225, 185, 65), Color.rgb(175, 40, 45), Color.rgb(130, 140, 155), Color.rgb(225, 40, 40)) // Cyclonemon
        319 -> Palette(Color.rgb(45, 105, 55), Color.rgb(25, 25, 30), Color.rgb(235, 235, 220), Color.rgb(225, 40, 40)) // Tuskmon
        320 -> Palette(Color.rgb(235, 195, 45), Color.rgb(85, 150, 220), Color.rgb(195, 45, 45), Color.rgb(20, 20, 25)) // Flymon
        321 -> Palette(Color.rgb(115, 65, 135), Color.rgb(145, 155, 165), Color.rgb(205, 85, 145), Color.rgb(245, 215, 40)) // Raremon
        322 -> Palette(Color.rgb(195, 35, 40), Color.rgb(35, 35, 40), Color.rgb(185, 190, 200), Color.rgb(245, 215, 40)) // Kuwagamon
        323 -> Palette(Color.rgb(245, 240, 235), Color.rgb(125, 55, 145), Color.rgb(235, 125, 40), Color.rgb(215, 35, 40)) // Kokatorimon
        324 -> Palette(Color.rgb(65, 95, 155), Color.rgb(215, 215, 225), Color.rgb(140, 165, 195), Color.rgb(225, 40, 40)) // Coelamon
        325 -> Palette(Color.rgb(235, 145, 95), Color.rgb(30, 30, 35), Color.rgb(215, 45, 50), Color.rgb(245, 245, 250)) // Nanimon
        326 -> Palette(Color.rgb(235, 240, 245), Color.rgb(35, 45, 75), Color.rgb(85, 125, 205), Color.rgb(255, 255, 255)) // Bakemon
        327 -> Palette(Color.rgb(235, 135, 155), Color.rgb(155, 105, 70), Color.rgb(75, 165, 85), Color.rgb(20, 20, 25)) // Shellmon
        328 -> Palette(Color.rgb(245, 245, 250), Color.rgb(215, 45, 45), Color.rgb(85, 145, 225), Color.rgb(40, 110, 215)) // Unimon
        329 -> Palette(Color.rgb(245, 245, 250), Color.rgb(45, 115, 215), Color.rgb(225, 45, 45), Color.rgb(20, 20, 25)) // Frigimon
        330 -> Palette(Color.rgb(145, 195, 55), Color.rgb(215, 45, 45), Color.rgb(65, 145, 45), Color.rgb(20, 20, 25)) // Vegiemon
        331 -> Palette(Color.rgb(45, 115, 225), Color.rgb(240, 240, 245), Color.rgb(215, 40, 45), Color.rgb(245, 215, 40)) // Veedramon
        332 -> Palette(Color.rgb(215, 45, 40), Color.rgb(245, 195, 40), Color.rgb(45, 95, 185), Color.rgb(225, 40, 40)) // Flamedramon
        333 -> Palette(Color.rgb(45, 145, 65), Color.rgb(35, 40, 45), Color.rgb(165, 175, 185), Color.rgb(215, 35, 40)) // Stingmon
        334 -> Palette(Color.rgb(215, 40, 35), Color.rgb(245, 245, 250), Color.rgb(35, 35, 40), Color.rgb(245, 215, 40)) // Growlmon
        335 -> Palette(Color.rgb(245, 245, 250), Color.rgb(245, 195, 45), Color.rgb(65, 145, 225), Color.rgb(45, 175, 215)) // Kyubimon
        336 -> Palette(Color.rgb(240, 235, 215), Color.rgb(55, 115, 65), Color.rgb(145, 155, 165), Color.rgb(25, 25, 30)) // Gargomon
        337 -> Palette(Color.rgb(95, 65, 145), Color.rgb(235, 185, 45), Color.rgb(165, 125, 75), Color.rgb(45, 165, 215)) // Wizardmon
        338 -> Palette(Color.rgb(245, 245, 250), Color.rgb(65, 155, 75), Color.rgb(135, 75, 155), Color.rgb(45, 125, 215)) // Gatomon
        339 -> Palette(Color.rgb(45, 45, 55), Color.rgb(95, 55, 35), Color.rgb(45, 85, 155), Color.rgb(0, 210, 80)) // BlackGreymon
        340 -> Palette(Color.rgb(40, 45, 55), Color.rgb(85, 95, 115), Color.rgb(185, 40, 45), Color.rgb(245, 215, 40)) // BlackGarurumon
        341 -> Palette(Color.rgb(240, 240, 245), Color.rgb(195, 35, 45), Color.rgb(55, 60, 70), Color.rgb(245, 205, 40)) // BaoHackmon

        // ==========================================
        // Perfect (401..434)
        // ==========================================
        401 -> Palette(Color.rgb(195, 145, 65), Color.rgb(165, 175, 195), Color.rgb(60, 95, 175), Color.rgb(0, 190, 60)) // MetalGreymon
        402 -> Palette(Color.rgb(245, 205, 45), Color.rgb(195, 155, 25), Color.rgb(235, 60, 80), Color.rgb(20, 20, 20)) // Monzaemon
        403 -> Palette(Color.rgb(230, 225, 215), Color.rgb(165, 160, 150), Color.rgb(85, 30, 120), Color.rgb(200, 20, 20)) // SkullGreymon
        404 -> Palette(Color.rgb(90, 105, 145), Color.rgb(235, 240, 250), Color.rgb(55, 55, 65), Color.rgb(255, 200, 30)) // WereGarurumon
        405 -> Palette(Color.rgb(245, 245, 255), Color.rgb(80, 135, 230), Color.rgb(225, 70, 110), Color.rgb(255, 215, 0)) // MagnaAngemon
        406 -> Palette(Color.rgb(85, 185, 80), Color.rgb(245, 120, 170), Color.rgb(255, 220, 70), Color.rgb(40, 140, 220)) // Lillymon
        407 -> Palette(Color.rgb(215, 60, 45), Color.rgb(245, 210, 60), Color.rgb(50, 90, 170), Color.rgb(255, 255, 255)) // Garudamon
        408 -> Palette(Color.rgb(190, 45, 40), Color.rgb(125, 25, 20), Color.rgb(65, 70, 85), Color.rgb(0, 210, 80)) // MegaKabuterimon
        409 -> Palette(Color.rgb(205, 45, 45), Color.rgb(175, 185, 200), Color.rgb(255, 210, 40), Color.rgb(0, 180, 220)) // MegaSeadramon
        410 -> Palette(Color.rgb(40, 40, 50), Color.rgb(175, 25, 40), Color.rgb(225, 210, 180), Color.rgb(240, 40, 40)) // Myotismon
        411 -> Palette(Color.rgb(165, 105, 65), Color.rgb(225, 170, 40), Color.rgb(20, 20, 20), Color.rgb(20, 20, 20)) // Etemon
        412 -> Palette(Color.rgb(165, 175, 190), Color.rgb(195, 45, 40), Color.rgb(235, 205, 50), Color.rgb(20, 20, 20)) // Mamemon
        413 -> Palette(Color.rgb(130, 140, 155), Color.rgb(195, 50, 45), Color.rgb(80, 85, 95), Color.rgb(255, 215, 0)) // MetalMamemon
        414 -> Palette(Color.rgb(55, 60, 65), Color.rgb(165, 175, 185), Color.rgb(195, 40, 45), Color.rgb(0, 210, 80)) // MetalTyrannomon
        415 -> Palette(Color.rgb(235, 135, 165), Color.rgb(35, 35, 40), Color.rgb(245, 245, 250), Color.rgb(20, 20, 25)) // Ex-Tyrannomon
        416 -> Palette(Color.rgb(185, 40, 45), Color.rgb(155, 165, 180), Color.rgb(65, 45, 75), Color.rgb(225, 40, 40)) // Megadramon
        417 -> Palette(Color.rgb(245, 165, 195), Color.rgb(235, 205, 55), Color.rgb(95, 55, 125), Color.rgb(0, 210, 80)) // Piximon
        418 -> Palette(Color.rgb(165, 175, 190), Color.rgb(60, 115, 195), Color.rgb(235, 185, 155), Color.rgb(40, 135, 225)) // Andromon
        419 -> Palette(Color.rgb(135, 145, 160), Color.rgb(115, 55, 135), Color.rgb(215, 45, 45), Color.rgb(225, 40, 40)) // Giromon
        420 -> Palette(Color.rgb(235, 145, 175), Color.rgb(115, 65, 155), Color.rgb(155, 165, 180), Color.rgb(25, 25, 30)) // Vademon
        421 -> Palette(Color.rgb(235, 230, 225), Color.rgb(45, 45, 55), Color.rgb(165, 175, 185), Color.rgb(225, 40, 40)) // SkullMamemon
        422 -> Palette(Color.rgb(45, 115, 225), Color.rgb(245, 245, 250), Color.rgb(215, 45, 45), Color.rgb(245, 215, 40)) // AeroVeedramon
        423 -> Palette(Color.rgb(45, 105, 195), Color.rgb(45, 135, 65), Color.rgb(155, 165, 180), Color.rgb(225, 40, 40)) // Paildramon
        424 -> Palette(Color.rgb(215, 40, 35), Color.rgb(165, 175, 190), Color.rgb(35, 35, 40), Color.rgb(245, 215, 40)) // WarGrowlmon
        425 -> Palette(Color.rgb(245, 245, 250), Color.rgb(35, 35, 45), Color.rgb(135, 145, 160), Color.rgb(45, 165, 215)) // Taomon
        426 -> Palette(Color.rgb(115, 165, 125), Color.rgb(235, 195, 45), Color.rgb(45, 65, 55), Color.rgb(25, 25, 30)) // Rapidmon
        427 -> Palette(Color.rgb(245, 245, 255), Color.rgb(245, 215, 65), Color.rgb(115, 185, 235), Color.rgb(45, 135, 225)) // Angewomon
        428 -> Palette(Color.rgb(35, 35, 45), Color.rgb(185, 35, 45), Color.rgb(145, 155, 165), Color.rgb(245, 215, 40)) // LadyDevimon
        429 -> Palette(Color.rgb(45, 65, 95), Color.rgb(115, 125, 140), Color.rgb(195, 145, 45), Color.rgb(0, 210, 80)) // BlackMetalGreymon
        430 -> Palette(Color.rgb(45, 45, 55), Color.rgb(165, 175, 190), Color.rgb(45, 85, 145), Color.rgb(225, 40, 40)) // BlackWereGarurumon
        431 -> Palette(Color.rgb(35, 35, 40), Color.rgb(195, 35, 45), Color.rgb(145, 155, 170), Color.rgb(225, 40, 40)) // Cyberdramon
        432 -> Palette(Color.rgb(165, 125, 85), Color.rgb(235, 135, 165), Color.rgb(235, 195, 45), Color.rgb(25, 25, 30)) // Antylamon
        433 -> Palette(Color.rgb(245, 245, 250), Color.rgb(195, 35, 45), Color.rgb(155, 165, 180), Color.rgb(245, 205, 40)) // SaviorHackmon
        434 -> Palette(Color.rgb(240, 235, 220), Color.rgb(75, 45, 115), Color.rgb(235, 195, 45), Color.rgb(215, 40, 45)) // Lucemon Chaos Mode

        // ==========================================
        // Ultimate & Super Ultimate (501..549)
        // ==========================================
        501 -> Palette(Color.rgb(255, 195, 45), Color.rgb(180, 190, 210), Color.rgb(215, 40, 40), Color.rgb(0, 210, 70)) // WarGreymon
        502 -> Palette(Color.rgb(120, 130, 145), Color.rgb(60, 65, 75), Color.rgb(220, 35, 35), Color.rgb(255, 40, 40)) // Machinedramon
        503 -> Palette(Color.rgb(80, 130, 215), Color.rgb(185, 195, 215), Color.rgb(240, 60, 60), Color.rgb(255, 215, 0)) // MetalGarurumon
        504 -> Palette(Color.rgb(205, 215, 235), Color.rgb(235, 195, 45), Color.rgb(65, 110, 195), Color.rgb(30, 120, 230)) // Seraphimon
        505 -> Palette(Color.rgb(225, 35, 60), Color.rgb(75, 165, 75), Color.rgb(245, 210, 60), Color.rgb(40, 130, 220)) // Rosemon
        506 -> Palette(Color.rgb(245, 205, 45), Color.rgb(215, 75, 45), Color.rgb(240, 240, 250), Color.rgb(40, 100, 220)) // Phoenixmon
        507 -> Palette(Color.rgb(235, 195, 45), Color.rgb(175, 135, 25), Color.rgb(55, 60, 75), Color.rgb(0, 230, 90)) // HerculesKabuterimon
        508 -> Palette(Color.rgb(235, 240, 250), Color.rgb(75, 140, 220), Color.rgb(240, 190, 50), Color.rgb(30, 90, 190)) // Plesiomon
        509 -> Palette(Color.rgb(85, 45, 135), Color.rgb(215, 45, 55), Color.rgb(245, 245, 250), Color.rgb(255, 215, 0)) // Piedmon
        510 -> Palette(Color.rgb(245, 245, 255), Color.rgb(215, 175, 45), Color.rgb(65, 115, 215), Color.rgb(0, 210, 90)) // Omnimon
        511 -> Palette(Color.rgb(195, 205, 220), Color.rgb(140, 150, 165), Color.rgb(30, 30, 30), Color.rgb(20, 20, 20)) // MetalEtemon
        512 -> Palette(Color.rgb(40, 45, 55), Color.rgb(235, 195, 45), Color.rgb(195, 40, 45), Color.rgb(0, 210, 80)) // Imperialdramon DM
        513 -> Palette(Color.rgb(45, 50, 60), Color.rgb(245, 205, 50), Color.rgb(195, 45, 45), Color.rgb(0, 210, 80)) // Imperialdramon FM
        514 -> Palette(Color.rgb(245, 245, 255), Color.rgb(235, 195, 45), Color.rgb(65, 125, 225), Color.rgb(45, 135, 225)) // Imperialdramon PM
        515 -> Palette(Color.rgb(245, 245, 255), Color.rgb(215, 40, 45), Color.rgb(235, 195, 45), Color.rgb(245, 215, 40)) // Gallantmon
        516 -> Palette(Color.rgb(215, 40, 45), Color.rgb(245, 245, 255), Color.rgb(245, 215, 55), Color.rgb(245, 215, 40)) // Gallantmon CM
        517 -> Palette(Color.rgb(245, 205, 50), Color.rgb(245, 245, 250), Color.rgb(115, 65, 155), Color.rgb(45, 165, 215)) // Sakuyamon
        518 -> Palette(Color.rgb(95, 145, 105), Color.rgb(195, 45, 45), Color.rgb(145, 155, 165), Color.rgb(25, 25, 30)) // MegaGargomon
        519 -> Palette(Color.rgb(35, 35, 45), Color.rgb(195, 35, 45), Color.rgb(155, 165, 175), Color.rgb(225, 40, 40)) // Beelzemon
        520 -> Palette(Color.rgb(35, 35, 45), Color.rgb(55, 45, 75), Color.rgb(165, 175, 185), Color.rgb(0, 210, 80)) // Beelzemon BM
        521 -> Palette(Color.rgb(245, 205, 45), Color.rgb(45, 115, 215), Color.rgb(245, 245, 250), Color.rgb(225, 40, 40)) // Magnamon
        522 -> Palette(Color.rgb(45, 115, 225), Color.rgb(245, 210, 50), Color.rgb(0, 210, 140), Color.rgb(245, 215, 40)) // UlforceVeedramon
        523 -> Palette(Color.rgb(30, 30, 35), Color.rgb(235, 195, 45), Color.rgb(75, 135, 225), Color.rgb(0, 210, 80)) // Alphamon
        524 -> Palette(Color.rgb(30, 30, 35), Color.rgb(245, 205, 50), Color.rgb(215, 45, 45), Color.rgb(245, 215, 40)) // Alphamon Ouryuken
        525 -> Palette(Color.rgb(40, 40, 45), Color.rgb(165, 175, 190), Color.rgb(225, 195, 45), Color.rgb(0, 210, 80)) // BlackWarGreymon
        526 -> Palette(Color.rgb(30, 30, 35), Color.rgb(195, 35, 45), Color.rgb(115, 125, 140), Color.rgb(225, 40, 40)) // ChaosBlackWarGreymon
        527 -> Palette(Color.rgb(75, 95, 125), Color.rgb(195, 40, 45), Color.rgb(35, 40, 50), Color.rgb(225, 40, 40)) // Diaboromon
        528 -> Palette(Color.rgb(45, 45, 55), Color.rgb(215, 45, 45), Color.rgb(145, 155, 170), Color.rgb(225, 40, 40)) // Armaggeddemon
        529 -> Palette(Color.rgb(45, 45, 55), Color.rgb(245, 235, 215), Color.rgb(195, 40, 45), Color.rgb(245, 215, 40)) // Apocalymon
        530 -> Palette(Color.rgb(115, 65, 145), Color.rgb(35, 35, 45), Color.rgb(165, 175, 185), Color.rgb(225, 40, 40)) // Belphemon
        531 -> Palette(Color.rgb(35, 35, 45), Color.rgb(235, 195, 45), Color.rgb(135, 75, 155), Color.rgb(225, 40, 40)) // Lilithmon
        532 -> Palette(Color.rgb(195, 35, 45), Color.rgb(35, 35, 40), Color.rgb(235, 195, 45), Color.rgb(225, 40, 40)) // Daemon
        533 -> Palette(Color.rgb(195, 35, 40), Color.rgb(165, 175, 185), Color.rgb(45, 45, 55), Color.rgb(245, 215, 40)) // Leviamon
        534 -> Palette(Color.rgb(45, 45, 60), Color.rgb(245, 245, 250), Color.rgb(165, 175, 190), Color.rgb(225, 40, 40)) // Barbamon
        535 -> Palette(Color.rgb(245, 245, 255), Color.rgb(195, 45, 45), Color.rgb(245, 205, 45), Color.rgb(225, 40, 40)) // Omnimon Alter-S
        536 -> Palette(Color.rgb(35, 35, 45), Color.rgb(235, 195, 45), Color.rgb(185, 35, 45), Color.rgb(225, 40, 40)) // Omnimon Zwart
        537 -> Palette(Color.rgb(245, 245, 255), Color.rgb(35, 35, 45), Color.rgb(135, 75, 155), Color.rgb(145, 85, 205)) // Mastemon
        538 -> Palette(Color.rgb(245, 245, 255), Color.rgb(215, 45, 45), Color.rgb(245, 205, 45), Color.rgb(245, 205, 40)) // Jesmon
        539 -> Palette(Color.rgb(235, 195, 160), Color.rgb(245, 205, 45), Color.rgb(45, 45, 55), Color.rgb(245, 205, 40)) // Gankoomon
        540 -> Palette(Color.rgb(245, 245, 255), Color.rgb(115, 65, 145), Color.rgb(65, 125, 215), Color.rgb(0, 210, 80)) // Dynasmon
        541 -> Palette(Color.rgb(235, 145, 175), Color.rgb(245, 205, 45), Color.rgb(245, 245, 255), Color.rgb(45, 135, 225)) // LordKnightmon
        542 -> Palette(Color.rgb(55, 65, 75), Color.rgb(165, 175, 185), Color.rgb(195, 45, 45), Color.rgb(225, 40, 40)) // Craniummon
        543 -> Palette(Color.rgb(215, 45, 45), Color.rgb(245, 205, 45), Color.rgb(245, 245, 255), Color.rgb(45, 175, 215)) // Sleipmon
        544 -> Palette(Color.rgb(245, 245, 255), Color.rgb(245, 205, 45), Color.rgb(145, 155, 165), Color.rgb(45, 135, 225)) // Duftmon
        545 -> Palette(Color.rgb(215, 40, 45), Color.rgb(55, 75, 115), Color.rgb(245, 205, 45), Color.rgb(0, 210, 80)) // Examon
        546 -> Palette(Color.rgb(65, 75, 95), Color.rgb(165, 175, 190), Color.rgb(115, 45, 125), Color.rgb(225, 40, 40)) // Millenniummon
        547 -> Palette(Color.rgb(115, 65, 145), Color.rgb(55, 45, 75), Color.rgb(185, 145, 215), Color.rgb(225, 40, 40)) // MoonMillenniummon
        548 -> Palette(Color.rgb(45, 45, 60), Color.rgb(245, 205, 45), Color.rgb(145, 55, 145), Color.rgb(225, 40, 40)) // ZeedMillenniummon
        549 -> Palette(Color.rgb(215, 45, 45), Color.rgb(245, 205, 45), Color.rgb(245, 245, 255), Color.rgb(45, 135, 225)) // Susanoomon

        // ==========================================
        // Expanded roster (130..132, 120..122, 223..231, 342..349, 435..443, 550..557)
        // ==========================================
        130 -> Palette(Color.rgb(32, 32, 40), Color.rgb(60, 60, 75), Color.rgb(80, 200, 60), Color.rgb(255, 215, 0)) // Nyokimon
        131 -> Palette(Color.rgb(120, 200, 245), Color.rgb(75, 160, 220), Color.rgb(200, 235, 255), Color.rgb(25, 25, 35)) // Pichimon
        132 -> Palette(Color.rgb(80, 160, 220), Color.rgb(40, 100, 160), Color.rgb(220, 240, 255), Color.rgb(25, 25, 30)) // Pukamon
        120 -> Palette(Color.rgb(250, 240, 160), Color.rgb(150, 50, 140), Color.rgb(240, 160, 180), Color.rgb(40, 160, 70)) // Nyaromon
        121 -> Palette(Color.rgb(255, 150, 165), Color.rgb(220, 40, 70), Color.rgb(70, 160, 70), Color.rgb(25, 25, 30)) // Pyocomon
        122 -> Palette(Color.rgb(140, 90, 180), Color.rgb(240, 240, 250), Color.rgb(230, 190, 60), Color.rgb(255, 215, 0)) // Dorimon
        223 -> Palette(Color.rgb(250, 245, 230), Color.rgb(200, 140, 75), Color.rgb(255, 215, 0), Color.rgb(45, 125, 215)) // Salamon
        224 -> Palette(Color.rgb(65, 120, 215), Color.rgb(240, 240, 250), Color.rgb(215, 45, 55), Color.rgb(30, 60, 140)) // Gaomon
        225 -> Palette(Color.rgb(215, 70, 50), Color.rgb(245, 205, 60), Color.rgb(250, 240, 220), Color.rgb(40, 140, 220)) // Hawkmon
        226 -> Palette(Color.rgb(240, 200, 60), Color.rgb(165, 125, 45), Color.rgb(250, 235, 170), Color.rgb(25, 25, 30)) // Armadillomon
        227 -> Palette(Color.rgb(125, 60, 165), Color.rgb(240, 240, 250), Color.rgb(215, 40, 50), Color.rgb(255, 215, 0)) // Dorumon
        228 -> Palette(Color.rgb(245, 235, 215), Color.rgb(205, 45, 55), Color.rgb(60, 95, 175), Color.rgb(25, 25, 30)) // Kotemon
        229 -> Palette(Color.rgb(140, 75, 160), Color.rgb(245, 205, 40), Color.rgb(215, 40, 50), Color.rgb(255, 215, 0)) // Monodramon
        230 -> Palette(Color.rgb(140, 180, 60), Color.rgb(230, 110, 175), Color.rgb(70, 150, 70), Color.rgb(40, 120, 210)) // Floramon
        231 -> Palette(Color.rgb(110, 175, 70), Color.rgb(165, 120, 70), Color.rgb(215, 85, 60), Color.rgb(225, 40, 40)) // Goblimon
        342 -> Palette(Color.rgb(95, 45, 140), Color.rgb(240, 240, 250), Color.rgb(215, 40, 50), Color.rgb(255, 215, 0)) // Dorugamon
        343 -> Palette(Color.rgb(235, 235, 250), Color.rgb(70, 150, 230), Color.rgb(230, 190, 55), Color.rgb(25, 25, 30)) // Sorcerymon
        344 -> Palette(Color.rgb(205, 60, 45), Color.rgb(245, 205, 60), Color.rgb(235, 225, 205), Color.rgb(40, 140, 220)) // Aquilamon
        345 -> Palette(Color.rgb(215, 170, 45), Color.rgb(125, 95, 40), Color.rgb(235, 215, 150), Color.rgb(25, 25, 30)) // Ankylomon
        346 -> Palette(Color.rgb(50, 105, 205), Color.rgb(240, 240, 250), Color.rgb(205, 45, 55), Color.rgb(230, 190, 40)) // Gaogamon
        347 -> Palette(Color.rgb(240, 240, 250), Color.rgb(165, 190, 225), Color.rgb(90, 130, 195), Color.rgb(25, 25, 30)) // Mojyamon
        348 -> Palette(Color.rgb(245, 205, 55), Color.rgb(215, 60, 55), Color.rgb(200, 210, 225), Color.rgb(30, 40, 100)) // Starmon
        349 -> Palette(Color.rgb(65, 45, 90), Color.rgb(195, 45, 55), Color.rgb(145, 150, 165), Color.rgb(245, 210, 40)) // Dokugumon
        435 -> Palette(Color.rgb(100, 50, 150), Color.rgb(205, 210, 225), Color.rgb(215, 40, 50), Color.rgb(255, 215, 0)) // Dorugreymon
        436 -> Palette(Color.rgb(200, 205, 220), Color.rgb(205, 45, 55), Color.rgb(220, 160, 40), Color.rgb(40, 140, 220)) // Knightmon
        437 -> Palette(Color.rgb(235, 240, 250), Color.rgb(205, 60, 50), Color.rgb(245, 200, 60), Color.rgb(45, 150, 225)) // Silphymon
        438 -> Palette(Color.rgb(70, 125, 195), Color.rgb(235, 235, 245), Color.rgb(165, 125, 80), Color.rgb(225, 190, 45)) // Zudomon
        439 -> Palette(Color.rgb(245, 245, 250), Color.rgb(90, 125, 190), Color.rgb(225, 175, 60), Color.rgb(205, 45, 55)) // Panjyamon
        440 -> Palette(Color.rgb(50, 40, 70), Color.rgb(235, 235, 245), Color.rgb(195, 45, 55), Color.rgb(245, 210, 40)) // Phantomon
        441 -> Palette(Color.rgb(195, 160, 70), Color.rgb(125, 135, 155), Color.rgb(215, 45, 55), Color.rgb(30, 30, 35)) // Shakkoumon
        442 -> Palette(Color.rgb(220, 213, 185), Color.rgb(40, 40, 40), Color.rgb(245, 205, 40), Color.rgb(255, 60, 30)) // Digitamamon
        443 -> Palette(Color.rgb(125, 75, 55), Color.rgb(80, 160, 70), Color.rgb(215, 70, 90), Color.rgb(245, 210, 40)) // Cherrymon
        550 -> Palette(Color.rgb(120, 55, 175), Color.rgb(235, 235, 250), Color.rgb(255, 215, 0), Color.rgb(255, 215, 0)) // Dorugoramon
        551 -> Palette(Color.rgb(45, 45, 60), Color.rgb(195, 45, 55), Color.rgb(235, 180, 55), Color.rgb(225, 40, 40)) // Gaiomon
        552 -> Palette(Color.rgb(245, 245, 255), Color.rgb(235, 110, 175), Color.rgb(245, 205, 60), Color.rgb(145, 215, 250)) // Ophanimon
        553 -> Palette(Color.rgb(225, 230, 245), Color.rgb(75, 105, 175), Color.rgb(195, 150, 70), Color.rgb(245, 205, 40)) // Vikemon
        554 -> Palette(Color.rgb(195, 40, 50), Color.rgb(70, 75, 85), Color.rgb(200, 210, 220), Color.rgb(255, 30, 30)) // Chaosdramon
        555 -> Palette(Color.rgb(235, 235, 250), Color.rgb(75, 135, 215), Color.rgb(225, 175, 60), Color.rgb(45, 150, 225)) // Kentaurosmon
        556 -> Palette(Color.rgb(235, 120, 150), Color.rgb(245, 225, 150), Color.rgb(255, 210, 50), Color.rgb(40, 140, 224)) // Magnadramon
        557 -> Palette(Color.rgb(245, 245, 250), Color.rgb(125, 85, 175), Color.rgb(235, 195, 60), Color.rgb(245, 205, 40)) // Cherubimon
        else -> Palette(Color.rgb(255, 175, 35), Color.rgb(220, 125, 15), Color.rgb(255, 210, 120), Color.rgb(0, 180, 50))
    }

    private fun renderSpriteToCanvas(
        canvas: Canvas,
        paint: Paint,
        speciesId: Int,
        frame: SpriteFrame,
        isAmbient: Boolean
    ) {
        val palette = getPalette(speciesId)

        // Dynamic frame offsets
        val xOffset = when (frame) {
            SpriteFrame.IDLE_B -> 1f
            SpriteFrame.WALK_A -> 3f
            SpriteFrame.WALK_B -> -3f
            SpriteFrame.ATTACK_CHARGE -> -4f
            SpriteFrame.ATTACK_RELEASE -> 6f
            SpriteFrame.REFUSAL -> -2f
            SpriteFrame.INJURED -> -2f
            else -> 0f
        }

        val yBob = when (frame) {
            SpriteFrame.IDLE_B -> -2f
            SpriteFrame.WALK_A, SpriteFrame.WALK_B -> 1f
            SpriteFrame.SLEEP_A, SpriteFrame.SLEEP_B -> 4f
            SpriteFrame.FEED_START -> 1f
            SpriteFrame.JOY_EAT -> -3f
            else -> 0f
        }

        if (isAmbient) {
            // STRICT HIGH-CONTRAST MONOCHROME OUTLINE ONLY (OPR <= 15%)
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f

            when {
                // Baby / Slime / Sphere
                speciesId in 101..139 || speciesId in listOf(402, 412, 413, 315, 321, 325, 415, 419, 420, 421, 528, 529, 547) -> {
                    val radius = if (speciesId in 101..139) 15f else 18f
                    canvas.drawCircle(32f + xOffset, 35f + yBob, radius, paint)
                    if (speciesId in listOf(104, 211)) {
                        canvas.drawLine(32f + xOffset, 20f + yBob, 32f + xOffset, 8f + yBob, paint)
                    } else if (speciesId in listOf(102, 110, 119)) {
                        canvas.drawLine(20f + xOffset, 26f + yBob, 12f + xOffset, 10f + yBob, paint)
                        canvas.drawLine(44f + xOffset, 26f + yBob, 52f + xOffset, 10f + yBob, paint)
                    }
                }
                // Avian / Bird / Winged Digimon
                speciesId in listOf(206, 225, 309, 320, 323, 328, 344, 407, 417, 422, 437, 506, 545) -> {
                    canvas.drawCircle(32f + xOffset, 24f + yBob, 10f, paint)
                    canvas.drawRect(24f + xOffset, 34f + yBob, 40f + xOffset, 50f + yBob, paint)
                    canvas.drawLine(24f + xOffset, 36f + yBob, 8f + xOffset, 22f + yBob, paint)
                    canvas.drawLine(40f + xOffset, 36f + yBob, 56f + xOffset, 22f + yBob, paint)
                }
                // Serpent / Aquatic
                speciesId in listOf(202, 210, 314, 324, 327, 409, 508, 533) -> {
                    canvas.drawRoundRect(14f + xOffset, 28f + yBob, 50f + xOffset, 48f + yBob, 8f, 8f, paint)
                    canvas.drawCircle(44f + xOffset, 26f + yBob, 8f, paint)
                }
                // Heavy Mech / Artillery
                speciesId in listOf(418, 502, 518, 542, 546, 548, 554, 441) -> {
                    canvas.drawRect(20f + xOffset, 24f + yBob, 44f + xOffset, 48f + yBob, paint)
                    canvas.drawRect(14f + xOffset, 10f + yBob, 20f + xOffset, 24f + yBob, paint)
                    canvas.drawRect(38f + xOffset, 10f + yBob, 44f + xOffset, 24f + yBob, paint)
                }
                // Default Bipedal / Dino / Warrior / Knight
                else -> {
                    canvas.drawRect(26f + xOffset, 14f + yBob, 46f + xOffset, 30f + yBob, paint)
                    canvas.drawRect(22f + xOffset, 30f + yBob, 44f + xOffset, 48f + yBob, paint)
                    canvas.drawLine(26f + xOffset, 48f + yBob, 26f + xOffset, 56f + yBob, paint)
                    canvas.drawLine(40f + xOffset, 48f + yBob, 40f + xOffset, 56f + yBob, paint)
                }
            }

            if (frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B) {
                paint.strokeWidth = 1.5f
                canvas.drawLine(44f, 12f, 52f, 12f, paint)
                canvas.drawLine(52f, 12f, 44f, 20f, paint)
                canvas.drawLine(44f, 20f, 52f, 20f, paint)
            } else {
                paint.style = Paint.Style.FILL
                canvas.drawRect(34f + xOffset, 20f + yBob, 37f + xOffset, 23f + yBob, paint)
            }
        } else {
            // FULL 24-BIT RGB COLOR SPRITE RENDERING
            paint.style = Paint.Style.FILL

            when {
                // ==========================================
                // Slime / Baby species
                // ==========================================
                speciesId in 101..139 -> {
                    paint.color = palette.primary
                    canvas.drawCircle(32f + xOffset, 36f + yBob, 16f, paint)

                    paint.color = palette.secondary
                    when (speciesId) {
                        102 -> { // Koromon long ears
                            paint.style = Paint.Style.STROKE
                            paint.strokeWidth = 4f
                            canvas.drawLine(20f + xOffset, 26f + yBob, 12f + xOffset, 10f + yBob, paint)
                            canvas.drawLine(44f + xOffset, 26f + yBob, 52f + xOffset, 10f + yBob, paint)
                            paint.style = Paint.Style.FILL
                        }
                        103 -> { // Punimon 3 horns
                            canvas.drawCircle(24f + xOffset, 22f + yBob, 3.5f, paint)
                            canvas.drawCircle(32f + xOffset, 18f + yBob, 4f, paint)
                            canvas.drawCircle(40f + xOffset, 22f + yBob, 3.5f, paint)
                        }
                        104 -> { // Tsunomon horn
                            val horn = Path().apply {
                                moveTo(28f + xOffset, 22f + yBob)
                                lineTo(36f + xOffset, 22f + yBob)
                                lineTo(32f + xOffset, 8f + yBob)
                                close()
                            }
                            canvas.drawPath(horn, paint)
                        }
                        106 -> { // Tokomon bat ears
                            canvas.drawRect(14f + xOffset, 24f + yBob, 20f + xOffset, 32f + yBob, paint)
                            canvas.drawRect(44f + xOffset, 24f + yBob, 50f + xOffset, 32f + yBob, paint)
                        }
                        108 -> { // Tanemon leaves
                            paint.color = palette.tertiary
                            canvas.drawCircle(30f + xOffset, 18f + yBob, 5f, paint)
                            canvas.drawCircle(36f + xOffset, 18f + yBob, 5f, paint)
                        }
                        110, 119 -> { // Pagumon / Yaamon horns/ears
                            paint.style = Paint.Style.STROKE
                            paint.strokeWidth = 3f
                            canvas.drawLine(22f + xOffset, 24f + yBob, 16f + xOffset, 12f + yBob, paint)
                            canvas.drawLine(42f + xOffset, 24f + yBob, 48f + xOffset, 12f + yBob, paint)
                            paint.style = Paint.Style.FILL
                        }
                        111, 112 -> { // Mokumon / DemiMeramon fire/smoke wisps
                            paint.color = palette.tertiary
                            canvas.drawCircle(32f + xOffset, 16f + yBob, 6f, paint)
                            canvas.drawCircle(24f + xOffset, 20f + yBob, 4f, paint)
                            canvas.drawCircle(40f + xOffset, 20f + yBob, 4f, paint)
                        }
                        114, 115, 116, 117, 118 -> { // Small head crest / ears
                            canvas.drawCircle(22f + xOffset, 24f + yBob, 4f, paint)
                            canvas.drawCircle(42f + xOffset, 24f + yBob, 4f, paint)
                        }
                        130 -> { // Nyokimon green twin sprout
                            paint.color = palette.tertiary
                            canvas.drawRect(31f + xOffset, 12f + yBob, 33f + xOffset, 22f + yBob, paint)
                            canvas.drawCircle(27f + xOffset, 14f + yBob, 4f, paint)
                            canvas.drawCircle(37f + xOffset, 14f + yBob, 4f, paint)
                        }
                        131 -> { // Pichimon cheek fins and tail
                            paint.color = palette.secondary
                            canvas.drawCircle(18f + xOffset, 36f + yBob, 4.5f, paint)
                            canvas.drawCircle(46f + xOffset, 36f + yBob, 4.5f, paint)
                            canvas.drawRect(30f + xOffset, 48f + yBob, 34f + xOffset, 54f + yBob, paint)
                        }
                        132 -> { // Pukamon dorsal crest tuft
                            paint.color = palette.secondary
                            canvas.drawRect(30f + xOffset, 12f + yBob, 34f + xOffset, 22f + yBob, paint)
                            canvas.drawCircle(32f + xOffset, 12f + yBob, 4.5f, paint)
                        }
                        120 -> { // Nyaromon cat ears and striped tail
                            val earL = Path().apply {
                                moveTo(22f + xOffset, 24f + yBob)
                                lineTo(26f + xOffset, 12f + yBob)
                                lineTo(18f + xOffset, 16f + yBob)
                                close()
                            }
                            val earR = Path().apply {
                                moveTo(42f + xOffset, 24f + yBob)
                                lineTo(38f + xOffset, 12f + yBob)
                                lineTo(46f + xOffset, 16f + yBob)
                                close()
                            }
                            canvas.drawPath(earL, paint)
                            canvas.drawPath(earR, paint)
                            paint.color = palette.secondary
                            canvas.drawRect(44f + xOffset, 38f + yBob, 54f + xOffset, 44f + yBob, paint)
                        }
                        121 -> { // Pyocomon flower bloom
                            paint.color = palette.secondary
                            canvas.drawCircle(32f + xOffset, 14f + yBob, 7f, paint)
                            canvas.drawCircle(26f + xOffset, 18f + yBob, 5f, paint)
                            canvas.drawCircle(38f + xOffset, 18f + yBob, 5f, paint)
                            paint.color = palette.tertiary
                            canvas.drawRect(31f + xOffset, 18f + yBob, 33f + xOffset, 22f + yBob, paint)
                        }
                        122 -> { // Dorimon furry ears and forehead tuft
                            canvas.drawLine(24f + xOffset, 24f + yBob, 18f + xOffset, 12f + yBob, paint)
                            canvas.drawLine(40f + xOffset, 24f + yBob, 46f + xOffset, 12f + yBob, paint)
                            paint.color = palette.tertiary
                            canvas.drawRect(30f + xOffset, 14f + yBob, 34f + xOffset, 22f + yBob, paint)
                        }
                        else -> {
                            paint.color = palette.secondary
                            canvas.drawCircle(26f + xOffset, 30f + yBob, 4f, paint)
                        }
                    }

                    // Eyes
                    paint.color = palette.eye
                    if (frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B) {
                        paint.color = Color.BLACK
                        paint.strokeWidth = 2f
                        canvas.drawLine(26f + xOffset, 34f + yBob, 30f + xOffset, 34f + yBob, paint)
                        canvas.drawLine(36f + xOffset, 34f + yBob, 40f + xOffset, 34f + yBob, paint)
                    } else if (speciesId == 113) { // Kuramon single eye
                        canvas.drawCircle(32f + xOffset, 34f + yBob, 5f, paint)
                        paint.color = Color.BLACK
                        canvas.drawCircle(32f + xOffset, 34f + yBob, 2f, paint)
                    } else {
                        canvas.drawCircle(28f + xOffset, 34f + yBob, 3f, paint)
                        canvas.drawCircle(38f + xOffset, 34f + yBob, 3f, paint)
                    }
                }

                // ==========================================
                // Spherical / Mutant (Mamemon, Monzaemon, Sukamon, Raremon, Nanimon, Vademon, Digitamamon, Armaggeddemon)
                // ==========================================
                speciesId in listOf(402, 412, 413, 315, 321, 325, 415, 419, 420, 421, 442, 528, 529, 547) -> {
                    paint.color = palette.primary
                    if (speciesId == 442) { // Digitamamon cracked egg shell with glowing eyes & lizard feet
                        paint.color = palette.primary // egg shell
                        canvas.drawRoundRect(18f + xOffset, 14f + yBob, 46f + xOffset, 48f + yBob, 14f, 14f, paint)
                        paint.color = palette.secondary // dark interior crack
                        canvas.drawRect(22f + xOffset, 28f + yBob, 42f + xOffset, 34f + yBob, paint)
                        paint.color = palette.tertiary // golden eyes inside crack
                        canvas.drawCircle(28f + xOffset, 31f + yBob, 2.5f, paint)
                        canvas.drawCircle(36f + xOffset, 31f + yBob, 2.5f, paint)
                        paint.color = palette.eye
                        canvas.drawCircle(28f + xOffset, 31f + yBob, 1f, paint)
                        canvas.drawCircle(36f + xOffset, 31f + yBob, 1f, paint)
                        paint.color = Color.rgb(85, 145, 65) // green dinosaur feet
                        canvas.drawRect(22f + xOffset, 48f + yBob, 28f + xOffset, 55f + yBob, paint)
                        canvas.drawRect(36f + xOffset, 48f + yBob, 42f + xOffset, 55f + yBob, paint)
                    } else if (speciesId == 315) { // Sukamon coil
                        canvas.drawCircle(32f + xOffset, 42f + yBob, 16f, paint)
                        paint.color = palette.secondary
                        canvas.drawCircle(32f + xOffset, 30f + yBob, 12f, paint)
                        paint.color = palette.tertiary
                        canvas.drawCircle(32f + xOffset, 20f + yBob, 7f, paint)
                    } else {
                        canvas.drawCircle(32f + xOffset, 34f + yBob, 18f, paint)
                        // Boxing gloves / Hands / Blasters
                        paint.color = palette.secondary
                        canvas.drawCircle(14f + xOffset, 38f + yBob, 7f, paint)
                        if (speciesId in listOf(413, 419, 420)) { // Blasters / Chainsaw
                            paint.color = palette.tertiary
                            canvas.drawRect(48f + xOffset, 34f + yBob, 58f + xOffset, 42f + yBob, paint)
                        } else {
                            canvas.drawCircle(50f + xOffset, 38f + yBob, 7f, paint)
                        }
                        // Feet
                        paint.color = palette.secondary
                        canvas.drawRect(22f + xOffset, 48f + yBob, 28f + xOffset, 55f + yBob, paint)
                        canvas.drawRect(36f + xOffset, 48f + yBob, 42f + xOffset, 55f + yBob, paint)
                    }

                    // Special features for mutants
                    if (speciesId == 325) { // Nanimon sunglasses & lips
                        paint.color = Color.BLACK
                        canvas.drawRect(24f + xOffset, 28f + yBob, 40f + xOffset, 34f + yBob, paint)
                        paint.color = palette.tertiary
                        canvas.drawRect(28f + xOffset, 38f + yBob, 36f + xOffset, 42f + yBob, paint)
                    } else if (speciesId != 442) {
                        // Eyes
                        paint.color = palette.eye
                        canvas.drawCircle(28f + xOffset, 30f + yBob, 3f, paint)
                        canvas.drawCircle(38f + xOffset, 30f + yBob, 3f, paint)
                    }
                }

                // ==========================================
                // Avian / Bird / Winged Digimon
                // ==========================================
                speciesId in listOf(206, 225, 309, 320, 323, 328, 344, 407, 417, 422, 437, 506, 545) -> {
                    paint.color = palette.primary
                    // Torso & Tail
                    canvas.drawRoundRect(24f + xOffset, 28f + yBob, 44f + xOffset, 48f + yBob, 6f, 6f, paint)
                    // Head & Beak
                    canvas.drawCircle(34f + xOffset, 22f + yBob, 9f, paint)
                    paint.color = palette.secondary
                    val beak = Path().apply {
                        moveTo(42f + xOffset, 20f + yBob)
                        lineTo(52f + xOffset, 23f + yBob)
                        lineTo(42f + xOffset, 26f + yBob)
                        close()
                    }
                    canvas.drawPath(beak, paint)

                    // Large Wings
                    val wingL = Path().apply {
                        moveTo(26f + xOffset, 32f + yBob)
                        lineTo(6f + xOffset, 14f + yBob)
                        lineTo(16f + xOffset, 42f + yBob)
                        close()
                    }
                    val wingR = Path().apply {
                        moveTo(42f + xOffset, 32f + yBob)
                        lineTo(58f + xOffset, 14f + yBob)
                        lineTo(48f + xOffset, 42f + yBob)
                        close()
                    }
                    canvas.drawPath(wingL, paint)
                    canvas.drawPath(wingR, paint)

                    // Feet
                    paint.color = palette.tertiary
                    canvas.drawRect(26f + xOffset, 48f + yBob, 30f + xOffset, 56f + yBob, paint)
                    canvas.drawRect(38f + xOffset, 48f + yBob, 42f + xOffset, 56f + yBob, paint)

                    // Eye
                    paint.color = palette.eye
                    canvas.drawCircle(36f + xOffset, 20f + yBob, 2.5f, paint)
                }

                // ==========================================
                // Aquatic / Serpent / Holy Dragon (Betamon, Gizamon, Seadramon, Plesiomon, Leviamon, Magnadramon)
                // ==========================================
                speciesId in listOf(202, 210, 314, 324, 327, 409, 508, 533, 556) -> {
                    if (speciesId == 556) { // Magnadramon pink holy dragon body & 10 golden wings
                        paint.color = palette.primary // Rose pink dragon body
                        canvas.drawRoundRect(14f + xOffset, 30f + yBob, 52f + xOffset, 48f + yBob, 8f, 8f, paint)
                        paint.color = palette.secondary // Cream/golden belly & mane
                        canvas.drawRoundRect(20f + xOffset, 38f + yBob, 46f + xOffset, 46f + yBob, 4f, 4f, paint)
                        canvas.drawRoundRect(34f + xOffset, 14f + yBob, 52f + xOffset, 34f + yBob, 6f, 6f, paint) // Dragon/lion head
                        paint.color = palette.tertiary // Golden horn crests & 10 wings
                        canvas.drawLine(44f + xOffset, 14f + yBob, 48f + xOffset, 4f + yBob, paint)
                        canvas.drawLine(48f + xOffset, 14f + yBob, 54f + xOffset, 6f + yBob, paint)
                        canvas.drawLine(24f + xOffset, 32f + yBob, 6f + xOffset, 14f + yBob, paint)
                        canvas.drawLine(28f + xOffset, 32f + yBob, 12f + xOffset, 8f + yBob, paint)
                        canvas.drawLine(34f + xOffset, 32f + yBob, 20f + xOffset, 6f + yBob, paint)
                        canvas.drawLine(40f + xOffset, 32f + yBob, 56f + xOffset, 8f + yBob, paint)
                        canvas.drawLine(44f + xOffset, 32f + yBob, 62f + xOffset, 14f + yBob, paint)
                        paint.color = palette.eye // Sacred azure eye
                        canvas.drawCircle(44f + xOffset, 20f + yBob, 2.5f, paint)
                    } else {
                        paint.color = palette.primary
                        // Serpentine body
                        canvas.drawRoundRect(14f + xOffset, 32f + yBob, 52f + xOffset, 48f + yBob, 8f, 8f, paint)
                        // Neck & Head
                        canvas.drawRoundRect(36f + xOffset, 16f + yBob, 52f + xOffset, 34f + yBob, 6f, 6f, paint)

                        // Horned mask / Fin
                        paint.color = palette.secondary
                        canvas.drawRect(42f + xOffset, 8f + yBob, 46f + xOffset, 20f + yBob, paint)
                        // Fins
                        paint.color = palette.tertiary
                        canvas.drawCircle(22f + xOffset, 48f + yBob, 6f, paint)
                        canvas.drawCircle(44f + xOffset, 48f + yBob, 6f, paint)

                        // Eye
                        paint.color = palette.eye
                        canvas.drawCircle(44f + xOffset, 22f + yBob, 3f, paint)
                    }
                }

                // ==========================================
                // Bipedal Dinosaur, Beast, Holy, Mech, Boss Digimon
                // ==========================================
                else -> {
                    // Torso
                    paint.color = palette.primary
                    canvas.drawRoundRect(22f + xOffset, 28f + yBob, 44f + xOffset, 48f + yBob, 6f, 6f, paint)

                    // Head
                    paint.color = palette.primary
                    canvas.drawRoundRect(26f + xOffset, 14f + yBob, 48f + xOffset, 32f + yBob, 4f, 4f, paint)

                    // Characteristic Secondary Features
                    paint.color = palette.secondary
                    when (speciesId) {
                        301, 316, 339, 401, 414, 429, 501, 525, 526 -> { // Greymon skull helmet & horns / Dramon Killers
                            canvas.drawRect(26f + xOffset, 14f + yBob, 42f + xOffset, 22f + yBob, paint)
                            canvas.drawLine(24f + xOffset, 14f + yBob, 18f + xOffset, 6f + yBob, paint)
                            canvas.drawLine(42f + xOffset, 14f + yBob, 48f + xOffset, 6f + yBob, paint)
                            if (speciesId in listOf(401, 414, 429)) { // MetalGreymon / MetalTyrannomon cyber wing & trident arm
                                paint.color = palette.tertiary
                                canvas.drawRect(12f + xOffset, 22f + yBob, 24f + xOffset, 38f + yBob, paint)
                            } else if (speciesId in listOf(501, 525, 526)) { // WarGreymon Dramon Killers & Shield
                                paint.color = palette.tertiary
                                canvas.drawCircle(18f + xOffset, 32f + yBob, 6f, paint)
                                canvas.drawCircle(46f + xOffset, 32f + yBob, 6f, paint)
                            }
                        }
                        306, 340, 404, 430, 503 -> { // Garurumon wolf ears & stripes
                            canvas.drawLine(24f + xOffset, 16f + yBob, 20f + xOffset, 6f + yBob, paint)
                            canvas.drawLine(34f + xOffset, 16f + yBob, 38f + xOffset, 6f + yBob, paint)
                            paint.color = palette.tertiary
                            canvas.drawRect(24f + xOffset, 36f + yBob, 32f + xOffset, 40f + yBob, paint)
                        }
                        303, 317, 326, 410, 428, 509, 519, 520, 530, 531, 532 -> { // Devimon / Vampire / Demon Lords bat wings
                            val wingL = Path().apply {
                                moveTo(24f + xOffset, 30f + yBob)
                                lineTo(6f + xOffset, 16f + yBob)
                                lineTo(14f + xOffset, 38f + yBob)
                                close()
                            }
                            val wingR = Path().apply {
                                moveTo(44f + xOffset, 30f + yBob)
                                lineTo(60f + xOffset, 16f + yBob)
                                lineTo(52f + xOffset, 38f + yBob)
                                close()
                            }
                            canvas.drawPath(wingL, paint)
                            canvas.drawPath(wingR, paint)
                        }
                        221, 307, 405, 427, 504, 514, 515, 516, 521, 537 -> { // Holy Angel wings & visor / Paladin
                            paint.color = palette.secondary
                            canvas.drawRect(30f + xOffset, 16f + yBob, 44f + xOffset, 22f + yBob, paint) // Visor
                            paint.color = Color.rgb(240, 240, 255)
                            canvas.drawLine(22f + xOffset, 32f + yBob, 8f + xOffset, 16f + yBob, paint)
                            canvas.drawLine(44f + xOffset, 32f + yBob, 58f + xOffset, 16f + yBob, paint)
                        }
                        308, 406, 505 -> { // Cactus / Flower gloves & petals
                            canvas.drawCircle(16f + xOffset, 38f + yBob, 6f, paint)
                            canvas.drawCircle(48f + xOffset, 38f + yBob, 6f, paint)
                        }
                        208, 211, 213, 310, 322, 333, 408, 507 -> { // Beetle stag horn & carapace
                            val horn = Path().apply {
                                moveTo(32f + xOffset, 14f + yBob)
                                lineTo(26f + xOffset, 4f + yBob)
                                lineTo(38f + xOffset, 4f + yBob)
                                close()
                            }
                            canvas.drawPath(horn, paint)
                        }
                        312 -> { // Ogremon club
                            paint.color = palette.tertiary
                            canvas.drawRect(46f + xOffset, 22f + yBob, 52f + xOffset, 46f + yBob, paint)
                        }
                        313 -> { // Leomon mane & sword
                            canvas.drawCircle(32f + xOffset, 22f + yBob, 12f, paint)
                            paint.color = palette.tertiary
                            canvas.drawLine(18f + xOffset, 42f + yBob, 14f + xOffset, 54f + yBob, paint)
                        }
                        337 -> { // Wizardmon wizard hat & cape
                            paint.color = palette.secondary
                            val hat = Path().apply {
                                moveTo(26f + xOffset, 16f + yBob)
                                lineTo(46f + xOffset, 16f + yBob)
                                lineTo(36f + xOffset, 4f + yBob)
                                close()
                            }
                            canvas.drawPath(hat, paint)
                        }
                        338 -> { // Gatomon oversized green claw gloves & ring tail
                            paint.color = palette.secondary // Lime green claw gloves
                            canvas.drawCircle(16f + xOffset, 38f + yBob, 6f, paint)
                            canvas.drawCircle(48f + xOffset, 38f + yBob, 6f, paint)
                            paint.color = palette.tertiary // Pink claw pads & tail stripe
                            canvas.drawCircle(16f + xOffset, 38f + yBob, 2.5f, paint)
                            canvas.drawCircle(48f + xOffset, 38f + yBob, 2.5f, paint)
                            canvas.drawRect(46f + xOffset, 42f + yBob, 56f + xOffset, 46f + yBob, paint)
                        }
                        403 -> { // SkullGreymon ribcage & missile
                            paint.color = palette.tertiary
                            canvas.drawRect(30f + xOffset, 10f + yBob, 34f + xOffset, 24f + yBob, paint) // Spine missile
                        }
                        418, 502, 518, 542, 546 -> { // Machinedramon / MegaGargomon dual Giga Cannons
                            paint.color = palette.secondary
                            canvas.drawRect(16f + xOffset, 6f + yBob, 22f + xOffset, 28f + yBob, paint)
                            canvas.drawRect(42f + xOffset, 6f + yBob, 48f + xOffset, 28f + yBob, paint)
                        }
                        510, 535, 536 -> { // Omnimon / Alter-S / Zwart crimson cape, Grey Sword & Garuru Cannon
                            paint.color = Color.rgb(200, 30, 40)
                            canvas.drawRect(18f + xOffset, 28f + yBob, 24f + xOffset, 48f + yBob, paint)
                            paint.color = Color.rgb(255, 140, 20)
                            canvas.drawCircle(16f + xOffset, 36f + yBob, 6f, paint)
                            paint.color = Color.rgb(220, 235, 250)
                            canvas.drawRect(6f + xOffset, 34f + yBob, 14f + xOffset, 38f + yBob, paint) // Grey Sword blade
                            paint.color = Color.rgb(60, 120, 220)
                            canvas.drawCircle(48f + xOffset, 36f + yBob, 6f, paint)
                            paint.color = Color.rgb(40, 50, 75)
                            canvas.drawRect(50f + xOffset, 33f + yBob, 58f + xOffset, 39f + yBob, paint) // Garuru Cannon barrel
                        }
                        512, 513 -> { // Imperialdramon Positron Laser
                            paint.color = palette.tertiary
                            canvas.drawRect(44f + xOffset, 26f + yBob, 58f + xOffset, 34f + yBob, paint)
                        }
                        223, 224, 227, 342, 346, 347, 439, 555 -> { // Beast ears & special marks
                            if (speciesId == 223) { // Salamon floppy puppy ears & golden holy ring collar
                                paint.color = palette.secondary
                                canvas.drawRoundRect(20f + xOffset, 16f + yBob, 26f + xOffset, 30f + yBob, 3f, 3f, paint)
                                canvas.drawRoundRect(42f + xOffset, 16f + yBob, 48f + xOffset, 30f + yBob, 3f, 3f, paint)
                                paint.color = palette.tertiary // Golden Holy Ring collar
                                canvas.drawRect(28f + xOffset, 28f + yBob, 38f + xOffset, 31f + yBob, paint)
                            } else {
                                canvas.drawLine(26f + xOffset, 16f + yBob, 22f + xOffset, 6f + yBob, paint)
                                canvas.drawLine(38f + xOffset, 16f + yBob, 42f + xOffset, 6f + yBob, paint)
                                paint.color = palette.tertiary
                                if (speciesId in listOf(227, 342)) { // Dorumon / Dorugamon Red Digicore Interface Gem
                                    val gem = Path().apply {
                                        moveTo(32f + xOffset, 16f + yBob)
                                        lineTo(29f + xOffset, 21f + yBob)
                                        lineTo(35f + xOffset, 21f + yBob)
                                        close()
                                    }
                                    canvas.drawPath(gem, paint)
                                } else {
                                    canvas.drawRect(22f + xOffset, 38f + yBob, 30f + xOffset, 42f + yBob, paint)
                                }
                            }
                        }
                        228, 343, 436, 551 -> { // Kotemon / Sorcerymon / Knightmon / Gaiomon helmet, blade & cape
                            if (speciesId == 228) { // Kotemon kendo helmet grill (men) & bamboo shinai
                                paint.color = Color.rgb(55, 55, 70)
                                canvas.drawRect(30f + xOffset, 18f + yBob, 38f + xOffset, 26f + yBob, paint)
                                paint.color = Color.rgb(240, 240, 245)
                                canvas.drawLine(34f + xOffset, 18f + yBob, 34f + xOffset, 26f + yBob, paint)
                                paint.color = Color.rgb(215, 180, 100) // Bamboo shinai sword
                                canvas.drawLine(44f + xOffset, 36f + yBob, 54f + xOffset, 14f + yBob, paint)
                            } else {
                                paint.color = palette.tertiary
                                canvas.drawRect(26f + xOffset, 12f + yBob, 44f + xOffset, 17f + yBob, paint)
                                canvas.drawLine(46f + xOffset, 36f + yBob, 56f + xOffset, 16f + yBob, paint)
                                paint.color = palette.secondary
                                canvas.drawRect(18f + xOffset, 30f + yBob, 23f + xOffset, 48f + yBob, paint)
                            }
                        }
                        226, 345, 438, 553 -> { // Armadillomon / Ankylomon / Zudomon / Vikemon shell, claws & hammer
                            canvas.drawRoundRect(14f + xOffset, 26f + yBob, 26f + xOffset, 46f + yBob, 5f, 5f, paint)
                            paint.color = palette.tertiary
                            if (speciesId == 345) { // Ankylomon spiked tail mace
                                canvas.drawCircle(10f + xOffset, 40f + yBob, 6f, paint)
                                canvas.drawRect(8f + xOffset, 38f + yBob, 12f + xOffset, 42f + yBob, paint)
                            } else {
                                canvas.drawRect(46f + xOffset, 20f + yBob, 56f + xOffset, 30f + yBob, paint)
                                canvas.drawRect(50f + xOffset, 30f + yBob, 52f + xOffset, 50f + yBob, paint)
                            }
                        }
                        229, 231, 435, 550 -> { // Monodramon / Goblimon / Dorugreymon / Dorugoramon horns, claws & cannons
                            if (speciesId == 229) { // Monodramon yellow wings & crimson knuckle spikes
                                paint.color = palette.secondary // Yellow wing membranes
                                canvas.drawRoundRect(14f + xOffset, 24f + yBob, 24f + xOffset, 38f + yBob, 4f, 4f, paint)
                                canvas.drawRoundRect(42f + xOffset, 24f + yBob, 52f + xOffset, 38f + yBob, 4f, 4f, paint)
                                paint.color = palette.tertiary // Crimson knuckle bands
                                canvas.drawCircle(18f + xOffset, 38f + yBob, 4f, paint)
                                canvas.drawCircle(46f + xOffset, 38f + yBob, 4f, paint)
                                paint.color = Color.rgb(240, 240, 245)
                                canvas.drawLine(34f + xOffset, 14f + yBob, 34f + xOffset, 6f + yBob, paint)
                            } else if (speciesId in listOf(435, 550)) { // Dorugreymon / Dorugoramon Red Digicore Interface & horns
                                canvas.drawLine(28f + xOffset, 14f + yBob, 24f + xOffset, 4f + yBob, paint)
                                canvas.drawLine(40f + xOffset, 14f + yBob, 44f + xOffset, 4f + yBob, paint)
                                paint.color = Color.rgb(225, 40, 50)
                                val gem = Path().apply {
                                    moveTo(34f + xOffset, 16f + yBob)
                                    lineTo(31f + xOffset, 21f + yBob)
                                    lineTo(37f + xOffset, 21f + yBob)
                                    close()
                                }
                                canvas.drawPath(gem, paint)
                            } else {
                                canvas.drawLine(28f + xOffset, 14f + yBob, 24f + xOffset, 4f + yBob, paint)
                                canvas.drawLine(40f + xOffset, 14f + yBob, 44f + xOffset, 4f + yBob, paint)
                                paint.color = palette.tertiary
                                canvas.drawCircle(16f + xOffset, 36f + yBob, 5f, paint)
                                canvas.drawCircle(48f + xOffset, 36f + yBob, 5f, paint)
                            }
                        }
                        230, 443 -> { // Floramon / Cherrymon bloom & tree-trunk arms
                            paint.color = palette.tertiary
                            canvas.drawCircle(32f + xOffset, 10f + yBob, 6f, paint)
                            canvas.drawCircle(24f + xOffset, 14f + yBob, 4f, paint)
                            canvas.drawCircle(40f + xOffset, 14f + yBob, 4f, paint)
                            paint.color = palette.secondary
                            canvas.drawRect(16f + xOffset, 34f + yBob, 22f + xOffset, 44f + yBob, paint)
                            canvas.drawRect(46f + xOffset, 34f + yBob, 52f + xOffset, 44f + yBob, paint)
                        }
                        440, 349 -> { // Phantomon / Dokugumon reaper cloak & spider legs
                            val cloakL = Path().apply {
                                moveTo(24f + xOffset, 30f + yBob)
                                lineTo(6f + xOffset, 50f + yBob)
                                lineTo(18f + xOffset, 46f + yBob)
                                close()
                            }
                            val cloakR = Path().apply {
                                moveTo(44f + xOffset, 30f + yBob)
                                lineTo(60f + xOffset, 50f + yBob)
                                lineTo(50f + xOffset, 46f + yBob)
                                close()
                            }
                            canvas.drawPath(cloakL, paint)
                            canvas.drawPath(cloakR, paint)
                        }
                        348, 441 -> { // Starmon star / Shakkoumon ancient clay plating
                            if (speciesId == 441) { // Shakkoumon ancient clay figure & sacred markings
                                paint.color = palette.secondary // Silver-gray sacred plating
                                canvas.drawCircle(32f + xOffset, 24f + yBob, 8f, paint)
                                paint.color = palette.tertiary // Red sacred markings & eye slits
                                canvas.drawCircle(32f + xOffset, 38f + yBob, 5f, paint)
                                canvas.drawLine(26f + xOffset, 22f + yBob, 38f + xOffset, 22f + yBob, paint)
                                paint.color = palette.primary
                                canvas.drawCircle(18f + xOffset, 24f + yBob, 4f, paint) // Ear disks
                                canvas.drawCircle(46f + xOffset, 24f + yBob, 4f, paint)
                            } else {
                                paint.color = palette.tertiary
                                canvas.drawCircle(34f + xOffset, 8f + yBob, 5f, paint)
                                canvas.drawRect(30f + xOffset, 2f + yBob, 38f + xOffset, 14f + yBob, paint)
                                paint.color = palette.secondary
                                canvas.drawRect(16f + xOffset, 6f + yBob, 22f + xOffset, 28f + yBob, paint)
                                canvas.drawRect(42f + xOffset, 6f + yBob, 48f + xOffset, 28f + yBob, paint)
                            }
                        }
                        552, 557 -> { // Ophanimon / Cherubimon angelic wings & halo
                            paint.color = Color.rgb(240, 240, 255)
                            canvas.drawLine(22f + xOffset, 32f + yBob, 6f + xOffset, 12f + yBob, paint)
                            canvas.drawLine(44f + xOffset, 32f + yBob, 60f + xOffset, 12f + yBob, paint)
                            paint.color = palette.tertiary
                            canvas.drawRect(28f + xOffset, 8f + yBob, 44f + xOffset, 11f + yBob, paint)
                        }
                        554 -> { // Chaosdramon dual chrome Hades Cannons & gunmetal chassis
                            paint.color = palette.tertiary // Chrome cannons
                            canvas.drawRect(14f + xOffset, 4f + yBob, 20f + xOffset, 30f + yBob, paint)
                            canvas.drawRect(44f + xOffset, 4f + yBob, 50f + xOffset, 30f + yBob, paint)
                            paint.color = palette.secondary // Gunmetal muzzle caps
                            canvas.drawRect(13f + xOffset, 2f + yBob, 21f + xOffset, 6f + yBob, paint)
                            canvas.drawRect(43f + xOffset, 2f + yBob, 51f + xOffset, 6f + yBob, paint)
                        }
                        522, 523, 524, 538, 549 -> { // Royal Knights blades & energy weapons
                            paint.color = palette.tertiary
                            canvas.drawLine(44f + xOffset, 34f + yBob, 56f + xOffset, 18f + yBob, paint)
                        }
                    }

                    // Legs
                    paint.color = palette.primary
                    canvas.drawRect(24f + xOffset, 48f + yBob, 30f + xOffset, 56f + yBob, paint)
                    canvas.drawRect(36f + xOffset, 48f + yBob, 42f + xOffset, 56f + yBob, paint)

                    // Eyes
                    paint.color = palette.eye
                    if (frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B) {
                        paint.color = Color.BLACK
                        canvas.drawLine(36f + xOffset, 20f + yBob, 42f + xOffset, 20f + yBob, paint)
                    } else {
                        canvas.drawRect(38f + xOffset, 18f + yBob, 42f + xOffset, 22f + yBob, paint)
                    }
                }
            }

            // ==========================================
            // Universal Authentic V-Pet Action & Status Overlays
            // ==========================================
            if (frame == SpriteFrame.FEED_START || frame == SpriteFrame.JOY_EAT) {
                paint.color = Color.rgb(245, 240, 230)
                canvas.drawRect(48f + xOffset, 25f + yBob, 58f + xOffset, 27f + yBob, paint)
                paint.color = Color.rgb(205, 55, 45)
                canvas.drawCircle(53f + xOffset, 26f + yBob, 4f, paint)
                if (frame == SpriteFrame.JOY_EAT) {
                    paint.color = Color.rgb(255, 65, 110)
                    canvas.drawRect(29f + xOffset, 5f + yBob, 32f + xOffset, 8f + yBob, paint)
                    canvas.drawRect(33f + xOffset, 5f + yBob, 36f + xOffset, 8f + yBob, paint)
                    canvas.drawRect(30f + xOffset, 8f + yBob, 35f + xOffset, 11f + yBob, paint)
                }
            }

            if (frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B) {
                paint.color = Color.rgb(130, 215, 255)
                val zShift = if (frame == SpriteFrame.SLEEP_B) -3f else 0f
                canvas.drawRect(46f, 10f + zShift, 54f, 12f + zShift, paint)
                canvas.drawLine(53f, 12f + zShift, 47f, 18f + zShift, paint)
                canvas.drawRect(46f, 17f + zShift, 54f, 19f + zShift, paint)
            }

            if (frame == SpriteFrame.INJURED) {
                paint.color = Color.rgb(255, 60, 60)
                canvas.drawRect(9f, 8f, 13f, 18f, paint)
                canvas.drawRect(6f, 11f, 16f, 15f, paint)
            }

            if (frame == SpriteFrame.REFUSAL) {
                paint.color = Color.rgb(80, 195, 255)
                canvas.drawRect(50f, 10f, 53f, 16f, paint)
                canvas.drawRect(49f, 13f, 54f, 17f, paint)
            }

            if (frame == SpriteFrame.ATTACK_RELEASE) {
                paint.color = Color.rgb(255, 75, 20)
                canvas.drawCircle(54f, 26f, 7f, paint)
                paint.color = Color.rgb(255, 230, 55)
                canvas.drawCircle(54f, 26f, 4f, paint)
            }
        }
    }
}
