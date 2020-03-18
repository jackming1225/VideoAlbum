package com.video.album.utils

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

object VideoCache {
    private var sDownloadCache: SimpleCache? = null
    private const val maxCacheSize: Long = 100 * 1024 * 1024

    fun getInstance(context: Context): SimpleCache {
        val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        if (sDownloadCache == null) sDownloadCache =
            SimpleCache(File(context.cacheDir, "video-album"), leastRecentlyUsedCacheEvictor)
        return sDownloadCache as SimpleCache
    }
}