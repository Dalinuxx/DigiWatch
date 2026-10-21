package com.digiwear.wear.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Canonical Bandai V-Pet & Digivice Vector Art Icon Registry for Wear OS.
 * Replaces system font emojis with crisp, resolution-independent vector paths
 * engineered for small smartwatch displays and high-DPI OLED panels.
 */
enum class DigiIconType {
    STATUS, STATUS_SCALE,
    FEED, FEED_MEAT,
    VITAMIN, PROTEIN_TABLET,
    TRAIN, TRAIN_GLOVE,
    BATTLE, BATTLE_SWORDS,
    FLUSH, CLEAN_FLUSH,
    LIGHT_ON, SLEEP_LIGHT,
    LIGHT_OFF,
    MEDS_PILL,
    ATTENTION, CALL_ALERT,
    WASTE,
    HEART_FULL,
    HEART_EMPTY,
    STAR_FULL,
    STAR_EMPTY,
    TOMBSTONE, MEMORIAL_GRAVE,
    EGG,
    QUEST,
    SETTINGS, SETTINGS_GEAR,
    BOLT, LIGHTNING_BOLT, DNA_HELIX,
    TROPHY, TROPHY_CROWN,
    SKULL, SKULL_INJURY,
    NFC, NFC_WAVE,
    SCAN, RADAR_SCAN, SEARCH_LENS,
    GLOBE, GLOBE_NET,
    COLOSSEUM,
    CAMERA,
    QR, COMM_LINK, QR_MATRIX,
    SOUND_ON,
    SOUND_OFF,
    SHIELD, VAULT_SAFE,
    SWEAT,
    STEP,
    COPY,
    KEYPAD,
    BACKSPACE,
    CHECK,
    CLOSE,
    REWARD,
    SPARKLE,
    WATCH, WATCH_DEVICE
}

@Composable
fun DigiVectorIcon(
    type: DigiIconType,
    modifier: Modifier = Modifier,
    size: Dp = 14.dp,
    tint: Color = Color.Unspecified
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val s = minOf(w, h)
        val hasCustomTint = tint != Color.Unspecified

        when (type) {
            DigiIconType.STATUS, DigiIconType.STATUS_SCALE -> drawScaleIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF4DD0E1))
            DigiIconType.FEED, DigiIconType.FEED_MEAT -> drawMeatIcon(w, h, s, hasCustomTint, tint)
            DigiIconType.VITAMIN, DigiIconType.PROTEIN_TABLET -> drawVitaminIcon(w, h, s, hasCustomTint, tint)
            DigiIconType.TRAIN, DigiIconType.TRAIN_GLOVE -> drawGloveIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFF7043))
            DigiIconType.BATTLE, DigiIconType.BATTLE_SWORDS -> drawCrossedSwordsIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFD54F))
            DigiIconType.FLUSH, DigiIconType.CLEAN_FLUSH -> drawFlushWaveIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF00E5FF))
            DigiIconType.LIGHT_ON, DigiIconType.SLEEP_LIGHT -> drawLightOnIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFEE58))
            DigiIconType.LIGHT_OFF -> drawLightOffMoonIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF90CAF9))
            DigiIconType.MEDS_PILL -> drawMedicalPillIcon(w, h, s, hasCustomTint, tint)
            DigiIconType.ATTENTION, DigiIconType.CALL_ALERT -> drawAttentionBeaconIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFF5252))
            DigiIconType.WASTE -> drawCoiledWasteIcon(w, h, s, hasCustomTint, tint)
            DigiIconType.HEART_FULL -> drawHeartIcon(w, h, s, filled = true, if (hasCustomTint) tint else Color(0xFFFF5252))
            DigiIconType.HEART_EMPTY -> drawHeartIcon(w, h, s, filled = false, if (hasCustomTint) tint else Color(0xFF78909C))
            DigiIconType.STAR_FULL -> drawStarIcon(w, h, s, filled = true, if (hasCustomTint) tint else Color(0xFFFFD54F))
            DigiIconType.STAR_EMPTY -> drawStarIcon(w, h, s, filled = false, if (hasCustomTint) tint else Color(0xFF78909C))
            DigiIconType.TOMBSTONE, DigiIconType.MEMORIAL_GRAVE -> drawTombstoneIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFB0BEC5))
            DigiIconType.EGG -> drawDigiEggIcon(w, h, s, hasCustomTint, tint)
            DigiIconType.QUEST -> drawRadarCompassIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF69F0AE))
            DigiIconType.SETTINGS, DigiIconType.SETTINGS_GEAR -> drawGearIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFB0BEC5))
            DigiIconType.BOLT, DigiIconType.LIGHTNING_BOLT, DigiIconType.DNA_HELIX -> drawBoltIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFD54F))
            DigiIconType.TROPHY, DigiIconType.TROPHY_CROWN -> drawTrophyIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFD54F))
            DigiIconType.SKULL, DigiIconType.SKULL_INJURY -> drawBossSkullIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFF5252))
            DigiIconType.NFC, DigiIconType.NFC_WAVE -> drawNfcWaveIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF00E5FF))
            DigiIconType.SCAN, DigiIconType.RADAR_SCAN, DigiIconType.SEARCH_LENS -> drawScannerReticleIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF69F0AE))
            DigiIconType.GLOBE, DigiIconType.GLOBE_NET -> drawNetworkGlobeIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFCE93D8))
            DigiIconType.COLOSSEUM -> drawColosseumIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFB74D))
            DigiIconType.CAMERA -> drawCameraIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF00E5FF))
            DigiIconType.QR, DigiIconType.COMM_LINK, DigiIconType.QR_MATRIX -> drawQrIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF00E5FF))
            DigiIconType.SOUND_ON -> drawSoundIcon(w, h, s, muted = false, if (hasCustomTint) tint else Color(0xFF69F0AE))
            DigiIconType.SOUND_OFF -> drawSoundIcon(w, h, s, muted = true, if (hasCustomTint) tint else Color(0xFF90A4AE))
            DigiIconType.SHIELD, DigiIconType.VAULT_SAFE -> drawShieldIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF00E5FF))
            DigiIconType.SWEAT -> drawSweatDropIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF4FC3F7))
            DigiIconType.STEP -> drawStepBootIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF69F0AE))
            DigiIconType.COPY -> drawCopyIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF80DEEA))
            DigiIconType.KEYPAD -> drawKeypadIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFB0BEC5))
            DigiIconType.BACKSPACE -> drawBackspaceIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFF8A80))
            DigiIconType.CHECK -> drawCheckIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF69F0AE))
            DigiIconType.CLOSE -> drawCloseIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFF5252))
            DigiIconType.REWARD -> drawRewardChestIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFD54F))
            DigiIconType.SPARKLE -> drawSparkleIcon(w, h, s, if (hasCustomTint) tint else Color(0xFFFFD54F))
            DigiIconType.WATCH, DigiIconType.WATCH_DEVICE -> drawWatchIcon(w, h, s, if (hasCustomTint) tint else Color(0xFF00E5FF))
        }
    }
}

