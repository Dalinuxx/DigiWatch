package com.digiwear.wear.render

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * High-performance, memory-efficient cache for official Bandai Reference Book character portraits.
 * Capacity is tuned to 8 entries (~1.8 MB) to protect smartwatch heap, with ComponentCallbacks2
 * memory trimming on low-memory / background events.
 */
@Suppress("DEPRECATION")
object PortraitAssetCache : ComponentCallbacks2 {
    private val memoryCache = LruCache<Int, ImageBitmap>(8)
    private var isRegistered = false

    fun init(context: Context) {
        if (!isRegistered) {
            context.applicationContext.registerComponentCallbacks(this)
            isRegistered = true
        }
    }

    fun getPortrait(context: Context, speciesId: Int): ImageBitmap? {
        init(context)
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

    override fun onTrimMemory(level: Int) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            memoryCache.evictAll()
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            memoryCache.trimToSize(memoryCache.size() / 2)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {}

    @Deprecated("Deprecated in Java", ReplaceWith("onTrimMemory(level)"))
    override fun onLowMemory() {
        memoryCache.evictAll()
    }
}
