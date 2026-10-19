package com.digiwear.wear.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Adaptive configuration catering to Round and Square/Rectangular Wear OS & AOSP watch displays.
 * Guarantees zero content clipping on circular displays while maximizing screen real-estate
 * on square, rectangular, and high-DPI Full Android Watch form factors.
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
        fun current(): WatchDisplayConfig {
            val config = LocalConfiguration.current
            val context = androidx.compose.ui.platform.LocalContext.current
            val shapeOverride by remember(context) {
                com.digiwear.wear.data.PetRepository.getInstance(context).displayShapeOverrideFlow
            }.collectAsState()
            val isRound = when (shapeOverride) {
                "ROUND" -> true
                "SQUARE" -> false
                else -> config.isScreenRound
            }
            val width = config.screenWidthDp
            val height = config.screenHeightDp
            val isWearOs = context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WATCH)
            val platformLabel = if (isWearOs) "WEAR OS" else "AOSP WATCH"
            val shapeSuffix = if (shapeOverride != "AUTO") " ($shapeOverride)" else ""

            return if (isRound) {
                // Round watches (Wear OS or Round AOSP Full Android Watches): apply circular insets
                val horizPad = when {
                    width >= 300 -> 22.dp
                    width >= 240 -> 16.dp
                    else -> 14.dp
                }
                val vertPad = when {
                    height >= 300 -> 28.dp
                    height >= 240 -> 24.dp
                    else -> 18.dp
                }
                WatchDisplayConfig(
                    isRound = true,
                    isWearOsSystem = isWearOs,
                    runtimePlatformLabel = "$platformLabel • ROUND$shapeSuffix",
                    screenWidthDp = width,
                    screenHeightDp = height,
                    horizontalPadding = horizPad,
                    verticalPadding = 10.dp,
                    contentPadding = PaddingValues(horizontal = horizPad, vertical = vertPad),
                    mainSpriteSize = when {
                        width >= 300 -> 124.dp
                        width >= 250 -> 104.dp
                        else -> 92.dp
                    },
                    battleSpriteSize = when {
                        width >= 300 -> 72.dp
                        width >= 250 -> 62.dp
                        else -> 56.dp
                    },
                    headerFontSize = if (width >= 300) 12.sp else 11.sp,
                    buttonSize = if (width >= 300) 44.dp else 40.dp,
                    actionRowSpacing = if (width >= 300) 4.dp else 2.dp
                )
            } else {
                // Square & Rectangular watches (AOSP Full Android Watches, Oppo Watch, square form factors)
                WatchDisplayConfig(
                    isRound = false,
                    isWearOsSystem = isWearOs,
                    runtimePlatformLabel = "$platformLabel • RECT/SQ$shapeSuffix",
                    screenWidthDp = width,
                    screenHeightDp = height,
                    horizontalPadding = if (width >= 300) 10.dp else 6.dp,
                    verticalPadding = if (height >= 300) 10.dp else 6.dp,
                    contentPadding = PaddingValues(
                        horizontal = if (width >= 300) 10.dp else 6.dp,
                        vertical = if (height >= 300) 16.dp else 12.dp
                    ),
                    mainSpriteSize = when {
                        width >= 300 -> 132.dp
                        width >= 260 -> 118.dp
                        else -> 108.dp
                    },
                    battleSpriteSize = when {
                        width >= 300 -> 78.dp
                        width >= 260 -> 70.dp
                        else -> 64.dp
                    },
                    headerFontSize = if (width >= 300) 13.sp else 12.sp,
                    buttonSize = if (width >= 300) 48.dp else 44.dp,
                    actionRowSpacing = if (width >= 300) 6.dp else 4.dp
                )
            }
        }
    }
}