@Composable
fun VPetMeterBar(
    count: Int = 0,
    maxCount: Int = 4,
    isHeart: Boolean = true,
    iconSize: Dp = 9.dp,
    activeColor: Color = if (isHeart) Color(0xFFFF5252) else Color(0xFFFFD54F),
    emptyColor: Color = Color(0xFF546E7A),
    modifier: Modifier = Modifier,
    filledCount: Int = count,
    max: Int = maxCount,
    isHearts: Boolean = isHeart
) {
    val actualCount = if (count != 0) count else filledCount
    val actualMax = if (maxCount != 4) maxCount else max
    val actualIsHeart = if (!isHearts) false else isHeart
    val clamped = actualCount.coerceIn(0, actualMax)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until actualMax) {
            val filled = i < clamped
            DigiVectorIcon(
                type = when {
                    actualIsHeart && filled -> DigiIconType.HEART_FULL
                    actualIsHeart && !filled -> DigiIconType.HEART_EMPTY
                    !actualIsHeart && filled -> DigiIconType.STAR_FULL
                    else -> DigiIconType.STAR_EMPTY
                },
                size = iconSize,
                tint = if (filled) activeColor else emptyColor
            )
        }
    }
}

@Composable
fun VPetWasteRow(
    wasteCount: Int,
    iconSize: Dp = 11.dp,
    modifier: Modifier = Modifier
) {
    val count = wasteCount.coerceIn(0, 4)
    if (count <= 0) return
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) {
            DigiVectorIcon(
                type = DigiIconType.WASTE,
                size = iconSize
            )
        }
    }
}

@Composable
fun MedsPillOverlayIcon(
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DigiVectorIcon(type = DigiIconType.SPARKLE, size = size * 0.45f, tint = Color(0xFF69F0AE))
        DigiVectorIcon(type = DigiIconType.MEDS_PILL, size = size)
        DigiVectorIcon(type = DigiIconType.SPARKLE, size = size * 0.45f, tint = Color(0xFF69F0AE))
    }
}

// ============================================================================
// Vector Drawing Implementations
// ============================================================================

