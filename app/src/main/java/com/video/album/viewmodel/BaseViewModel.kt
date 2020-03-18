package com.video.album.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.video.album.helper.VideoHelper
import com.video.album.model.VideoData

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val videoHelper = VideoHelper(application)

    //region ----  Preparation of the Video List  ----
    /**
     *  Declaring the live variable for Video List
     */
    private val _videoList =
        MutableLiveData<ArrayList<VideoData>>().apply { value = ArrayList() }
    val videoList: LiveData<ArrayList<VideoData>> = _videoList

    fun init() {
        getVideoList()
    }

    private fun getVideoList(){
        _videoList.value = videoHelper.getVideoListData()
    }
    //endregion

    /**
     * Preparing the video player
     */
    fun preparePlayer(videoData: VideoData, eventListener: Player.EventListener): SimpleExoPlayer {
        return videoHelper.prepareMediaSource(videoData = videoData, eventListener = eventListener)
    }

    fun showToast(msg: String) {
        videoHelper.showToast(msg)
    }

    /**
     * Update the video list according to the specific position
     */
    fun updateVideoPlayNeeded(position: Int) {
        videoList.value?.get(position).let { videoData ->
            videoData?.isVideoPlayingNeeded = true
        }
    }
}