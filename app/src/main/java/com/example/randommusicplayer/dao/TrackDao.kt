package com.example.randommusicplayer.dao

import com.example.randommusicplayer.interfaces.TrackDataCallback
import com.example.randommusicplayer.interfaces.VolleyCallback
import com.example.randommusicplayer.model.Song
import com.example.randommusicplayer.model.SongImage
import com.example.randommusicplayer.model.SongURL
import com.example.randommusicplayer.services.SongService
import com.example.randommusicplayer.singleton.MySingleton
import com.example.randommusicplayer.singleton.PlaylistSingleton

class TrackDao private constructor (queue : MySingleton, playlistSingleton: PlaylistSingleton , token : String){

    private var songs : ArrayList<Song> = ArrayList()
    private var songsURL : ArrayList<SongURL> = ArrayList()
    private var songsImage : ArrayList<SongImage> = ArrayList()
    private var singerNamesList : ArrayList<String> = ArrayList()
    private val songService : SongService = SongService.getInstance(queue,playlistSingleton,token)

    companion object {
        @Volatile
        private var INSTANCE: TrackDao? = null
        fun getInstance(queue : MySingleton, playlistSingleton: PlaylistSingleton ,token : String) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TrackDao(queue,playlistSingleton ,token ).also {
                    INSTANCE = it
                }
                return TrackDao(queue,playlistSingleton,token)
            }
    }

    fun getData(callback : TrackDataCallback) {
        if(songsURL.isNotEmpty()) songsURL.clear()
        if(songs.isNotEmpty()) songs.clear()
        if(songsImage.isNotEmpty()) songsImage.clear()
        if(singerNamesList.isNotEmpty()) singerNamesList.clear()
        songService.playedTracks(object : VolleyCallback {
            override fun onSuccess() {
                songsURL = songService.getSongURL()
                songs = songService.getSong()
                songsImage = songService.getSongImage()
                singerNamesList = songService.getSinger()
                callback.onSuccess()
            }
        })
    }

    fun getSongs() : ArrayList<Song> {
        return songs
    }

    fun getSongsURL() : ArrayList<SongURL>{
        return songsURL
    }

    fun getSongsImage() : ArrayList<SongImage>{
        return songsImage
    }

    fun getSinger() : ArrayList<String> {
        return singerNamesList
    }
}