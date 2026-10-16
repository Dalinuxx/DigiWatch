package com.digiwatch.companion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwatch.core.model.ElementalAttribute
import com.digiwatch.core.model.PetStage

/**
 * Shared Bandai V-Pet (1997 Brick-Wall Casing / Digimon Color) & Cyber Digivice UI components
 * for the Phone Companion App.
 */
object DigiWatchPalette {
    val CyberBg = Color(0xFF0B0F17)
    val CardSurface = Color(0xFF171E2B)
    val CardInner = Color(0xFF101620)
    val CardBorder = Color(0xFF28354A)
    val LcdSageBg = Color(0xFF1D2D24)
    val LcdPhosphor = Color(0xFF80CBC4)
    val DigiviceCyan = Color(0xFF4DD0E1)
    val CrestGold = Color(0xFFFFCA28)
    val BrickTerracotta = Color(0xFF4A2C2A)
    val BrickBorder = Color(0xFF795548)
    val ShellSteel = Color(0xFF263238)
}

private val TOP_VPET_ICONS = listOf(("⚖️" to "STAT"), ("🍖" to "FEED"), ("🥊" to "TRN"), ("⚔" to "BTL"))
private val BOTTOM_VPET_ICONS = listOf(("💩" to "FLSH"), ("💡" to "LGT"), ("🩹" to "MED"), ("🚨" to "CALL"))

/**
 * Authentic 1997 Bandai V-Pet Brick-Wall Casing & Recessed LCD Viewport.
 * Includes top & bottom 4-icon LCD rails, dot-matrix screen grid, corner rivets,
 * and optional interactive [A] [B] [C] shell buttons.
 */
@Composable
fun VPetBrickShellFrame(
    modifier: Modifier = Modifier,
    screenBgColor: Color = DigiWatchPalette.LcdSageBg,
    selectedIconIndex: Int = 0,
    statusLabel: String = "DM-COLOR // VER. 1-5",
    onButtonA: (() -> Unit)? = null,
    onButtonB: (() -> Unit)? = null,
    onButtonC: (() -> Unit)? = null,
    screenHeight: Dp = 152.dp,
    content: @Composable BoxScope.() -> Unit
) {
    // Outer Brick-Textured Bandai V-Pet Shell
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF37474F),
                        Color(0xFF263238),
                        Color(0xFF1C262B)
                    )
                )
            )
            .border(2.dp, Color(0xFF607D8B), RoundedCornerShape(20.dp))
            .drawWithCache {
                val brickColor = Color(0x22000000)
                val highlightColor = Color(0x14FFFFFF)
                onDrawBehind {
                    // Subtle brick-wall mortar lines across the shell casing
                    val rowH = 18.dp.toPx()
                    val colW = 36.dp.toPx()
                    var row = 0
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = brickColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.5f
                        )
                        drawLine(
                            color = highlightColor,
                            start = Offset(0f, y + 1.5f),
                            end = Offset(size.width, y + 1.5f),
                            strokeWidth = 1f
                        )
                        val xOffset = if (row % 2 == 0) 0f else colW / 2f
                        var x = xOffset
                        while (x < size.width) {
                            drawLine(
                                color = brickColor,
                                start = Offset(x, y),
                                end = Offset(x, (y + rowH).coerceAtMost(size.height)),
                                strokeWidth = 1.5f
                            )
                            x += colW
                        }
                        y += rowH
                        row++
                    }
                }
            }
            .padding(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Top Casing Nameplate with Corner Rivets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RivetDot()
                Text(
                    text = "◈ DIGITAL MONSTER • $statusLabel ◈",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = DigiWatchPalette.CrestGold,
                    fontFamily = FontFamily.Monospace
                )
                RivetDot()
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Inner Recessed LCD Bezel + Optional [A][B][C] Physical Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Recessed LCD Cage
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D1412))
                        .border(2.dp, Color(0xFF455A64), RoundedCornerShape(12.dp))
                        .padding(6.dp)
                ) {
                    // Top 4 V-Pet LCD Care Icons Rail
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0A100E), RoundedCornerShape(6.dp))
                            .padding(vertical = 3.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TOP_VPET_ICONS.forEachIndexed { idx, (emoji, lbl) ->
                            val isSel = selectedIconIndex == idx
                            LcdIconCell(emoji = emoji, label = lbl, isSelected = isSel)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Dot-Matrix LCD Center Viewport
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight)
                            .clip(RoundedCornerShape(8.dp))
                            .background(screenBgColor)
                            .border(1.dp, Color(0x6680CBC4), RoundedCornerShape(8.dp))
                            .drawWithCache {
                                val gridColor = Color(0x16000000)
                                val frameColor = Color(0x3380CBC4)
                                onDrawBehind {
                                    val step = 6.dp.toPx()
                                    var x = 0f
                                    while (x < size.width) {
                                        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), 1f)
                                        x += step
                                    }
                                    var y = 0f
                                    while (y < size.height) {
                                        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1f)
                                        y += step
                                    }
                                    val inset = 5.dp.toPx()
                                    drawRect(
                                        color = frameColor,
                                        topLeft = Offset(inset, inset),
                                        size = Size(size.width - inset * 2, size.height - inset * 2),
                                        style = Stroke(width = 1f)
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center,
                        content = content
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Bottom 4 V-Pet LCD Care Icons Rail
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0A100E), RoundedCornerShape(6.dp))
                            .padding(vertical = 3.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BOTTOM_VPET_ICONS.forEachIndexed { idx, (emoji, lbl) ->
                            val isSel = selectedIconIndex == (idx + 4)
                            LcdIconCell(emoji = emoji, label = lbl, isSelected = isSel)
                        }
                    }
                }

                // Right-Side Tactile [A] [B] [C] Bandai Buttons (when callbacks provided)
                if (onButtonA != null && onButtonB != null && onButtonC != null) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShellRoundButton(label = "A", sub = "SEL", color = Color(0xFFFFCA28), onClick = onButtonA)
                        ShellRoundButton(label = "B", sub = "OK", color = Color(0xFF26A69A), onClick = onButtonB)
                        ShellRoundButton(label = "C", sub = "ESC", color = Color(0xFFEF5350), onClick = onButtonC)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Casing Serial Engraving
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RivetDot()
                Text(
                    text = "D-COM / 0NL1NE LINK READY • 64x64 PX",
                    fontSize = 8.sp,
                    color = Color(0xFF90A4AE),
                    fontFamily = FontFamily.Monospace
                )
                RivetDot()
            }
        }
    }
}

