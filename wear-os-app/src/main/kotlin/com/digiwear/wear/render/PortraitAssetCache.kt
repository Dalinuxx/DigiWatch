package com.digiwear.wear.render

import android.content.Context
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * High-performance, zero-latency memory cache for official Bandai Reference Book character portraits.
 * Prevents redundant PNG decompressions from APK assets during fast battles, cut-ins, and scroll views.
 */
object PortraitAssetCache {
    private val memoryCache = LruCache<Int, ImageBitmap>(64)

    fun getPortrait(context: Context, speciesId: Int): ImageBitmap? {
        memoryCache[speciesId]?.let { return it }
        return try {
            context.assets.open("portraits/$speciesId.png").use { stream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()?.also { bitmap ->
                    memoryCache.put(speciesId, bitmap)
                }
            }
        } catch (_: Exception) {
            null
        }
    }
}