private fun DrawScope.drawMedicalPillIcon(w: Float, h: Float, s: Float, hasTint: Boolean, tint: Color) {
    // Iconic 45-degree Two-Tone Medical Pill Capsule (NEVER a syringe!)
    withTransform({
        rotate(degrees = -38f, pivot = Offset(w * 0.5f, h * 0.5f))
    }) {
        val pillW = w * 0.80f
        val pillH = h * 0.42f
        val left = (w - pillW) * 0.5f
        val top = (h - pillH) * 0.5f
        val radius = pillH * 0.5f

        val leftColor = if (hasTint) tint else Color(0xFFFF5252)
        val rightColor = if (hasTint) tint.copy(alpha = 0.45f) else Color(0xFFF5F7FA)
        val borderColor = if (hasTint) tint else Color(0xFF1A1E26)

        // Full capsule base (right half white)
        drawRoundRect(
            color = rightColor,
            topLeft = Offset(left, top),
            size = Size(pillW, pillH),
            cornerRadius = CornerRadius(radius, radius)
        )

        // Left half crimson red cap
        drawRoundRect(
            color = leftColor,
            topLeft = Offset(left, top),
            size = Size(pillW * 0.54f, pillH),
            cornerRadius = CornerRadius(radius, radius)
        )
        drawRect(
            color = leftColor,
            topLeft = Offset(left + pillW * 0.30f, top),
            size = Size(pillW * 0.22f, pillH)
        )

        // Center divider band
        drawLine(
            color = borderColor,
            start = Offset(w * 0.5f, top),
            end = Offset(w * 0.5f, top + pillH),
            strokeWidth = (s * 0.08f).coerceAtLeast(1.2f)
        )

        // Specular shine strip along top edge
        drawLine(
            color = Color.White.copy(alpha = 0.85f),
            start = Offset(left + radius * 0.65f, top + pillH * 0.26f),
            end = Offset(left + pillW - radius * 0.65f, top + pillH * 0.26f),
            strokeWidth = (s * 0.07f).coerceAtLeast(1f),
            cap = StrokeCap.Round
        )

        // Outer capsule outline
        drawRoundRect(
            color = borderColor,
            topLeft = Offset(left, top),
            size = Size(pillW, pillH),
            cornerRadius = CornerRadius(radius, radius),
            style = Stroke(width = (s * 0.08f).coerceAtLeast(1.1f))
        )
    }
}

private fun DrawScope.drawMeatIcon(w: Float, h: Float, s: Float, hasTint: Boolean, tint: Color) {
    val boneColor = if (hasTint) tint.copy(alpha = 0.85f) else Color(0xFFF5F0E6)
    val meatColor = if (hasTint) tint else Color(0xFFD84315)
    val highlightColor = if (hasTint) Color.White.copy(alpha = 0.6f) else Color(0xFFFF8A65)

    // Bone shaft
    drawRoundRect(
        color = boneColor,
        topLeft = Offset(w * 0.08f, h * 0.42f),
        size = Size(w * 0.84f, h * 0.16f),
        cornerRadius = CornerRadius(s * 0.06f, s * 0.06f)
    )
    // Bone knobs (left & right)
    val knobR = s * 0.11f
    drawCircle(boneColor, radius = knobR, center = Offset(w * 0.14f, h * 0.39f))
    drawCircle(boneColor, radius = knobR, center = Offset(w * 0.14f, h * 0.61f))
    drawCircle(boneColor, radius = knobR, center = Offset(w * 0.86f, h * 0.39f))
    drawCircle(boneColor, radius = knobR, center = Offset(w * 0.86f, h * 0.61f))

    // Meat roast center
    drawRoundRect(
        color = meatColor,
        topLeft = Offset(w * 0.25f, h * 0.20f),
        size = Size(w * 0.50f, h * 0.60f),
        cornerRadius = CornerRadius(s * 0.16f, s * 0.16f)
    )
    // Highlight
    drawRoundRect(
        color = highlightColor,
        topLeft = Offset(w * 0.32f, h * 0.27f),
        size = Size(w * 0.36f, h * 0.12f),
        cornerRadius = CornerRadius(s * 0.05f, s * 0.05f)
    )
}

private fun DrawScope.drawVitaminIcon(w: Float, h: Float, s: Float, hasTint: Boolean, tint: Color) {
    val capColor = if (hasTint) tint else Color(0xFFFFB300)
    val bodyColor = if (hasTint) tint.copy(alpha = 0.8f) else Color(0xFFFFA000)
    val crossColor = Color.White

    // Bottle cap
    drawRoundRect(
        color = capColor,
        topLeft = Offset(w * 0.34f, h * 0.10f),
        size = Size(w * 0.32f, h * 0.14f),
        cornerRadius = CornerRadius(s * 0.04f, s * 0.04f)
    )
    // Bottle body
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(w * 0.22f, h * 0.26f),
        size = Size(w * 0.56f, h * 0.64f),
        cornerRadius = CornerRadius(s * 0.12f, s * 0.12f)
    )
    // Plus emblem on vitamin bottle
    val stroke = (s * 0.10f).coerceAtLeast(1.2f)
    drawLine(crossColor, Offset(w * 0.50f, h * 0.42f), Offset(w * 0.50f, h * 0.72f), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(crossColor, Offset(w * 0.35f, h * 0.57f), Offset(w * 0.65f, h * 0.57f), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawScaleIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.2f)
    // Base & center pillar
    drawLine(color, Offset(w * 0.25f, h * 0.86f), Offset(w * 0.75f, h * 0.86f), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(w * 0.50f, h * 0.22f), Offset(w * 0.50f, h * 0.86f), strokeWidth = stroke, cap = StrokeCap.Round)
    // Balance beam
    drawLine(color, Offset(w * 0.16f, h * 0.34f), Offset(w * 0.84f, h * 0.34f), strokeWidth = stroke, cap = StrokeCap.Round)
    // Left & right pans
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.10f, h * 0.44f),
        size = Size(w * 0.28f, h * 0.22f)
    )
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.62f, h * 0.44f),
        size = Size(w * 0.28f, h * 0.22f)
    )
}

