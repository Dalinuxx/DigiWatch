package com.digiwear.wear.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.digiwear.core.qrcode.QrMatrixGenerator

/**
 * Jetpack Compose Canvas renderer for 2D QR Code Matrices on Wear OS.
 *
 * Renders dark modules on a pure white background with a quiet zone margin using [drawRect].
 * Ensures maximum optical contrast for camera scanner recognition from watch displays.
 *
 * @param matrix 2D boolean array where true is black module, false is white.
 * @param modifier Compose Modifier.
 * @param quietZoneModules Quiet zone margin in module units (default: 2).
 * @param moduleColor Color for dark modules (default: Color.Black).
 * @param backgroundColor Color for background and quiet zone (default: Color.White).
 */
@Composable
fun QrCodeMatrixView(
    matrix: Array<BooleanArray>,
    modifier: Modifier = Modifier,
    quietZoneModules: Int = 2,
    moduleColor: Color = Color.Black,
    backgroundColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        // 1. Draw solid background covering the entire canvas
        drawRect(
            color = backgroundColor,
            topLeft = Offset.Zero,
            size = size
        )

        val rows = matrix.size
        if (rows == 0) return@Canvas
        val cols = matrix[0].size
        if (cols == 0) return@Canvas

        val effectiveQuietZone = quietZoneModules.coerceAtLeast(0)
        val totalCols = cols + (effectiveQuietZone * 2)
        val totalRows = rows + (effectiveQuietZone * 2)

        val availableWidth = size.width
        val availableHeight = size.height

        // Square module size preserving 1:1 aspect ratio
        val cellSize = minOf(
            availableWidth / totalCols,
            availableHeight / totalRows
        )

        if (cellSize <= 0f) return@Canvas

        // Center matrix inside canvas
        val contentWidth = totalCols * cellSize
        val contentHeight = totalRows * cellSize
        val originX = (availableWidth - contentWidth) / 2f + (effectiveQuietZone * cellSize)
        val originY = (availableHeight - contentHeight) / 2f + (effectiveQuietZone * cellSize)

        // Draw dark modules with boundary snapping to avoid subpixel seams
        for (r in 0 until rows) {
            val row = matrix[r]
            val top = originY + r * cellSize
            val nextTop = originY + (r + 1) * cellSize
            val cellHeight = nextTop - top

            for (c in 0 until minOf(cols, row.size)) {
                if (row[c]) {
                    val left = originX + c * cellSize
                    val nextLeft = originX + (c + 1) * cellSize
                    val cellWidth = nextLeft - left

                    drawRect(
                        color = moduleColor,
                        topLeft = Offset(left, top),
                        size = Size(cellWidth, cellHeight)
                    )
                }
            }
        }
    }
}

/**
 * Convenience overload that accepts string content directly.
 */
@Composable
fun QrCodeMatrixView(
    content: String,
    modifier: Modifier = Modifier,
    size: Int = 29,
    quietZoneModules: Int = 2,
    moduleColor: Color = Color.Black,
    backgroundColor: Color = Color.White
) {
    val matrix = remember(content, size) {
        if (content.isNotBlank()) {
            try {
                QrMatrixGenerator.generateQrMatrix(content, size)
            } catch (_: Exception) {
                emptyArray()
            }
        } else {
            emptyArray()
        }
    }

    QrCodeMatrixView(
        matrix = matrix,
        modifier = modifier,
        quietZoneModules = quietZoneModules,
        moduleColor = moduleColor,
        backgroundColor = backgroundColor
    )
}
