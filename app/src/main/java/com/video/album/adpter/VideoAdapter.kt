package com.video.album.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.video.album.databinding.RvVideoItemBinding
import com.video.album.interfaces.RecyclerInterface
import com.video.album.model.VideoData
import com.video.album.viewmodel.BaseViewModel

class VideoAdapter(
    private val viewModel: BaseViewModel,
    private val recyclerInterface: RecyclerInterface
) :
    RecyclerView.Adapter<VideoAdapter.VideoHolder>(), Player.EventListener {

    private var lastItemPosition = -1
    //region ----  Adapter related methods  ----
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        return VideoHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return viewModel.videoList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        if (position > lastItemPosition) {
            recyclerInterface.onScroll(position, lastItemPosition)
        }
        lastItemPosition = position
        viewModel.videoList.value?.get(position)?.let {
            holder.bind(viewModel, it, this)
        }
    }
    //endregion

    /**
     * Video View holder
     */
    class VideoHolder private constructor(private val binding: RvVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            viewModel: BaseViewModel,
            videoData: VideoData,
            eventListener: Player.EventListener
        ) {
            binding.videoPlayerSEP.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            binding.videoPlayerSEP.player =
                viewModel.preparePlayer(videoData = videoData, eventListener = eventListener)
            binding.executePendingBindings()
        }

        fun stopVideo() {
            if (binding.videoPlayerSEP.player?.isPlaying == true) {
                binding.videoPlayerSEP.player?.stop()
            }
        }

        companion object {
            fun from(parent: ViewGroup): VideoHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvVideoItemBinding.inflate(layoutInflater, parent, false)
                return VideoHolder(binding)
            }
        }
    }

    /**
     * Player State Change Listener methods to Update the live-data and update the RecyclerView
     */
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            val size = viewModel.videoList.value?.size ?: 0
            for (i in 0 until size) {
                viewModel.videoList.value?.get(i).let {
                    if (it?.isVideoPlayingNeeded == true) {
                        val position = if (i == (size - 1)) {
                            0
                        } else {
                            i + 1
                        }
                        viewModel.updateVideoPlayNeeded(position)
                        it.isVideoPlayingNeeded = false
                        notifyDataSetChanged()
                        recyclerInterface.scrollToNext(position, it.isVideoPlayingNeeded)
                        return
                    }
                }
            }
        }
    }
}