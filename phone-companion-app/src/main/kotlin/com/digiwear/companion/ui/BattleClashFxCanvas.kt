package com.digiwear.companion.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwear.core.model.ElementalAttribute
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-definition 16-bit animated Combat Clash FX Canvas for Phone Companion App.
 * Renders attribute-themed energy projectiles, laser beams, impact explosions,
 * and floating damage popups between two battling Digimon in the D-Com Colosseum.
 */
@Composable
fun BattleClashFxCanvas(
    attackerIndex: Int, // 0 = Player (left -> right), 1 = Opponent (right -> left)
    attribute: ElementalAttribute,
    attackType: String, // SINGLE, DOUBLE, MEGA_HIT
    isCritical: Boolean,
    moveName: String,
    damage: Int,
    progress: Float, // 0f to 1f over the turn
    modifier: Modifier = Modifier
) {
    val (primaryColor, secondaryColor) = remember(attribute, isCritical) {
        when {
            isCritical || attackType == "MEGA_HIT" -> Pair(Color(0xFFFFD54F), Color(0xFFFFFFFF))
            attribute == ElementalAttribute.VACCINE -> Pair(Color(0xFFFF7043), Color(0xFFFFEB3B))
            attribute == ElementalAttribute.DATA -> Pair(Color(0xFF00E676), Color(0xFF80D8FF))
            attribute == ElementalAttribute.VIRUS -> Pair(Color(0xFFFF1744), Color(0xFFD500F9))
            else -> Pair(Color(0xFF00E5FF), Color(0xFFB388FF))
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Projectile & Impact Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Combatant screen anchor points
            val leftX = w * 0.28f
            val rightX = w * 0.72f
            val centerY = h * 0.45f

            val startX = if (attackerIndex == 0) leftX else rightX
            val endX = if (attackerIndex == 0) rightX else leftX

            // Flight phase: 0.0f to 0.65f
            if (progress <= 0.65f) {
                val flightPct = (progress / 0.65f).coerceIn(0f, 1f)
                val currentX = startX + (endX - startX) * flightPct
                val waveY = centerY + sin(flightPct * 12.56f) * (if (isCritical) 4f else 8f)

                val projRadius = when {
                    isCritical || attackType == "MEGA_HIT" -> 12f
                    attackType == "DOUBLE" -> 9f
                    else -> 7f
                }

                // Trailing speed particles
                for (i in 1..4) {
                    val trailOffset = (i * 12f) * (if (attackerIndex == 0) -1 else 1)
                    val trailAlpha = (1f - i * 0.22f).coerceAtLeast(0.1f)
                    drawCircle(
                        color = secondaryColor.copy(alpha = trailAlpha * 0.75f),
                        radius = (projRadius * (1f - i * 0.18f)).coerceAtLeast(2f),
                        center = Offset(currentX + trailOffset, waveY)
                    )
                }

                // Main energy projectile core
                drawCircle(
                    color = primaryColor,
                    radius = projRadius,
                    center = Offset(currentX, waveY)
                )
                drawCircle(
                    color = Color.White,
                    radius = projRadius * 0.5f,
                    center = Offset(currentX, waveY)
                )

                // High-tier attack energy rings
                if (attackType == "DOUBLE" || isCritical || attackType == "MEGA_HIT") {
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.85f),
                        radius = projRadius * 1.6f,
                        center = Offset(currentX, waveY),
                        style = Stroke(width = 2f)
                    )
                }
            } else {
                // Impact & Explosion phase: 0.65f to 1.0f
                val explodePct = ((progress - 0.65f) / 0.35f).coerceIn(0f, 1f)
                val maxRadius = if (isCritical) 38f else 26f
                val curRadius = maxRadius * explodePct
                val alpha = (1f - explodePct).coerceIn(0f, 1f)

                // Shockwave blast ring
                drawCircle(
                    color = primaryColor.copy(alpha = alpha * 0.85f),
                    radius = curRadius,
                    center = Offset(endX, centerY),
                    style = Stroke(width = 3.5f * (1f - explodePct).coerceAtLeast(0.5f))
                )

                // 8-Point Starburst Sparks
                val sparkDistance = curRadius * 1.35f
                for (angleDeg in 0 until 360 step 45) {
                    val rad = Math.toRadians(angleDeg.toDouble())
                    val sx = endX + (cos(rad) * sparkDistance).toFloat()
                    val sy = centerY + (sin(rad) * sparkDistance).toFloat()
                    val sparkLen = 6f * (1f - explodePct)
                    drawLine(
                        color = secondaryColor.copy(alpha = alpha),
                        start = Offset(endX + (cos(rad) * (sparkDistance - sparkLen)).toFloat(), centerY + (sin(rad) * (sparkDistance - sparkLen)).toFloat()),
                        end = Offset(sx, sy),
                        strokeWidth = 2.5f
                    )
                }
            }
        }

        // Overlay: Attack Name Banner & Floating Damage Number
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 22.dp)
        ) {
            if (moveName.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .background(Color(0xD90A121E), RoundedCornerShape(6.dp))
                        .border(1.dp, primaryColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = moveName.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = primaryColor,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            if (progress >= 0.60f && damage > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                val floatOffset = (-(progress - 0.60f) * 50f).dp
                Box(
                    modifier = Modifier
                        .offset(
                            x = if (attackerIndex == 0) 55.dp else (-55).dp,
                            y = floatOffset
                        )
                        .background(Color(0xE6260C10), RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "-$damage HP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF5252),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