private fun DrawScope.drawGloveIcon(w: Float, h: Float, s: Float, color: Color) {
    // Padded main fist
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.20f, h * 0.16f),
        size = Size(w * 0.62f, h * 0.48f),
        cornerRadius = CornerRadius(s * 0.18f, s * 0.18f)
    )
    // Thumb lobe
    drawRoundRect(
        color = color.copy(alpha = 0.85f),
        topLeft = Offset(w * 0.12f, h * 0.34f),
        size = Size(w * 0.24f, h * 0.26f),
        cornerRadius = CornerRadius(s * 0.10f, s * 0.10f)
    )
    // Wrist cuff
    drawRoundRect(
        color = Color(0xFFECEFF1),
        topLeft = Offset(w * 0.28f, h * 0.66f),
        size = Size(w * 0.46f, h * 0.20f),
        cornerRadius = CornerRadius(s * 0.06f, s * 0.06f)
    )
}

private fun DrawScope.drawCrossedSwordsIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.10f).coerceAtLeast(1.3f)
    // Blade 1 (bottom-left to top-right)
    drawLine(color, Offset(w * 0.18f, h * 0.82f), Offset(w * 0.82f, h * 0.18f), strokeWidth = stroke, cap = StrokeCap.Round)
    // Guard 1
    drawLine(color, Offset(w * 0.16f, h * 0.64f), Offset(w * 0.36f, h * 0.84f), strokeWidth = stroke, cap = StrokeCap.Round)
    // Blade 2 (top-left to bottom-right)
    drawLine(color, Offset(w * 0.18f, h * 0.18f), Offset(w * 0.82f, h * 0.82f), strokeWidth = stroke, cap = StrokeCap.Round)
    // Guard 2
    drawLine(color, Offset(w * 0.64f, h * 0.84f), Offset(w * 0.84f, h * 0.64f), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawFlushWaveIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.10f).coerceAtLeast(1.2f)
    for (i in 0..2) {
        val y = h * (0.28f + i * 0.22f)
        val path = Path().apply {
            moveTo(w * 0.12f, y)
            quadraticBezierTo(w * 0.31f, y - h * 0.14f, w * 0.50f, y)
            quadraticBezierTo(w * 0.69f, y + h * 0.14f, w * 0.88f, y)
        }
        drawPath(path, color = color, style = Stroke(width = stroke, cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawLightOnIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.08f).coerceAtLeast(1.1f)
    // Bulb globe
    drawCircle(color = color, radius = s * 0.26f, center = Offset(w * 0.5f, h * 0.42f))
    // Screw base
    drawRoundRect(
        color = Color(0xFFB0BEC5),
        topLeft = Offset(w * 0.36f, h * 0.66f),
        size = Size(w * 0.28f, h * 0.18f),
        cornerRadius = CornerRadius(s * 0.04f, s * 0.04f)
    )
    // Rays
    val angles = floatArrayOf(-150f, -90f, -30f, 20f, 160f)
    for (deg in angles) {
        val rad = Math.toRadians(deg.toDouble())
        val r1 = s * 0.34f
        val r2 = s * 0.46f
        val cx = w * 0.5f
        val cy = h * 0.42f
        drawLine(
            color = color,
            start = Offset(cx + (r1 * cos(rad)).toFloat(), cy + (r1 * sin(rad)).toFloat()),
            end = Offset(cx + (r2 * cos(rad)).toFloat(), cy + (r2 * sin(rad)).toFloat()),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawLightOffMoonIcon(w: Float, h: Float, s: Float, color: Color) {
    // Crescent moon via overlapping arcs
    val path = Path().apply {
        moveTo(w * 0.62f, h * 0.16f)
        cubicTo(w * 0.28f, h * 0.18f, w * 0.18f, h * 0.72f, w * 0.58f, h * 0.84f)
        cubicTo(w * 0.80f, h * 0.86f, w * 0.86f, h * 0.70f, w * 0.78f, h * 0.64f)
        cubicTo(w * 0.48f, h * 0.64f, w * 0.44f, h * 0.30f, w * 0.62f, h * 0.16f)
        close()
    }
    drawPath(path, color = color)
    drawCircle(color = color, radius = s * 0.06f, center = Offset(w * 0.78f, h * 0.28f))
}

private fun DrawScope.drawAttentionBeaconIcon(w: Float, h: Float, s: Float, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.50f, h * 0.12f)
        lineTo(w * 0.88f, h * 0.84f)
        lineTo(w * 0.12f, h * 0.84f)
        close()
    }
    drawPath(path, color = color.copy(alpha = 0.22f))
    drawPath(path, color = color, style = Stroke(width = (s * 0.09f).coerceAtLeast(1.2f), join = StrokeJoin.Round))
    // Exclamation mark
    drawLine(
        color = color,
        start = Offset(w * 0.50f, h * 0.36f),
        end = Offset(w * 0.50f, h * 0.60f),
        strokeWidth = (s * 0.10f).coerceAtLeast(1.3f),
        cap = StrokeCap.Round
    )
    drawCircle(color = color, radius = s * 0.055f, center = Offset(w * 0.50f, h * 0.73f))
}

private fun DrawScope.drawCoiledWasteIcon(w: Float, h: Float, s: Float, hasTint: Boolean, tint: Color) {
    // Classic 1997 Bandai V-Pet 3-Tier Coiled Poop Sprite
    val bodyColor = if (hasTint) tint else Color(0xFF8D6E63)
    val shadeColor = if (hasTint) tint.copy(alpha = 0.7f) else Color(0xFF5D4037)
    val highlight = Color(0xFFD7CCC8)

    // Bottom coil
    drawRoundRect(
        color = shadeColor,
        topLeft = Offset(w * 0.14f, h * 0.66f),
        size = Size(w * 0.72f, h * 0.22f),
        cornerRadius = CornerRadius(s * 0.10f, s * 0.10f)
    )
    // Middle coil
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(w * 0.22f, h * 0.46f),
        size = Size(w * 0.56f, h * 0.22f),
        cornerRadius = CornerRadius(s * 0.10f, s * 0.10f)
    )
    // Top coil & tip
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(w * 0.34f, h * 0.28f),
        size = Size(w * 0.32f, h * 0.20f),
        cornerRadius = CornerRadius(s * 0.09f, s * 0.09f)
    )
    val tip = Path().apply {
        moveTo(w * 0.44f, h * 0.30f)
        lineTo(w * 0.52f, h * 0.14f)
        lineTo(w * 0.60f, h * 0.30f)
        close()
    }
    drawPath(tip, color = bodyColor)
    // Pixel shine
    drawCircle(highlight, radius = s * 0.045f, center = Offset(w * 0.36f, h * 0.55f))
}

