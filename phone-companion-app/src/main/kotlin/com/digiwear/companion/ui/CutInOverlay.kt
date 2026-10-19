package com.digiwear.companion.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwear.companion.render.SpriteCanvasRenderer
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.SpriteFrame
import com.digiwear.core.render.CutInPortraitData
import com.digiwear.core.render.CutInPortraitEngine
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Authentic Color V-Pet (Digital Monster Color / Pendulum Color) battle cut-in view for Android Phone Companion.
 * Renders high-speed anime action lines, glowing attribute energy aura,
 * a magnified close-up headshot portrait with eye glints, and critical hit battle banners.
 */
@Composable
fun PhoneCutInOverlay(
    speciesId: Int,
    modifier: Modifier = Modifier,
    title: String = "CRITICAL HIT",
    autoDismissMs: Long? = 850L,
    onDismiss: (() -> Unit)? = null
) {
    val cutIn = remember(speciesId) { CutInPortraitEngine.getCutInPortrait(speciesId) }
    val attrColor = Color(cutIn.attributeColorArgb)
    val secColor = Color(cutIn.secondaryAuraArgb)

    val infiniteTransition = rememberInfiniteTransition(label = "PhoneCutInAnim")
    val speedlinePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Speedlines"
    )

    val eyeGlintPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(250, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Glint"
    )

    LaunchedEffect(speciesId, autoDismissMs) {
        if (autoDismissMs != null && autoDismissMs > 0L) {
            delay(autoDismissMs)
            onDismiss?.invoke()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF030710).copy(alpha = 0.95f))
            .clickable { onDismiss?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        // 1. Dynamic Canvas Layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f - 20.dp.toPx())
            drawActionSpeedlines(center, speedlinePhase, attrColor, secColor)
            drawAttributeAura(center, attrColor, secColor)
            drawMagnifiedHeadshot(cutIn, center, 200.dp.toPx())
            drawEyeGlints(cutIn, center, 200.dp.toPx(), eyeGlintPulse, attrColor)
            drawCyberReticle(center, 220.dp.toPx(), attrColor)
        }

        // 2. HUD Overlay Layer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Critical Hit Banner
            Box(
                modifier = Modifier
                    .background(Color(0xFF140808), RoundedCornerShape(8.dp))
                    .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "⚡ $title ⚡",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD54F),
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Digimon Details Banner
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color(0xEE0A121E), RoundedCornerShape(12.dp))
                    .border(1.dp, attrColor.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = cutIn.name.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[${cutIn.attribute.displayName.uppercase()}]",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = attrColor,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  ${cutIn.stage.displayName.uppercase()}",
                        fontSize = 11.sp,
                        color = Color(0xFFB0BEC5),
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"${cutIn.battleCry}\"",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = secColor,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun DrawScope.drawActionSpeedlines(
    center: Offset,
    phase: Float,
    primaryColor: Color,
    secondaryColor: Color
) {
    val rayCount = 40
    val maxRadius = size.maxDimension
    val innerRadius = 70.dp.toPx()

    for (i in 0 until rayCount) {
        val angleDeg = (i * (360f / rayCount) + phase) % 360f
        val angleRad = Math.toRadians(angleDeg.toDouble())
        val isAlt = i % 2 == 0

        val startX = center.x + innerRadius * cos(angleRad).toFloat()
        val startY = center.y + innerRadius * sin(angleRad).toFloat()
        val endX = center.x + maxRadius * cos(angleRad).toFloat()
        val endY = center.y + maxRadius * sin(angleRad).toFloat()

        val color = if (isAlt) primaryColor.copy(alpha = 0.25f) else secondaryColor.copy(alpha = 0.14f)
        val strokeWidth = if (isAlt) 3.5f else 1.8f

        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawAttributeAura(
    center: Offset,
    primaryColor: Color,
    secondaryColor: Color
) {
    drawCircle(
        color = primaryColor.copy(alpha = 0.18f),
        radius = 110.dp.toPx(),
        center = center
    )
    drawCircle(
        color = secondaryColor.copy(alpha = 0.10f),
        radius = 135.dp.toPx(),
        center = center
    )
}

private fun DrawScope.drawMagnifiedHeadshot(
    cutIn: CutInPortraitData,
    center: Offset,
    renderBoxSize: Float
) {
    val matrix = cutIn.headCropMatrix
    val rows = matrix.size
    val cols = matrix[0].length
    val cellSize = renderBoxSize / cols.toFloat()

    val startX = center.x - (cols * cellSize) / 2f
    val startY = center.y - (rows * cellSize) / 2f
    val palette = cutIn.palette

    for (r in 0 until rows) {
        val rowStr = matrix[r]
        val py = startY + r * cellSize
        for (c in 0 until cols) {
            val ch = rowStr[c]
            if (ch == '.') continue
            val px = startX + c * cellSize

            val pixelColor = when (ch) {
                '#' -> Color(palette.primary)
                'S' -> Color(palette.secondary)
                'T' -> Color(palette.tertiary)
                'E' -> Color(palette.eye)
                'K' -> Color(0xFF12141A)
                'W' -> Color(0xFFFAFAFA)
                'B' -> Color(0xFF10121A)
                'R' -> Color(0xFFF44336)
                'G' -> Color(0xFF43B955)
                'C' -> Color(0xFF00E5FF)
                else -> Color(palette.primary)
            }

            drawRect(
                color = pixelColor,
                topLeft = Offset(px, py),
                size = Size(cellSize, cellSize)
            )
        }
    }
}

private fun DrawScope.drawEyeGlints(
    cutIn: CutInPortraitData,
    center: Offset,
    renderBoxSize: Float,
    pulse: Float,
    glintColor: Color
) {
    val cols = cutIn.headCropMatrix[0].length
    val cellSize = renderBoxSize / cols.toFloat()
    val startX = center.x - (cols * cellSize) / 2f
    val startY = center.y - (cutIn.headCropMatrix.size * cellSize) / 2f

    for (eye in cutIn.eyePoints) {
        val ex = startX + eye.first * cellSize + (cellSize / 2f)
        val ey = startY + eye.second * cellSize + (cellSize / 2f)
        val flareLen = 18.dp.toPx() * pulse

        drawCircle(
            color = Color.White,
            radius = 4.dp.toPx() * pulse,
            center = Offset(ex, ey)
        )
        drawCircle(
            color = glintColor.copy(alpha = 0.65f),
            radius = 8.dp.toPx() * pulse,
            center = Offset(ex, ey)
        )

        drawLine(
            color = Color.White.copy(alpha = 0.95f),
            start = Offset(ex - flareLen, ey),
            end = Offset(ex + flareLen, ey),
            strokeWidth = 2.5f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.95f),
            start = Offset(ex, ey - flareLen),
            end = Offset(ex, ey + flareLen),
            strokeWidth = 2.5f
        )
    }
}

private fun DrawScope.drawCyberReticle(
    center: Offset,
    boxSize: Float,
    reticleColor: Color
) {
    val half = boxSize / 2f
    val cornerLen = 22.dp.toPx()
    val stroke = 3.dp.toPx()

    val left = center.x - half
    val right = center.x + half
    val top = center.y - half
    val bottom = center.y + half

    val path = Path().apply {
        // Top-Left [
        moveTo(left, top + cornerLen)
        lineTo(left, top)
        lineTo(left + cornerLen, top)

        // Top-Right ]
        moveTo(right - cornerLen, top)
        lineTo(right, top)
        lineTo(right, top + cornerLen)

        // Bottom-Left [
        moveTo(left, bottom - cornerLen)
        lineTo(left, bottom)
        lineTo(left + cornerLen, bottom)

        // Bottom-Right ]
        moveTo(right - cornerLen, bottom)
        lineTo(right, bottom)
        lineTo(right, bottom - cornerLen)
    }

    drawPath(path = path, color = reticleColor.copy(alpha = 0.85f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
}

/**
 * Authentic battle encounter flash sequence for Android Companion App.
 */
@Composable
fun PhoneEncounterFlashOverlay(
    enemySpeciesId: Int,
    isBoss: Boolean = false,
    bossTitle: String? = null,
    onFinished: () -> Unit
) {
    var flashStep by remember { mutableIntStateOf(0) }
    val enemySpecies = remember(enemySpeciesId) { PetSpecies.getById(enemySpeciesId) }

    LaunchedEffect(enemySpeciesId) {
        flashStep = 0
        delay(60L)
        flashStep = 1
        delay(60L)
        flashStep = 2
        delay(70L)
        flashStep = 3
        delay(900L)
        onFinished()
    }

    when (flashStep) {
        0, 2 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
        1 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isBoss) Color(0xFF990000) else Color(0xFF002244))
            )
        }
        3 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF080E18))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(if (isBoss) Color(0xFF4A0E17) else Color(0xFF0D253A), RoundedCornerShape(8.dp))
                            .border(1.5.dp, if (isBoss) Color(0xFFFF5252) else Color(0xFF40C4FF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isBoss) "🚨 APEX BOSS DETECTED! 🚨" else "⚔ WILD ENCOUNTER DETECTED! ⚔",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isBoss) Color(0xFFFF8A80) else Color(0xFF80D8FF),
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SpriteCanvasRenderer(
                        speciesId = enemySpeciesId,
                        frame = SpriteFrame.ATTACK_CHARGE,
                        size = 96.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = (bossTitle ?: enemySpecies.name).uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${enemySpecies.stage.displayName} • ${enemySpecies.attribute.displayName} • HP ${enemySpecies.baseHp} • POWER ${enemySpecies.basePower}",
                        fontSize = 11.sp,
                        color = Color(0xFFFFD166),
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "SCANNING DIGI-CORE... COMBAT ENGAGED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
