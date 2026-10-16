package com.digiwatch.wear.render

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SpriteCanvasRenderer(
    speciesId: Int,
    isAmbient: Boolean,
    overrideFrame: SpriteFrame? = null,
    isSleeping: Boolean = false,
    isInjured: Boolean = false,
    modifier: Modifier = Modifier,
    size: Dp = 128.dp
) {
    // 2 Hz V-Pet LCD frame toggle in Interactive Mode; paused whenever frame is static
    var currentIdleFrame by remember { mutableStateOf(SpriteFrame.IDLE_A) }
    val isStaticFrame = isAmbient || isInjured || overrideFrame != null

    LaunchedEffect(isStaticFrame) {
        if (isStaticFrame) return@LaunchedEffect

        while (true) {
            delay(500L)
            currentIdleFrame = if (currentIdleFrame == SpriteFrame.IDLE_A) {
                SpriteFrame.IDLE_B
            } else {
                SpriteFrame.IDLE_A
            }
        }
    }

    val activeFrame = overrideFrame ?: when {
        isInjured -> SpriteFrame.INJURED
        isSleeping -> if (currentIdleFrame == SpriteFrame.IDLE_A) SpriteFrame.SLEEP_A else SpriteFrame.SLEEP_B
        else -> currentIdleFrame
    }

    val imageBitmap = remember(speciesId, activeFrame, isAmbient) {
        PixelArtMatrices.getFrameBitmap(speciesId, activeFrame, isAmbient)
    }

    // OLED Burn-in margin (minimum 10px padding enforced in ambient mode)
    val burnInPadding = if (isAmbient) 10.dp else 0.dp

    Box(
        modifier = modifier.padding(burnInPadding),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val dstPx = size.roundToPx()
            // Draw 64x64 pixel sprite scaled with FilterQuality.None to avoid bilinear blurring
            drawImage(
                image = imageBitmap,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(64, 64),
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(dstPx, dstPx),
                filterQuality = FilterQuality.None
            )
        }
    }
}
