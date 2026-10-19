package com.digiwear.companion.render

import android.content.Context
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * High-performance, zero-latency memory cache for official Bandai Reference Book character portraits.
 * Caches decoded ImageBitmaps to eliminate duplicate disk/asset decompressions across DigiDex and Battle Hub.
 */
object PortraitAssetCache {
    private val memoryCache = LruCache<Int, ImageBitmap>(96)

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