private fun DrawScope.drawHeartIcon(w: Float, h: Float, s: Float, filled: Boolean, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.50f, h * 0.84f)
        cubicTo(w * 0.12f, h * 0.60f, w * 0.08f, h * 0.26f, w * 0.30f, h * 0.18f)
        cubicTo(w * 0.42f, h * 0.14f, w * 0.50f, h * 0.26f, w * 0.50f, h * 0.32f)
        cubicTo(w * 0.50f, h * 0.26f, w * 0.58f, h * 0.14f, w * 0.70f, h * 0.18f)
        cubicTo(w * 0.92f, h * 0.26f, w * 0.88f, h * 0.60f, w * 0.50f, h * 0.84f)
        close()
    }
    if (filled) {
        drawPath(path, color = color)
    } else {
        drawPath(path, color = color, style = Stroke(width = (s * 0.11f).coerceAtLeast(1.1f), join = StrokeJoin.Round))
    }
}

private fun DrawScope.drawStarIcon(w: Float, h: Float, s: Float, filled: Boolean, color: Color) {
    val cx = w * 0.5f
    val cy = h * 0.5f
    val outerR = s * 0.44f
    val innerR = s * 0.20f
    val path = Path()
    for (i in 0 until 10) {
        val r = if (i % 2 == 0) outerR else innerR
        val angle = Math.toRadians((i * 36.0) - 90.0)
        val x = cx + (r * cos(angle)).toFloat()
        val y = cy + (r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    if (filled) {
        drawPath(path, color = color)
    } else {
        drawPath(path, color = color, style = Stroke(width = (s * 0.10f).coerceAtLeast(1.1f), join = StrokeJoin.Round))
    }
}

private fun DrawScope.drawTombstoneIcon(w: Float, h: Float, s: Float, color: Color) {
    // Rounded arch headstone
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.24f, h * 0.14f),
        size = Size(w * 0.52f, h * 0.66f),
        cornerRadius = CornerRadius(w * 0.26f, w * 0.26f)
    )
    // Plinth base
    drawRoundRect(
        color = color.copy(alpha = 0.8f),
        topLeft = Offset(w * 0.14f, h * 0.76f),
        size = Size(w * 0.72f, h * 0.12f),
        cornerRadius = CornerRadius(s * 0.04f, s * 0.04f)
    )
    // Engraved cross
    val crossColor = Color(0xFF263238)
    val stroke = (s * 0.09f).coerceAtLeast(1.2f)
    drawLine(crossColor, Offset(w * 0.50f, h * 0.30f), Offset(w * 0.50f, h * 0.62f), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(crossColor, Offset(w * 0.36f, h * 0.42f), Offset(w * 0.64f, h * 0.42f), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawDigiEggIcon(w: Float, h: Float, s: Float, hasTint: Boolean, tint: Color) {
    val shellColor = if (hasTint) tint else Color(0xFFF5F5F5)
    val spotColor = if (hasTint) Color(0xFF263238) else Color(0xFFFF7043)
    // Oval egg body
    val path = Path().apply {
        moveTo(w * 0.50f, h * 0.10f)
        cubicTo(w * 0.78f, h * 0.10f, w * 0.86f, h * 0.52f, w * 0.82f, h * 0.72f)
        cubicTo(w * 0.78f, h * 0.88f, w * 0.64f, h * 0.90f, w * 0.50f, h * 0.90f)
        cubicTo(w * 0.36f, h * 0.90f, w * 0.22f, h * 0.88f, w * 0.18f, h * 0.72f)
        cubicTo(w * 0.14f, h * 0.52f, w * 0.22f, h * 0.10f, w * 0.50f, h * 0.10f)
        close()
    }
    drawPath(path, color = shellColor)
    // Digi-Egg spots
    drawCircle(spotColor, radius = s * 0.09f, center = Offset(w * 0.40f, h * 0.42f))
    drawCircle(spotColor, radius = s * 0.11f, center = Offset(w * 0.62f, h * 0.58f))
    drawCircle(spotColor, radius = s * 0.07f, center = Offset(w * 0.38f, h * 0.70f))
}

private fun DrawScope.drawRadarCompassIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    val center = Offset(w * 0.5f, h * 0.5f)
    drawCircle(color, radius = s * 0.40f, center = center, style = Stroke(width = stroke))
    drawLine(color.copy(alpha = 0.6f), Offset(w * 0.5f, h * 0.12f), Offset(w * 0.5f, h * 0.88f), strokeWidth = stroke * 0.8f)
    drawLine(color.copy(alpha = 0.6f), Offset(w * 0.12f, h * 0.5f), Offset(w * 0.88f, h * 0.5f), strokeWidth = stroke * 0.8f)
    drawLine(color, center, Offset(w * 0.76f, h * 0.24f), strokeWidth = stroke * 1.1f, cap = StrokeCap.Round)
    drawCircle(color, radius = s * 0.08f, center = center)
}

private fun DrawScope.drawGearIcon(w: Float, h: Float, s: Float, color: Color) {
    val cx = w * 0.5f
    val cy = h * 0.5f
    val stroke = (s * 0.12f).coerceAtLeast(1.3f)
    drawCircle(color, radius = s * 0.26f, center = Offset(cx, cy), style = Stroke(width = stroke))
    for (i in 0 until 8) {
        val rad = Math.toRadians(i * 45.0)
        val r1 = s * 0.28f
        val r2 = s * 0.42f
        drawLine(
            color = color,
            start = Offset(cx + (r1 * cos(rad)).toFloat(), cy + (r1 * sin(rad)).toFloat()),
            end = Offset(cx + (r2 * cos(rad)).toFloat(), cy + (r2 * sin(rad)).toFloat()),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawBoltIcon(w: Float, h: Float, s: Float, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.58f, h * 0.08f)
        lineTo(w * 0.22f, h * 0.54f)
        lineTo(w * 0.48f, h * 0.54f)
        lineTo(w * 0.38f, h * 0.92f)
        lineTo(w * 0.78f, h * 0.44f)
        lineTo(w * 0.52f, h * 0.44f)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawTrophyIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    // Cup bowl
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.26f, h * 0.14f),
        size = Size(w * 0.48f, h * 0.42f),
        cornerRadius = CornerRadius(s * 0.12f, s * 0.12f)
    )
    // Handles
    drawArc(color, 90f, 180f, false, Offset(w * 0.12f, h * 0.20f), Size(w * 0.24f, h * 0.26f), style = Stroke(stroke))
    drawArc(color, -90f, 180f, false, Offset(w * 0.64f, h * 0.20f), Size(w * 0.24f, h * 0.26f), style = Stroke(stroke))
    // Stem & Base
    drawLine(color, Offset(w * 0.50f, h * 0.56f), Offset(w * 0.50f, h * 0.82f), strokeWidth = stroke * 1.2f)
    drawLine(color, Offset(w * 0.28f, h * 0.84f), Offset(w * 0.72f, h * 0.84f), strokeWidth = stroke * 1.3f, cap = StrokeCap.Round)
}

private fun DrawScope.drawBossSkullIcon(w: Float, h: Float, s: Float, color: Color) {
    // Cranium
    drawCircle(color, radius = s * 0.34f, center = Offset(w * 0.50f, h * 0.42f))
    // Jaw
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.32f, h * 0.60f),
        size = Size(w * 0.36f, h * 0.24f),
        cornerRadius = CornerRadius(s * 0.05f, s * 0.05f)
    )
    // Eye sockets
    val socketColor = Color(0xFF141820)
    drawCircle(socketColor, radius = s * 0.08f, center = Offset(w * 0.38f, h * 0.42f))
    drawCircle(socketColor, radius = s * 0.08f, center = Offset(w * 0.62f, h * 0.42f))
}

