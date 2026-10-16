package com.digiwatch.companion.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.digiwatch.core.model.SpriteFrame

/**
 * Authentic 16x16 / 24x24 dot-matrix pixel art sprite generator supporting all 205 authentic virtual pet species.
 * Recreates authentic Bandai Digital Monster (1997) & Digimon Color pixel matrices for the Phone Companion App.
 * Renders with crisp pixel steps onto 64x64 canvas (FilterQuality.None) and supports low-power monochrome ambient mode.
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
     * Post-processes a 64x64 sprite once at cache-creation time to add an authentic
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
                    val r = ((c shr 16) and 0xFF).let { (it + 24).coerceAtMost(255) }
                    val g = ((c shr 8) and 0xFF).let { (it + 24).coerceAtMost(255) }
                    val b = (c and 0xFF).let { (it + 24).coerceAtMost(255) }
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

    private fun getPalette(speciesId: Int): Palette {
        val sid = if (speciesId <= 1) 201 else speciesId
        return when (sid) {
            // Baby I (101..113)
            101 -> Palette(Color.rgb(28, 28, 34), Color.rgb(55, 55, 68), Color.rgb(80, 80, 95), Color.rgb(255, 230, 0)) // Botamon
            103 -> Palette(Color.rgb(220, 40, 50), Color.rgb(180, 25, 35), Color.rgb(255, 120, 130), Color.rgb(255, 255, 255)) // Punimon
            105 -> Palette(Color.rgb(180, 225, 245), Color.rgb(140, 195, 225), Color.rgb(220, 245, 255), Color.rgb(20, 30, 40)) // Poyomon
            107 -> Palette(Color.rgb(190, 190, 195), Color.rgb(130, 130, 140), Color.rgb(240, 240, 245), Color.rgb(15, 15, 20)) // Yuramon
            109 -> Palette(Color.rgb(45, 55, 45), Color.rgb(75, 90, 70), Color.rgb(115, 135, 105), Color.rgb(255, 215, 0)) // Zurumon
            111 -> Palette(Color.rgb(120, 120, 125), Color.rgb(80, 80, 85), Color.rgb(160, 160, 170), Color.rgb(240, 60, 40)) // Mokumon
            113 -> Palette(Color.rgb(105, 90, 155), Color.rgb(70, 55, 115), Color.rgb(160, 140, 210), Color.rgb(230, 30, 40)) // Kuramon

            // Baby II (102..119)
            102 -> Palette(Color.rgb(255, 145, 175), Color.rgb(225, 80, 115), Color.rgb(255, 190, 210), Color.rgb(220, 20, 30)) // Koromon
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

            // Child (201..222)
            201 -> Palette(Color.rgb(255, 168, 38), Color.rgb(216, 112, 10), Color.rgb(255, 228, 140), Color.rgb(0, 200, 68)) // Agumon
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

            // Adult (301..341)
            301 -> Palette(Color.rgb(255, 135, 20), Color.rgb(62, 39, 35), Color.rgb(21, 101, 192), Color.rgb(0, 176, 255)) // Greymon
            302 -> Palette(Color.rgb(220, 45, 40), Color.rgb(160, 30, 30), Color.rgb(240, 230, 210), Color.rgb(0, 180, 60)) // Tyrannomon
            303 -> Palette(Color.rgb(26, 26, 29), Color.rgb(45, 19, 44), Color.rgb(199, 0, 57), Color.rgb(255, 56, 56)) // Devimon
            304 -> Palette(Color.rgb(255, 90, 25), Color.rgb(220, 40, 10), Color.rgb(255, 210, 40), Color.rgb(255, 255, 255)) // Meramon
            305 -> Palette(Color.rgb(145, 200, 65), Color.rgb(105, 155, 40), Color.rgb(200, 240, 120), Color.rgb(255, 235, 59)) // Numemon
            306 -> Palette(Color.rgb(235, 240, 255), Color.rgb(45, 90, 190), Color.rgb(245, 60, 70), Color.rgb(225, 30, 40)) // Garurumon
            307 -> Palette(Color.rgb(245, 245, 250), Color.rgb(45, 55, 115), Color.rgb(255, 215, 60), Color.rgb(45, 135, 225)) // Angemon
            308 -> Palette(Color.rgb(85, 180, 75), Color.rgb(215, 50, 60), Color.rgb(145, 115, 75), Color.rgb(20, 20, 20)) // Togemon
            309 -> Palette(Color.rgb(245, 75, 40), Color.rgb(245, 210, 50), Color.rgb(40, 40, 45), Color.rgb(255, 255, 255)) // Birdramon
            310 -> Palette(Color.rgb(180, 55, 45), Color.rgb(120, 35, 30), Color.rgb(245, 235, 210), Color.rgb(0, 220, 75)) // Kabuterimon
            311 -> Palette(Color.rgb(175, 120, 60), Color.rgb(235, 195, 125), Color.rgb(245, 245, 250), Color.rgb(20, 20, 25)) // Mojyamon
            312 -> Palette(Color.rgb(105, 175, 80), Color.rgb(165, 115, 55), Color.rgb(225, 215, 195), Color.rgb(220, 35, 35)) // Ogremon
            313 -> Palette(Color.rgb(225, 175, 70), Color.rgb(145, 95, 40), Color.rgb(25, 25, 30), Color.rgb(45, 140, 225)) // Leomon
            314 -> Palette(Color.rgb(55, 125, 220), Color.rgb(245, 220, 60), Color.rgb(245, 245, 250), Color.rgb(220, 35, 40)) // Seadramon
            315 -> Palette(Color.rgb(245, 205, 75), Color.rgb(195, 145, 45), Color.rgb(240, 60, 60), Color.rgb(20, 20, 25)) // Nanimon
            316 -> Palette(Color.rgb(45, 50, 60), Color.rgb(25, 30, 40), Color.rgb(45, 120, 210), Color.rgb(240, 40, 40)) // DarkTyrannomon
            317 -> Palette(Color.rgb(235, 235, 240), Color.rgb(175, 175, 185), Color.rgb(40, 40, 50), Color.rgb(220, 35, 35)) // Bakemon
            318 -> Palette(Color.rgb(120, 120, 125), Color.rgb(80, 80, 85), Color.rgb(225, 45, 45), Color.rgb(255, 215, 0)) // Guardromon
            319 -> Palette(Color.rgb(145, 90, 45), Color.rgb(95, 60, 30), Color.rgb(80, 160, 60), Color.rgb(20, 20, 25)) // Woodmon
            320 -> Palette(Color.rgb(245, 240, 225), Color.rgb(225, 45, 45), Color.rgb(120, 85, 45), Color.rgb(20, 20, 25)) // Kokatorimon
            321 -> Palette(Color.rgb(245, 205, 50), Color.rgb(195, 155, 30), Color.rgb(255, 235, 120), Color.rgb(255, 255, 255)) // Sukamon
            322 -> Palette(Color.rgb(185, 40, 40), Color.rgb(120, 25, 25), Color.rgb(245, 245, 250), Color.rgb(255, 215, 0)) // Kuwagamon
            323 -> Palette(Color.rgb(175, 185, 240), Color.rgb(235, 240, 255), Color.rgb(45, 85, 195), Color.rgb(220, 35, 35)) // Airdramon
            324 -> Palette(Color.rgb(130, 140, 160), Color.rgb(85, 95, 115), Color.rgb(235, 185, 55), Color.rgb(0, 210, 75)) // Coelamon
            325 -> Palette(Color.rgb(135, 165, 125), Color.rgb(85, 115, 75), Color.rgb(215, 115, 145), Color.rgb(220, 35, 35)) // Raremon
            326 -> Palette(Color.rgb(235, 45, 55), Color.rgb(160, 25, 35), Color.rgb(25, 25, 30), Color.rgb(255, 215, 0)) // Fugamon
            327 -> Palette(Color.rgb(240, 240, 245), Color.rgb(245, 75, 45), Color.rgb(35, 35, 40), Color.rgb(255, 215, 0)) // Shellmon
            328 -> Palette(Color.rgb(125, 175, 235), Color.rgb(75, 115, 185), Color.rgb(245, 245, 250), Color.rgb(220, 35, 35)) // Saberdramon
            329 -> Palette(Color.rgb(65, 130, 225), Color.rgb(240, 240, 245), Color.rgb(235, 45, 50), Color.rgb(215, 35, 45)) // ExVeemon
            330 -> Palette(Color.rgb(35, 35, 40), Color.rgb(245, 215, 50), Color.rgb(225, 45, 50), Color.rgb(45, 140, 225)) // Stingmon
            331 -> Palette(Color.rgb(225, 55, 50), Color.rgb(25, 25, 30), Color.rgb(245, 245, 250), Color.rgb(245, 205, 40)) // Growlmon
            332 -> Palette(Color.rgb(245, 245, 250), Color.rgb(115, 65, 155), Color.rgb(225, 45, 45), Color.rgb(45, 140, 225)) // Kyubimon
            333 -> Palette(Color.rgb(90, 175, 80), Color.rgb(245, 245, 240), Color.rgb(175, 125, 70), Color.rgb(25, 25, 30)) // Gargomon
            334 -> Palette(Color.rgb(225, 115, 155), Color.rgb(135, 80, 110), Color.rgb(245, 235, 180), Color.rgb(25, 25, 30)) // Turuiemon
            335 -> Palette(Color.rgb(45, 45, 55), Color.rgb(25, 25, 30), Color.rgb(140, 30, 45), Color.rgb(240, 240, 240)) // IceDevimon
            336 -> Palette(Color.rgb(240, 225, 55), Color.rgb(115, 55, 135), Color.rgb(245, 245, 250), Color.rgb(20, 20, 25)) // Flymon
            337 -> Palette(Color.rgb(145, 135, 130), Color.rgb(95, 85, 80), Color.rgb(225, 45, 45), Color.rgb(255, 215, 0)) // Golemon
            338 -> Palette(Color.rgb(60, 145, 235), Color.rgb(245, 245, 250), Color.rgb(225, 45, 50), Color.rgb(225, 35, 40)) // Veedramon
            339 -> Palette(Color.rgb(45, 50, 60), Color.rgb(35, 35, 40), Color.rgb(45, 110, 200), Color.rgb(240, 40, 40)) // BlackGreymon
            340 -> Palette(Color.rgb(45, 45, 50), Color.rgb(25, 25, 30), Color.rgb(225, 45, 50), Color.rgb(245, 205, 40)) // BlackGarurumon
            341 -> Palette(Color.rgb(45, 45, 50), Color.rgb(25, 25, 30), Color.rgb(195, 45, 45), Color.rgb(245, 205, 40)) // BlackGrowlmon

            // Perfect & Ultimate iconic defaults
            401 -> Palette(Color.rgb(255, 135, 20), Color.rgb(62, 39, 35), Color.rgb(144, 164, 174), Color.rgb(0, 176, 255)) // MetalGreymon
            414 -> Palette(Color.rgb(55, 55, 65), Color.rgb(30, 30, 35), Color.rgb(144, 164, 174), Color.rgb(244, 67, 54)) // MetalTyrannomon
            501 -> Palette(Color.rgb(216, 67, 21), Color.rgb(255, 193, 7), Color.rgb(144, 164, 174), Color.rgb(0, 230, 118)) // WarGreymon
            511 -> Palette(Color.rgb(245, 245, 250), Color.rgb(255, 193, 7), Color.rgb(144, 164, 174), Color.rgb(0, 200, 255)) // Omegamon
            else -> Palette(Color.rgb(255, 168, 38), Color.rgb(216, 112, 10), Color.rgb(255, 228, 140), Color.rgb(0, 200, 68))
        }
    }

    // =========================================================================
    // Authentic 16x16 Bandai Dot-Matrix Pixel Art Patterns
    // '.' = Transparent
    // '#' = Primary Body Color
    // 'S' = Secondary Accent / Shadow
    // 'T' = Tertiary / Belly / Markings
    // 'E' = Eye Color
    // 'W' = Pure White (Claws, Teeth, Sclera)
    // 'B' = Deep Ink Black (Pupil, Mouth Slit, Nostril)
    // 'R' = Crimson / Fire
    // 'G' = Golden Yellow
    // 'C' = Cyan Energy
    // =========================================================================

    private val AGUMON_IDLE_A = arrayOf(
        "......BBBBB.....",
        ".....B#####B....",
        "....BBEB###B....",
        "..BB###EE###B...",
        ".B#####WWW###B..",
        ".BBBB########B..",
        "..B##########B..",
        "...BBTTTT###B...",
        "....BTTTT#B#B...",
        "...B#TTTT#B.WW..",
        "...BBTTTT#BBBW..",
        ".....BTTTT#B..B.",
        "...BBTTB###B..B.",
        "..BWB..BWB.BWB.B",
        "..BBBBBB.BBBBBBB",
        "................"
    )

    private val AGUMON_IDLE_B = arrayOf(
        "......BBBBB.....",
        ".....B#####B....",
        "....BBEB###B....",
        "..BB###EE###B...",
        ".B#####WWW###B..",
        ".BBBB########B..",
        "..B##########B..",
        "...BBTTTT###B...",
        "....BTTTT#B#B...",
        "...B#TTTT#B.WW..",
        "...BBTTTT#BBBW..",
        ".....BTTTT#B..B.",
        "...BBTTB###B..B.",
        "...BWB..BWB.BWB.",
        "...BBBBBB.BBBBBB",
        "................"
    )

    private val AGUMON_SLEEP = arrayOf(
        "......BBBBB.....",
        ".....B#####B....",
        "....BBBB###B....",
        "..BB###BB###B...",
        ".B#####WWW###B..",
        ".BBBB########B..",
        "..B##########B..",
        "...BBTTTT###B...",
        "....BTTTT#B#B...",
        "...B#TTTT#B.WW..",
        "...BBTTTT#BBBW..",
        ".....BTTTT#B..B.",
        "...BBTTB###B..B.",
        "..BWB..BWB.BWB.B",
        "..BBBBBB.BBBBBBB",
        "................"
    )

    private val AGUMON_ATTACK = arrayOf(
        "......BBBBB.....",
        ".....B#####B....",
        "....BBEB###B....",
        "..BB###EE###B...",
        ".B#####RRRRRRRR.",
        ".BBBB##RRRRRRRR.",
        "..B##########B..",
        "...BBTTTT###B...",
        "....BTTTT#B#B...",
        "...B#TTTT#B.WW..",
        "...BBTTTT#BBBW..",
        ".....BTTTT#B..B.",
        "...BBTTB###B..B.",
        "...BWB..BWB.BWB.",
        "...BBBBBB.BBBBBB",
        "................"
    )

    private val BOTAMON_IDLE = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....BB..BB.....",
        "....BBBBBBBB....",
        "....B#EEEE#B....",
        "...BB#EEEE#BB...",
        "...BBBBBBBBBB...",
        "..BBBBBBBBBBBB..",
        "..BBBB....BBBB..",
        "..BBBBBBBBBBBB..",
        "...BBBBBBBBBB...",
        "................"
    )

    private val BOTAMON_SLEEP = arrayOf(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        ".....BB..BB.....",
        "....BBBBBBBB....",
        "....B#BBBB#B....",
        "...BB#BBBB#BB...",
        "...BBBBBBBBBB...",
        "..BBBBBBBBBBBB..",
        "..BBBB....BBBB..",
        "..BBBBBBBBBBBB..",
        "...BBBBBBBBBB...",
        "................"
    )

    private val KOROMON_IDLE = arrayOf(
        "................",
        "....BB....BB....",
        "...B##B..B##B...",
        "...B##B..B##B...",
        "...B##BBBB##B...",
        "..B##########B..",
        ".B############B.",
        ".B#EE######EE#B.",
        ".B#EE######EE#B.",
        ".B############B.",
        ".B###WWWWWW###B.",
        "..B##WWWWWW##B..",
        "...B########B...",
        "....BBBBBBBB....",
        "................",
        "................"
    )

    private val KOROMON_SLEEP = arrayOf(
        "................",
        "....BB....BB....",
        "...B##B..B##B...",
        "...B##B..B##B...",
        "...B##BBBB##B...",
        "..B##########B..",
        ".B############B.",
        ".B#BB######BB#B.",
        ".B#BB######BB#B.",
        ".B############B.",
        "..B##########B..",
        "..B##########B..",
        "...B########B...",
        "....BBBBBBBB....",
        "................",
        "................"
    )

    private val GREYMON_IDLE = arrayOf(
        "......SSSSS..SSS",
        ".SS..S#####SS..S",
        ".S.SSS##TT####S.",
        ".SSS###TT##SSS..",
        ".S#EBE#TT###S...",
        ".S#####TT###S###",
        "..S####TT####S##",
        "...SSSSSTTTT#S##",
        ".....TTTTTT####.",
        "...SS####TT#WW..",
        "..S#S###T###WW..",
        "..S#S###T#####S.",
        "...SS####SSS##S.",
        "..SS#S#####S#S#S",
        ".S#S##SSSSS#SSSS",
        ".SSSSSSS...SSSSS"
    )

    private val DEVIMON_IDLE = arrayOf(
        "S..............S",
        "SS............SS",
        "SSS...####...SSS",
        "SSSS.##EE##.SSSS",
        ".SSS########SSS.",
        "..SS########SS..",
        "...S########S...",
        "....########....",
        "....TT#TT#TT....",
        "...WW######WW...",
        "..WWW######WWW..",
        ".....######.....",
        "....###..###....",
        "....###..###....",
        "....###..###....",
        "...WWWW..WWWW..."
    )

    private val WARGREYMON_IDLE = arrayOf(
        ".....WW..WW.....",
        "....SSSSSSSS....",
        "...SS##EE##SS...",
        "....SSSSSSSS....",
        "..TT.######.TT..",
        ".TTT########TTT.",
        "TTTT########TTTT",
        "WWWW########WWWW",
        "WWWW########WWWW",
        ".WW.########.WW.",
        "....########....",
        "....TT#SS#TT....",
        "....###..###....",
        "....###..###....",
        "...SS##..##SS...",
        "...WWWW..WWWW..."
    )

    private val GABUMON_IDLE = arrayOf(
        "......TT........",
        ".....TTTT.......",
        "....SSSSSS......",
        "...SSSEBESS.....",
        "...SSSSSSSS.....",
        "...SSSSWWSS#####",
        "...SSSSSSSS#####",
        "....############",
        "...TT#TT#WW##...",
        "..TTTTTT#WW##...",
        ".#TTTTTT######..",
        "###TTTTT######..",
        "##..####..####..",
        "....####..####..",
        "...WWWW..WWWW...",
        "................"
    )

    private val BETAMON_IDLE = arrayOf(
        ".......TT.......",
        "......TTTT......",
        ".....TTTTTT.....",
        "....TTTTTTTT....",
        "...###########..",
        "..####EBE######.",
        ".###############",
        "################",
        "################",
        "################",
        "################",
        ".##############.",
        "WW##.######.##WW",
        "WW##.######.##WW",
        "WWWW........WWWW",
        "................"
    )

    private val PATAMON_IDLE = arrayOf(
        "..SS........SS..",
        ".SSSS......SSSS.",
        "SSSSSS....SSSSSS",
        ".SSSS......SSSS.",
        "..SS.######.SS..",
        "...##########...",
        "..####EBE####...",
        "..###########...",
        "..###########...",
        "...TTTTTTTT.....",
        "..TTTTTTTTTT....",
        "..##########....",
        "..##..##..##....",
        "..WW..WW..WW....",
        "................",
        "................"
    )

    private val NUMEMON_IDLE = arrayOf(
        "..EE......EE....",
        "..BE......BE....",
        "..##......##....",
        "..##......##....",
        "...############.",
        "....############",
        "...#############",
        "..##############",
        ".###############",
        "################",
        "################",
        "################",
        "################",
        ".##############.",
        "..############..",
        "................"
    )

    private val SUKAMON_IDLE = arrayOf(
        ".......##.......",
        "......####......",
        "....########....",
        "...##########...",
        "..##WW####WW##..",
        "..#WBB####WBB#..",
        ".###############",
        "################",
        ".####WWWWWW####.",
        "..###WWWWWW###..",
        "################",
        "################",
        ".##############.",
        "..############..",
        "...##########...",
        "................"
    )

    private val OMEGAMON_IDLE = arrayOf(
        ".....WW..WW.....",
        "....WWWWWWWW....",
        "...WW##EE##WW...",
        "....WWWWWWWW....",
        "..SS.######.TT..",
        ".SSSS######TTTT.",
        "SSSSS######TTTTT",
        "WWWWW######TTTTT",
        "WWWWW######CCCC.",
        ".WW..######..C..",
        "....########....",
        "....WW#SS#WW....",
        "....###..###....",
        "....###..###....",
        "...WW##..##WW...",
        "...WWWW..WWWW..."
    )

    private val ARCHETYPE_DINOSAUR = arrayOf(
        "......BBBBB.....",
        ".....B#####B....",
        "....BBEB###B....",
        "..BB###EE###B...",
        ".B#####WWW###B..",
        ".BBBB########B..",
        "..B##########B..",
        "...BBTTTT###B...",
        "....BTTTT#B#B...",
        "...B#TTTT#B.WW..",
        "...BBTTTT#BBBW..",
        ".....BTTTT#B..B.",
        "...BBTTB###B..B.",
        "..BWB..BWB.BWB.B",
        "..BBBBBB.BBBBBBB",
        "................"
    )

    private val ARCHETYPE_BEAST = arrayOf(
        "...SS......SS...",
        "..SSSS....SSSS..",
        "...SSSS..SSSS...",
        "....##########..",
        "...####EBE######",
        "...#############",
        "....############",
        "...TT#TT#WW##...",
        "..TTTTTT#WW##...",
        ".#TTTTTT######..",
        "###TTTTT######..",
        "##..####..####..",
        "##..####..####..",
        "....####..####..",
        "...WWWW..WWWW...",
        "................"
    )

    private val ARCHETYPE_AVIAN = arrayOf(
        "......####......",
        ".....##EBE##....",
        "....########WW..",
        ".....######WW...",
        "SS....####....SS",
        "SSS..######..SSS",
        "SSSS########SSSS",
        "SSS.########.SSS",
        "SS..########..SS",
        "....########....",
        ".....######.....",
        ".....######.....",
        "....###..###....",
        "....WW....WW....",
        "...WWWW..WWWW...",
        "................"
    )

    private val ARCHETYPE_INSECT = arrayOf(
        "...WW......WW...",
        "....WW....WW....",
        ".....######.....",
        "....##EBEB##....",
        "...############.",
        "....##########..",
        "TT...########...TT",
        "TTT.##########.TTT",
        "TT..##########..TT",
        "....##########....",
        "...############...",
        "..WW##.####.##WW..",
        "..WW...####...WW..",
        ".......####.......",
        "......##..##......",
        ".....WW....WW....."
    )

    private val ARCHETYPE_AQUATIC = arrayOf(
        ".......SS.......",
        "......SSSS......",
        ".....SSSSSS.....",
        "....##########..",
        "...####EBE######",
        "..##############",
        ".###############",
        "################",
        "################",
        "################",
        ".##############.",
        "..############..",
        "...##########...",
        "....########....",
        "...WWWW..WWWW...",
        "..WWWW....WWWW.."
    )

    private val ARCHETYPE_DEMON = arrayOf(
        "S..............S",
        "SS............SS",
        "SSS...####...SSS",
        "SSSS.##EE##.SSSS",
        ".SSS########SSS.",
        "..SS########SS..",
        "...S########S...",
        "....########....",
        "....TT#TT#TT....",
        "...WW######WW...",
        "..WWW######WWW..",
        ".....######.....",
        "....###..###....",
        "....###..###....",
        "....###..###....",
        "...WWWW..WWWW..."
    )

    private val ARCHETYPE_HOLY = arrayOf(
        ".....WWWWWW.....",
        "WW....####....WW",
        "WWW..##EE##..WWW",
        "WWWW########WWWW",
        "WWWW########WWWW",
        ".WW.########.WW.",
        "....########....",
        "...TT######TT...",
        "..TTTT####TTTT..",
        ".....######.....",
        ".....######.....",
        "....###..###....",
        "....###..###....",
        "....###..###....",
        "...WWWW..WWWW...",
        "................"
    )

    private val ARCHETYPE_MECH = arrayOf(
        "....WW....WW....",
        "...SSSS..SSSS...",
        "...SSSSSSSSSS...",
        "...SS##EE##SS...",
        "...SSSSSSSSSS...",
        "..TT########TT..",
        ".TTTT######TTTT.",
        "TTTT########TTTT",
        "WWWW########WWWW",
        ".WW.########.WW.",
        "....########....",
        "....TT#SS#TT....",
        "....###..###....",
        "....###..###....",
        "...SS##..##SS...",
        "...WWWW..WWWW..."
    )

    private val ARCHETYPE_PLANT = arrayOf(
        ".....TTTTTT.....",
        "....TTTTTTTT....",
        ".....######.....",
        "....##EBEB##....",
        "...############.",
        "..##############",
        ".###############",
        "################",
        "################",
        ".##############.",
        "..############..",
        "...##########...",
        "....########....",
        ".....######.....",
        "....###..###....",
        "...WWWW..WWWW..."
    )

    private val ARCHETYPE_BABY = arrayOf(
        "................",
        ".....######.....",
        "...##########...",
        "..############..",
        ".##############.",
        ".###EE####EE###.",
        ".###EE####EE###.",
        "################",
        "################",
        ".##############.",
        ".##############.",
        "..############..",
        "...##########...",
        "....########....",
        ".....##..##.....",
        "................"
    )

    private fun getMatrixForSpecies(speciesId: Int, frame: SpriteFrame): Array<String> {
        val isSleep = frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B
        val isWalk = frame == SpriteFrame.WALK_A || frame == SpriteFrame.WALK_B
        val isAttack = frame == SpriteFrame.ATTACK_RELEASE

        val sid = if (speciesId <= 1) 201 else speciesId

        return when (sid) {
            101 -> if (isSleep) BOTAMON_SLEEP else BOTAMON_IDLE
            102 -> if (isSleep) KOROMON_SLEEP else KOROMON_IDLE
            201 -> when {
                isSleep -> AGUMON_SLEEP
                isAttack -> AGUMON_ATTACK
                isWalk || frame == SpriteFrame.IDLE_B -> AGUMON_IDLE_B
                else -> AGUMON_IDLE_A
            }
            202 -> BETAMON_IDLE
            203 -> GABUMON_IDLE
            205 -> PATAMON_IDLE
            301 -> GREYMON_IDLE
            302 -> ARCHETYPE_DINOSAUR // Tyrannomon
            303 -> DEVIMON_IDLE
            305 -> NUMEMON_IDLE
            321 -> SUKAMON_IDLE
            401 -> GREYMON_IDLE // MetalGreymon
            501 -> WARGREYMON_IDLE
            511 -> OMEGAMON_IDLE

            // Baby / Slime
            in 101..139 -> if (isSleep) BOTAMON_SLEEP else ARCHETYPE_BABY

            // Avian / Winged
            in listOf(206, 225, 309, 320, 323, 328, 344, 407, 417, 422, 437, 506, 545) -> ARCHETYPE_AVIAN

            // Aquatic / Serpent
            in listOf(210, 314, 324, 327, 409, 508, 533) -> ARCHETYPE_AQUATIC

            // Insect / Arthropod
            in listOf(208, 211, 213, 310, 322, 333, 408, 507) -> ARCHETYPE_INSECT

            // Beast / Quadruped
            in listOf(204, 216, 217, 306, 311, 318, 340, 403, 404, 423, 430, 503) -> ARCHETYPE_BEAST

            // Demonic / Dark
            in listOf(209, 218, 312, 317, 326, 410, 428, 509, 519, 520, 530, 531, 532) -> ARCHETYPE_DEMON

            // Holy / Angelic
            in listOf(221, 307, 405, 427, 504, 514, 515, 516, 521, 537, 552) -> ARCHETYPE_HOLY

            // Heavy Mech / Artillery
            in listOf(418, 502, 518, 542, 546, 548, 554, 441, 414, 429) -> ARCHETYPE_MECH

            // Plant / Floral
            in listOf(207, 230, 308, 406, 443, 505) -> ARCHETYPE_PLANT

            // Default: Biped Dinosaur
            else -> ARCHETYPE_DINOSAUR
        }
    }

    private fun renderSpriteToCanvas(
        canvas: Canvas,
        paint: Paint,
        speciesId: Int,
        frame: SpriteFrame,
        isAmbient: Boolean
    ) {
        val palette = getPalette(speciesId)
        val matrix = getMatrixForSpecies(speciesId, frame)

        // Frame offsets for dynamic life
        val xOffset = when (frame) {
            SpriteFrame.WALK_A -> 2f
            SpriteFrame.WALK_B -> -2f
            SpriteFrame.ATTACK_CHARGE -> -3f
            SpriteFrame.ATTACK_RELEASE -> 4f
            SpriteFrame.REFUSAL -> -2f
            SpriteFrame.INJURED -> -2f
            else -> 0f
        }

        val yBob = when (frame) {
            SpriteFrame.IDLE_B -> -1f
            SpriteFrame.WALK_A, SpriteFrame.WALK_B -> 1f
            SpriteFrame.SLEEP_A, SpriteFrame.SLEEP_B -> 2f
            SpriteFrame.JOY_EAT -> -2f
            else -> 0f
        }

        val rows = matrix.size
        val cols = matrix[0].length
        val cellSize = 3.5f
        val startX = (64f - cols * cellSize) / 2f + xOffset
        val startY = (64f - rows * cellSize) / 2f + yBob

        paint.style = Paint.Style.FILL

        for (r in 0 until rows) {
            val rowStr = matrix[r]
            val py = startY + r * cellSize
            for (c in 0 until cols) {
                val ch = rowStr[c]
                if (ch == '.') continue
                val px = startX + c * cellSize

                if (isAmbient) {
                    paint.color = Color.WHITE
                    canvas.drawRect(px, py, px + cellSize, py + cellSize, paint)
                } else {
                    paint.color = when (ch) {
                        '#' -> palette.primary
                        'S' -> palette.secondary
                        'T' -> palette.tertiary
                        'E' -> palette.eye
                        'W' -> Color.rgb(250, 250, 250)
                        'B' -> Color.rgb(18, 20, 26)
                        'R' -> Color.rgb(244, 67, 54)
                        'G' -> Color.rgb(255, 215, 0)
                        'C' -> Color.rgb(0, 229, 255)
                        else -> palette.primary
                    }
                    canvas.drawRect(px, py, px + cellSize, py + cellSize, paint)
                }
            }
        }

        // ==========================================
        // Universal Authentic V-Pet Action & Status Overlays
        // ==========================================
        if (frame == SpriteFrame.FEED_START || frame == SpriteFrame.JOY_EAT) {
            // Classic V-Pet Meat-on-Bone pixel sprite
            paint.color = Color.rgb(245, 240, 230)
            canvas.drawRect(48f + xOffset, 25f + yBob, 58f + xOffset, 28f + yBob, paint)
            paint.color = Color.rgb(205, 55, 45)
            canvas.drawRect(50f + xOffset, 23f + yBob, 56f + xOffset, 30f + yBob, paint)
            if (frame == SpriteFrame.JOY_EAT) {
                // Pixel Heart above head
                paint.color = Color.rgb(255, 65, 110)
                canvas.drawRect(29f + xOffset, 5f + yBob, 32f + xOffset, 8f + yBob, paint)
                canvas.drawRect(33f + xOffset, 5f + yBob, 36f + xOffset, 8f + yBob, paint)
                canvas.drawRect(30f + xOffset, 8f + yBob, 35f + xOffset, 11f + yBob, paint)
            }
        }

        if (frame == SpriteFrame.SLEEP_A || frame == SpriteFrame.SLEEP_B) {
            // Classic V-Pet Sleep 'Zzz' pixel glyph in top-right
            paint.color = Color.rgb(130, 215, 255)
            val zShift = if (frame == SpriteFrame.SLEEP_B) -3f else 0f
            canvas.drawRect(46f, 10f + zShift, 54f, 12f + zShift, paint)
            canvas.drawLine(53f, 12f + zShift, 47f, 18f + zShift, paint)
            canvas.drawRect(46f, 17f + zShift, 54f, 19f + zShift, paint)
        }

        if (frame == SpriteFrame.INJURED) {
            // Classic V-Pet Injury/Sickness pixel cross alert in top-left
            paint.color = Color.rgb(255, 60, 60)
            canvas.drawRect(9f, 8f, 13f, 18f, paint)
            canvas.drawRect(6f, 11f, 16f, 15f, paint)
        }

        if (frame == SpriteFrame.REFUSAL) {
            // Classic V-Pet Refusal sweat drop in top-right
            paint.color = Color.rgb(80, 195, 255)
            canvas.drawRect(50f, 10f, 53f, 16f, paint)
            canvas.drawRect(49f, 13f, 54f, 17f, paint)
        }

        if (frame == SpriteFrame.ATTACK_RELEASE) {
            // Classic V-Pet Double Fireball / Energy Blast projectile
            paint.color = Color.rgb(255, 75, 20)
            canvas.drawRect(52f, 24f, 60f, 32f, paint)
            paint.color = Color.rgb(255, 230, 55)
            canvas.drawRect(54f, 26f, 58f, 30f, paint)
        }
    }
}
