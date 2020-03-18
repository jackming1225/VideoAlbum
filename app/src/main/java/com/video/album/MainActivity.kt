package com.video.album

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.video.album.adpter.VideoAdapter
import com.video.album.interfaces.RecyclerInterface
import com.video.album.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerInterface {

    private lateinit var baseViewModel: BaseViewModel

    //region ----  Activity related methods  ----
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel(application)::class.java)
        baseViewModel.init()
        initUI()
    }
    //endregion

    private fun initUI() {
        setupListAdapter()
    }

    /**
     * Setting up the video adapter
     */
    private fun setupListAdapter() {
        baseViewModel.let {
            rvVideo.adapter = VideoAdapter(it, this)
        }
    }

    //region ----  Handling Scrolls of recyclerview  ----
    /**
     * Scrolling to next video if the current on ends playing
     */
    override fun scrollToNext(position: Int, isScrollNeeded: Boolean) {
        rvVideo.layoutManager?.smoothScrollToPosition(rvVideo, RecyclerView.State(), position)
    }

    override fun onScroll(newPosition: Int, oldPosition: Int) {
        if (oldPosition > -1) {
            baseViewModel.videoList.value?.get(oldPosition).apply {
                this?.isVideoPlayingNeeded = false
            }

            baseViewModel.videoList.value?.get(newPosition).apply {
                this?.isVideoPlayingNeeded = true
            }
            (rvVideo.findViewHolderForLayoutPosition(oldPosition) as VideoAdapter.VideoHolder).stopVideo()
        }
    }
    //endregion
}