private fun DrawScope.drawNfcWaveIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    drawCircle(color, radius = s * 0.08f, center = Offset(w * 0.24f, h * 0.72f))
    for (i in 1..3) {
        val box = s * (0.24f * i)
        drawArc(
            color = color,
            startAngle = -85f,
            sweepAngle = 80f,
            useCenter = false,
            topLeft = Offset(w * 0.24f - box * 0.5f, h * 0.72f - box * 0.5f),
            size = Size(box, box),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawScannerReticleIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.2f)
    val len = s * 0.22f
    val m = s * 0.14f
    // Top-left
    drawLine(color, Offset(m, m), Offset(m + len, m), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(m, m), Offset(m, m + len), strokeWidth = stroke, cap = StrokeCap.Round)
    // Top-right
    drawLine(color, Offset(w - m, m), Offset(w - m - len, m), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(w - m, m), Offset(w - m, m + len), strokeWidth = stroke, cap = StrokeCap.Round)
    // Bottom-left
    drawLine(color, Offset(m, h - m), Offset(m + len, h - m), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(m, h - m), Offset(m, h - m - len), strokeWidth = stroke, cap = StrokeCap.Round)
    // Bottom-right
    drawLine(color, Offset(w - m, h - m), Offset(w - m - len, h - m), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(w - m, h - m), Offset(w - m, h - m - len), strokeWidth = stroke, cap = StrokeCap.Round)
    // Laser scan line
    drawLine(color, Offset(w * 0.20f, h * 0.50f), Offset(w * 0.80f, h * 0.50f), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawNetworkGlobeIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.085f).coerceAtLeast(1.1f)
    val c = Offset(w * 0.5f, h * 0.5f)
    val r = s * 0.38f
    drawCircle(color, radius = r, center = c, style = Stroke(width = stroke))
    drawOval(color, topLeft = Offset(w * 0.32f, h * 0.12f), size = Size(w * 0.36f, h * 0.76f), style = Stroke(width = stroke))
    drawLine(color, Offset(w * 0.12f, h * 0.50f), Offset(w * 0.88f, h * 0.50f), strokeWidth = stroke)
}

