package com.digiwatch.companion.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.digiwatch.core.model.SpriteFrame
import com.digiwatch.core.render.SpriteMatrixRegistry

/**
 * Authentic 16x16 dot-matrix pixel art renderer for the Phone Companion App.
 * Reads canonical 1997 Bandai V-Pet matrices from SpriteMatrixRegistry (shared-vpet-core).
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

    private fun renderSpriteToCanvas(
        canvas: Canvas,
        paint: Paint,
        speciesId: Int,
        frame: SpriteFrame,
        isAmbient: Boolean
    ) {
        val palette = SpriteMatrixRegistry.getPalette(speciesId)
        val matrix = SpriteMatrixRegistry.getMatrix(speciesId, frame)

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
    }
}