@Composable
private fun RivetDot() {
    Box(
        modifier = Modifier
            .size(7.dp)
            .background(Color(0xFF151C20), CircleShape)
            .border(1.dp, Color(0xFF546E7A), CircleShape)
    )
}

@Composable
private fun LcdIconCell(emoji: String, label: String, isSelected: Boolean) {
    Row(
        modifier = Modifier
            .background(
                if (isSelected) Color(0x33FFD54F) else Color.Transparent,
                RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFFFFD54F) else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 5.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFFFFD54F) else Color(0xFF607D8B),
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun ShellRoundButton(
    label: String,
    sub: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF151D24))
                .border(2.dp, color, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = color,
                fontFamily = FontFamily.Monospace
            )
        }
        Text(
            text = sub,
            fontSize = 7.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF90A4AE),
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Cyber-Digivice Section Header with glowing accent bar and optional right-side badge.
 */
@Composable
fun DigiSectionHeader(
    tag: String,
    title: String,
    subtitle: String? = null,
    accentColor: Color = DigiWatchPalette.DigiviceCyan,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(34.dp)
                    .background(accentColor, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "▸ $tag",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color(0xFF90A4AE)
                    )
                }
            }
        }
        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingContent()
        }
    }
}

/**
 * Digimon Stage Pill Badge.
 */
@Composable
fun StageBadge(stageName: String) {
    Surface(
        color = Color(0xFF102428),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color(0xFF26A69A))
    ) {
        Text(
            text = stageName.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF80CBC4),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}

/**
 * Digimon Elemental Attribute Pill Badge (Vaccine / Data / Virus / Free).
 */
@Composable
fun AttributeBadge(attributeName: String) {
    val (bg, border, textCol) = when {
        attributeName.contains("Vaccine", ignoreCase = true) ->
            Triple(Color(0xFF0D2538), Color(0xFF29B6F6), Color(0xFF81D4FA))
        attributeName.contains("Data", ignoreCase = true) ->
            Triple(Color(0xFF2E220D), Color(0xFFFFB300), Color(0xFFFFD54F))
        attributeName.contains("Virus", ignoreCase = true) ->
            Triple(Color(0xFF2A1228), Color(0xFFEC407A), Color(0xFFF48FB1))
        else ->
            Triple(Color(0xFF14281D), Color(0xFF66BB6A), Color(0xFFA5D6A7))
    }
    Surface(
        color = bg,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, border)
    ) {
        Text(
            text = attributeName.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = textCol,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
        )
    }
}

/**
 * 4-Segment LCD Block Gauge Row for Hunger, Strength, and Effort.
 */
@Composable
fun SegmentedGaugeRow(
    label: String,
    filled: Int,
    max: Int = 4,
    activeColor: Color,
    symbolFilled: String = "♥",
    symbolEmpty: String = "♡"
) {
    val safeFilled = filled.coerceIn(0, max)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFFB0BEC5),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(115.dp)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 0 until max) {
                val isOn = i < safeFilled
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .background(
                            if (isOn) activeColor else Color(0xFF111722),
                            RoundedCornerShape(3.dp)
                        )
                        .border(
                            1.dp,
                            if (isOn) activeColor else Color(0xFF263238),
                            RoundedCornerShape(3.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = symbolFilled.repeat(safeFilled) + symbolEmpty.repeat(max - safeFilled),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = activeColor,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End,
            modifier = Modifier.width(42.dp)
        )
    }
}
