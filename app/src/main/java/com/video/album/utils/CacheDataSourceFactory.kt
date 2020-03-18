package com.video.album.utils

import android.content.Context
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.video.album.R

class CacheDataSourceFactory(
    private val context: Context,
    private val maxFileSize: Long
) : DataSource.Factory {

    private val defaultDataSourceFactory: DefaultDataSourceFactory
    private val simpleCache: SimpleCache by lazy {
        VideoCache.getInstance(context)
    }

    init {
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDataSourceFactory = DefaultDataSourceFactory(
            context,
            bandwidthMeter,
            DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
        );
    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
            simpleCache, defaultDataSourceFactory.createDataSource(),
            FileDataSource(), CacheDataSink(simpleCache, maxFileSize),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}