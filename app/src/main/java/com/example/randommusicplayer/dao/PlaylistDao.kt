package com.example.randommusicplayer.dao

import com.example.randommusicplayer.interfaces.PlaylistCallback
import com.example.randommusicplayer.interfaces.PlaylistDaoCallback
import com.example.randommusicplayer.services.PlaylistService
import com.example.randommusicplayer.singleton.PlaylistSingleton

class PlaylistDao private constructor (playlistQueue: PlaylistSingleton, token : String) {

    private val playlistService = PlaylistService.getInstance(playlistQueue,token)
    private var offset = 0

    companion object {
        @Volatile
        private var INSTANCE: PlaylistDao? = null
        fun getInstance(playlistQueue: PlaylistSingleton, token : String) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlaylistDao( playlistQueue ,token ).also {
                    INSTANCE = it
                }
                return PlaylistDao( playlistQueue,token)
            }
    }

    fun getPlaylist(callback: PlaylistDaoCallback){
        while(offset <= 950) {
            playlistService.getPlaylistAPI(object : PlaylistCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                }
            }, offset)
            offset += 50
        }
    }
}