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
    val isWearOsSystem: Boolean,
    val runtimePlatformLabel: String,
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
            val context = androidx.compose.ui.platform.LocalContext.current
            val isRound = config.isScreenRound
            val width = config.screenWidthDp
            val height = config.screenHeightDp
            val isWearOs = context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WATCH)
            val platformLabel = if (isWearOs) "WEAR OS" else "AOSP WATCH"

            return if (isRound) {
                // Round watches (Wear OS or Round AOSP Full Android Watches): apply circular insets
                WatchDisplayConfig(
                    isRound = true,
                    isWearOsSystem = isWearOs,
                    runtimePlatformLabel = "$platformLabel • ROUND",
                    screenWidthDp = width,
                    screenHeightDp = height,
                    horizontalPadding = 14.dp,
                    verticalPadding = 10.dp,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 24.dp),
                    mainSpriteSize = if (width >= 250) 104.dp else 92.dp,
                    battleSpriteSize = if (width >= 250) 62.dp else 56.dp,
                    headerFontSize = 11.sp,
                    buttonSize = 40.dp,
                    actionRowSpacing = 2.dp
                )
            } else {
                // Square & Rectangular watches (AOSP Full Android Watches, Oppo Watch, square form factors)
                WatchDisplayConfig(
                    isRound = false,
                    isWearOsSystem = isWearOs,
                    runtimePlatformLabel = "$platformLabel • RECT/SQ",
                    screenWidthDp = width,
                    screenHeightDp = height,
                    horizontalPadding = 6.dp,
                    verticalPadding = 6.dp,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 12.dp),
                    mainSpriteSize = if (width >= 260) 118.dp else 108.dp,
                    battleSpriteSize = if (width >= 260) 70.dp else 64.dp,
                    headerFontSize = 12.sp,
                    buttonSize = 44.dp,
                    actionRowSpacing = 4.dp
                )
            }
        }
    }
}
