package com.example.randommusicplayer.utils

class PlaySong constructor (private val variables: Variables, private val startPlayActivity: StartPlayActivity) {

    fun playSong(songNumber : Int) {
        if (variables.songsURL.size > 0) {
            variables.songURL = variables.songsURL[songNumber]
            startPlayActivity.startPlatActivity(variables.songURL.spotify)
        }
    }
}