package com.example.randommusicplayer.repository

import com.example.randommusicplayer.dao.PlaylistDao
import com.example.randommusicplayer.interfaces.RepositoryCallback
import com.example.randommusicplayer.interfaces.TrackDataCallback
import com.example.randommusicplayer.model.Song
import com.example.randommusicplayer.model.SongImage
import com.example.randommusicplayer.model.SongURL
import com.example.randommusicplayer.singleton.MySingleton
import com.example.randommusicplayer.singleton.PlaylistSingleton
import com.example.randommusicplayer.dao.TrackDao
import com.example.randommusicplayer.interfaces.PlaylistDaoCallback

class SongRepository {

    lateinit var songs : ArrayList<Song>
    lateinit var songsURL : ArrayList<SongURL>
    lateinit var songsImage: ArrayList<SongImage>
    lateinit var singerNamesList : ArrayList<String>

    fun getData(callback : RepositoryCallback, queue : MySingleton, playlistQueue: PlaylistSingleton, token : String){
        val getTrackData  = TrackDao.getInstance(queue, playlistQueue,token)
        getTrackData.getData(object : TrackDataCallback{
            override fun onSuccess() {
                songs = getTrackData.getSongs()
                songsURL = getTrackData.getSongsURL()
                songsImage = getTrackData.getSongsImage()
                singerNamesList = getTrackData.getSinger()
                callback.onSuccess()
            }
        })
    }

    fun getPlaylist(callback : RepositoryCallback, playlistQueue: PlaylistSingleton, token : String){
        val getPlaylist = PlaylistDao.getInstance(playlistQueue,token)
        getPlaylist.getPlaylist(object : PlaylistDaoCallback{
            override fun onSuccess() {
                callback.onSuccess()
            }
        })
    }
}