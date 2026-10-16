package com.digiwatch.wear.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Adaptive configuration catering to Round and Square/Rectangular Wear OS watch displays.
 * Guarantees zero content clipping on circular displays while maximizing screen real-estate
 * on square or rectangular watch form factors.
 */
data class WatchDisplayConfig(
    val isRound: Boolean,
    val screenWidthDp: Int,
    val screenHeightDp: Int,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val contentPadding: PaddingValues,
    val mainSpriteSize: Dp,
    val battleSpriteSize: Dp,
    val headerFontSize: androidx.compose.ui.unit.TextUnit,
    val buttonSize: Dp,
    val actionRowSpacing: Dp
) {
    companion object {
        @Composable
        @ReadOnlyComposable
        fun current(): WatchDisplayConfig {
            val config = LocalConfiguration.current
            val isRound = config.isScreenRound
            val width = config.screenWidthDp
            val height = config.screenHeightDp

            return if (isRound) {
                // Round watches: circular curvature cuts off corners; apply curved insets
                WatchDisplayConfig(
                    isRound = true,
                    screenWidthDp = width,
                    screenHeightDp = height,
                    horizontalPadding = 14.dp,
                    verticalPadding = 10.dp,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 24.dp),
                    mainSpriteSize = 92.dp,
                    battleSpriteSize = 56.dp,
                    headerFontSize = 11.sp,
                    buttonSize = 40.dp,
                    actionRowSpacing = 2.dp
                )
            } else {
                // Square & Rectangular watches (e.g. Oppo Watch, square form factors)
                WatchDisplayConfig(
                    isRound = false,
                    screenWidthDp = width,
                    screenHeightDp = height,
                    horizontalPadding = 6.dp,
                    verticalPadding = 6.dp,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 12.dp),
                    mainSpriteSize = 108.dp,
                    battleSpriteSize = 64.dp,
                    headerFontSize = 12.sp,
                    buttonSize = 44.dp,
                    actionRowSpacing = 4.dp
                )
            }
        }
    }
}
