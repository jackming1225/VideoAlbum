package com.video.album.interfaces

interface RecyclerInterface {

    fun scrollToNext(position: Int, isScrollNeeded: Boolean)
    fun onScroll(newPosition: Int, oldPosition: Int)
}