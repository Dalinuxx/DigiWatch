package com.digiwear.wear.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import com.digiwear.core.model.BackgroundTheme

/**
 * Renders procedural, battery-optimized backgrounds tailored for virtual pets.
 * Uses [drawWithCache] and batched GPU point/line primitives for zero per-frame allocations.
 * In Ambient Mode, it renders strictly pure black (#000000) for OLED power saving (OPR <= 15%).
 * In Interactive Mode, it renders vintage LCD dot matrices or cybernetic Digivice data grids
 * with a recessed Bandai V-Pet inner screen bezel.
 */
@Composable
fun BackgroundCanvas(
    theme: BackgroundTheme,
    isAmbient: Boolean,
    modifier: Modifier = Modifier,
    lightsOff: Boolean = false
) {
    Spacer(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                val primaryColor = Color(theme.primaryColorHex)
                val secondaryColor = Color(theme.secondaryColorHex).copy(alpha = 0.35f)
                val gridColor25 = Color(theme.gridColorHex).copy(alpha = 0.25f)
                val gridColor20 = Color(theme.gridColorHex).copy(alpha = 0.20f)
                val gridColor15 = Color(theme.gridColorHex).copy(alpha = 0.15f)
                val gridColor35 = Color(theme.gridColorHex).copy(alpha = 0.35f)
                val bezelHighlight = Color(theme.gridColorHex).copy(alpha = 0.45f)

                // Precompute 1997 LCD dot-matrix coordinates once per size/theme change
                val lcdDots = if (theme == BackgroundTheme.CLASSIC_VPET_LCD) {
                    val pts = ArrayList<Offset>(1600)
                    val dotSpacing = 7f
                    var x = 3.5f
                    while (x < size.width) {
                        var y = 3.5f
                        while (y < size.height) {
                            pts.add(Offset(x, y))
                            y += dotSpacing
                        }
                        x += dotSpacing
                    }
                    pts
                } else {
                    emptyList()
                }

                // Precompute subtle brick-pattern inner bezel tick marks for Classic V-Pet
                val brickTicks = if (theme == BackgroundTheme.CLASSIC_VPET_LCD) {
                    val ticks = ArrayList<Offset>(64)
                    val step = 18f
                    var x = 12f
                    while (x < size.width - 12f) {
                        ticks.add(Offset(x, 0f))
                        ticks.add(Offset(x, 5f))
                        ticks.add(Offset(x, size.height - 5f))
                        ticks.add(Offset(x, size.height))
                        x += step
                    }
                    ticks
                } else {
                    emptyList()
                }

                onDrawBehind {
                    if (isAmbient || lightsOff) {
                        // OLED Power Saving: Pure pitch black
                        drawRect(color = Color.Black)
                        return@onDrawBehind
                    }

                    // Draw solid background base
                    drawRect(color = primaryColor)

                    when (theme) {
                        BackgroundTheme.CLASSIC_VPET_LCD -> {
                            // Single batched GPU call for authentic 1997 LCD dot-matrix pixel grid
                            drawPoints(
                                points = lcdDots,
                                pointMode = PointMode.Points,
                                color = gridColor25,
                                strokeWidth = 1.5f
                            )
                            // Recessed Bandai V-Pet LCD screen cage border
                            drawRoundRect(
                                color = gridColor35,
                                topLeft = Offset(3f, 3f),
                                size = Size(size.width - 6f, size.height - 6f),
                                cornerRadius = CornerRadius(12f, 12f),
                                style = Stroke(width = 3f)
                            )
                            if (brickTicks.isNotEmpty()) {
                                drawPoints(
                                    points = brickTicks,
                                    pointMode = PointMode.Lines,
                                    color = bezelHighlight,
                                    strokeWidth = 1.5f
                                )
                            }
                        }
                        BackgroundTheme.CYBER_FOREST, BackgroundTheme.DATA_WASTELAND -> {
                            val lineSpacing = 12f
                            var y = 0f
                            while (y < size.height) {
                                drawLine(
                                    color = secondaryColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f
                                )
                                y += lineSpacing
                            }
                            // Subtle Digivice radar horizon line
                            drawLine(
                                color = bezelHighlight,
                                start = Offset(0f, size.height * 0.72f),
                                end = Offset(size.width, size.height * 0.72f),
                                strokeWidth = 1.5f
                            )
                        }
                        BackgroundTheme.DIGITAL_OCEAN, BackgroundTheme.NEON_CORE_CITY -> {
                            val gridSize = 22f
                            var x = 0f
                            while (x < size.width) {
                                drawLine(
                                    color = gridColor20,
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 1f
                                )
                                x += gridSize
                            }
                            var y = 0f
                            while (y < size.height) {
                                drawLine(
                                    color = gridColor20,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f
                                )
                                y += gridSize
                            }
                            drawRoundRect(
                                color = bezelHighlight,
                                topLeft = Offset(2f, 2f),
                                size = Size(size.width - 4f, size.height - 4f),
                                cornerRadius = CornerRadius(16f, 16f),
                                style = Stroke(width = 1.5f)
                            )
                        }
                        BackgroundTheme.GLITCH_VOID -> {
                            val step = 16f
                            var y = 0f
                            var flip = false
                            while (y < size.height) {
                                drawLine(
                                    color = if (flip) gridColor15 else gridColor35,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 2f
                                )
                                flip = !flip
                                y += step
                            }
                        }
                        BackgroundTheme.DARK_NETWORK, BackgroundTheme.OLED_MINIMAL -> {
                            drawRect(color = primaryColor)
                        }
                    }
                }
            }
    )
}