private fun DrawScope.drawColosseumIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    // Pediment roof
    val roof = Path().apply {
        moveTo(w * 0.50f, h * 0.12f)
        lineTo(w * 0.88f, h * 0.34f)
        lineTo(w * 0.12f, h * 0.34f)
        close()
    }
    drawPath(roof, color = color)
    // 3 Pillars
    for (xFactor in floatArrayOf(0.24f, 0.50f, 0.76f)) {
        drawLine(color, Offset(w * xFactor, h * 0.36f), Offset(w * xFactor, h * 0.78f), strokeWidth = stroke * 1.2f)
    }
    // Base
    drawLine(color, Offset(w * 0.12f, h * 0.84f), Offset(w * 0.88f, h * 0.84f), strokeWidth = stroke * 1.3f, cap = StrokeCap.Round)
}

private fun DrawScope.drawCameraIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.12f, h * 0.28f),
        size = Size(w * 0.76f, h * 0.54f),
        cornerRadius = CornerRadius(s * 0.10f, s * 0.10f),
        style = Stroke(width = stroke)
    )
    drawCircle(color, radius = s * 0.16f, center = Offset(w * 0.50f, h * 0.55f), style = Stroke(width = stroke))
}

private fun DrawScope.drawQrIcon(w: Float, h: Float, s: Float, color: Color) {
    val box = s * 0.32f
    val m = s * 0.12f
    val stroke = (s * 0.08f).coerceAtLeast(1f)
    drawRect(color, Offset(m, m), Size(box, box), style = Stroke(stroke))
    drawRect(color, Offset(w - m - box, m), Size(box, box), style = Stroke(stroke))
    drawRect(color, Offset(m, h - m - box), Size(box, box), style = Stroke(stroke))
    drawRect(color, Offset(w * 0.56f, h * 0.56f), Size(box * 0.8f, box * 0.8f))
}

