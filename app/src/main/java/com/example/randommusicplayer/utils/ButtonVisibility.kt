package com.example.randommusicplayer.utils

import android.view.View

class ButtonVisibility constructor (private val variables: Variables) {

    fun buttonVisibility(){
        variables.playBtn.visibility = View.VISIBLE
        variables.nextBtn.visibility = View.VISIBLE
        variables.prevBtn.visibility = View.VISIBLE
        variables.songName.visibility = View.VISIBLE
        variables.songName.isSelected = true
        variables.singerName.visibility = View.VISIBLE
        variables.singerName.isSelected = true
        variables.songPopularity.visibility = View.VISIBLE
        variables.refreshTrack.visibility = View.VISIBLE
        variables.splashScreen.visibility = View.GONE
    }
}