package com.video.album.helper

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.video.album.R
import com.video.album.model.VideoData
import com.video.album.utils.CacheDataSourceFactory

class VideoHelper(application: Application) {

    private val context: Context

    init {
        context = application
    }

    /**
     * Get all the URLs from the strings arrays resource
     */
    private fun getUrlList(): Array<String> {
        return context.resources.getStringArray(R.array.urlList)
    }

    fun prepareMediaSource(
        videoData: VideoData,
        eventListener: Player.EventListener
    ): SimpleExoPlayer {
        val trackSelector =
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
        val player: SimpleExoPlayer =
            ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        val dataSourceFactory = CacheDataSourceFactory(
            context = context,
            maxFileSize = 5 * 1024 * 1024
        )
        val mediaSource =
            ExtractorMediaSource(
                Uri.parse(videoData.url),
                dataSourceFactory,
                DefaultExtractorsFactory(),
                null,
                null
            )
        player.addListener(eventListener)
        player.prepare(mediaSource)
        player.apply {
            volume = 0f
            repeatMode = Player.REPEAT_MODE_OFF
            playWhenReady = videoData.isVideoPlayingNeeded
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        }
        return player
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getVideoListData(): ArrayList<VideoData>? {
        val videoList = ArrayList<VideoData>()
        val urls = getUrlList()
        for (i in urls.indices) {
            videoList.add(
                VideoData(
                    url = urls[i],
                    isVideoPlayingNeeded = i == 0
                )
            )
        }
        return videoList
    }

}