private fun DrawScope.drawSoundIcon(w: Float, h: Float, s: Float, muted: Boolean, color: Color) {
    val cone = Path().apply {
        moveTo(w * 0.14f, h * 0.38f)
        lineTo(w * 0.32f, h * 0.38f)
        lineTo(w * 0.54f, h * 0.18f)
        lineTo(w * 0.54f, h * 0.82f)
        lineTo(w * 0.32f, h * 0.62f)
        lineTo(w * 0.14f, h * 0.62f)
        close()
    }
    drawPath(cone, color = color)
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    if (muted) {
        drawLine(Color(0xFFFF5252), Offset(w * 0.64f, h * 0.34f), Offset(w * 0.88f, h * 0.66f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(Color(0xFFFF5252), Offset(w * 0.88f, h * 0.34f), Offset(w * 0.64f, h * 0.66f), strokeWidth = stroke, cap = StrokeCap.Round)
    } else {
        drawArc(color, -45f, 90f, false, Offset(w * 0.46f, h * 0.28f), Size(w * 0.38f, h * 0.44f), style = Stroke(stroke, cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawShieldIcon(w: Float, h: Float, s: Float, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.50f, h * 0.10f)
        lineTo(w * 0.84f, h * 0.24f)
        lineTo(w * 0.84f, h * 0.54f)
        cubicTo(w * 0.84f, h * 0.74f, w * 0.66f, h * 0.86f, w * 0.50f, h * 0.92f)
        cubicTo(w * 0.34f, h * 0.86f, w * 0.16f, h * 0.74f, w * 0.16f, h * 0.54f)
        lineTo(w * 0.16f, h * 0.24f)
        close()
    }
    drawPath(path, color = color.copy(alpha = 0.25f))
    drawPath(path, color = color, style = Stroke(width = (s * 0.09f).coerceAtLeast(1.2f), join = StrokeJoin.Round))
}

private fun DrawScope.drawSweatDropIcon(w: Float, h: Float, s: Float, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.50f, h * 0.12f)
        cubicTo(w * 0.78f, h * 0.46f, w * 0.82f, h * 0.66f, w * 0.72f, h * 0.80f)
        cubicTo(w * 0.60f, h * 0.92f, w * 0.40f, h * 0.92f, w * 0.28f, h * 0.80f)
        cubicTo(w * 0.18f, h * 0.66f, w * 0.22f, h * 0.46f, w * 0.50f, h * 0.12f)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawStepBootIcon(w: Float, h: Float, s: Float, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.22f, h * 0.20f)
        lineTo(w * 0.48f, h * 0.20f)
        lineTo(w * 0.52f, h * 0.54f)
        lineTo(w * 0.86f, h * 0.62f)
        lineTo(w * 0.86f, h * 0.82f)
        lineTo(w * 0.16f, h * 0.82f)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawCopyIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    drawRoundRect(color, Offset(w * 0.16f, h * 0.16f), Size(w * 0.50f, h * 0.54f), CornerRadius(s * 0.06f), style = Stroke(stroke))
    drawRoundRect(color, Offset(w * 0.34f, h * 0.30f), Size(w * 0.50f, h * 0.54f), CornerRadius(s * 0.06f))
}

private fun DrawScope.drawKeypadIcon(w: Float, h: Float, s: Float, color: Color) {
    val dotR = s * 0.08f
    for (r in 0..2) {
        for (c in 0..2) {
            drawCircle(color, radius = dotR, center = Offset(w * (0.25f + c * 0.25f), h * (0.25f + r * 0.25f)))
        }
    }
}

private fun DrawScope.drawBackspaceIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    val path = Path().apply {
        moveTo(w * 0.36f, h * 0.24f)
        lineTo(w * 0.86f, h * 0.24f)
        lineTo(w * 0.86f, h * 0.76f)
        lineTo(w * 0.36f, h * 0.76f)
        lineTo(w * 0.14f, h * 0.50f)
        close()
    }
    drawPath(path, color = color, style = Stroke(width = stroke, join = StrokeJoin.Round))
    drawLine(color, Offset(w * 0.48f, h * 0.38f), Offset(w * 0.70f, h * 0.62f), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(w * 0.70f, h * 0.38f), Offset(w * 0.48f, h * 0.62f), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawCheckIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.12f).coerceAtLeast(1.4f)
    val path = Path().apply {
        moveTo(w * 0.18f, h * 0.52f)
        lineTo(w * 0.42f, h * 0.76f)
        lineTo(w * 0.84f, h * 0.24f)
    }
    drawPath(path, color = color, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.drawCloseIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.12f).coerceAtLeast(1.4f)
    drawLine(color, Offset(w * 0.22f, h * 0.22f), Offset(w * 0.78f, h * 0.78f), strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, Offset(w * 0.78f, h * 0.22f), Offset(w * 0.22f, h * 0.78f), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawRewardChestIcon(w: Float, h: Float, s: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.16f, h * 0.34f),
        size = Size(w * 0.68f, h * 0.52f),
        cornerRadius = CornerRadius(s * 0.08f, s * 0.08f)
    )
    drawRect(Color(0xFF263238), Offset(w * 0.44f, h * 0.34f), Size(w * 0.12f, h * 0.52f))
    drawRect(Color(0xFF263238), Offset(w * 0.16f, h * 0.54f), Size(w * 0.68f, h * 0.10f))
}

private fun DrawScope.drawSparkleIcon(w: Float, h: Float, s: Float, color: Color) {
    val path = Path().apply {
        moveTo(w * 0.50f, h * 0.08f)
        lineTo(w * 0.62f, h * 0.38f)
        lineTo(w * 0.92f, h * 0.50f)
        lineTo(w * 0.62f, h * 0.62f)
        lineTo(w * 0.50f, h * 0.92f)
        lineTo(w * 0.38f, h * 0.62f)
        lineTo(w * 0.08f, h * 0.50f)
        lineTo(w * 0.38f, h * 0.38f)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawWatchIcon(w: Float, h: Float, s: Float, color: Color) {
    val stroke = (s * 0.09f).coerceAtLeast(1.1f)
    drawRect(color.copy(alpha = 0.6f), Offset(w * 0.36f, h * 0.08f), Size(w * 0.28f, h * 0.84f))
    drawCircle(Color(0xFF101820), radius = s * 0.32f, center = Offset(w * 0.50f, h * 0.50f))
    drawCircle(color, radius = s * 0.32f, center = Offset(w * 0.50f, h * 0.50f), style = Stroke(stroke))
}
