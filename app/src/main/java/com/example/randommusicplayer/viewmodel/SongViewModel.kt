package com.example.randommusicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.randommusicplayer.interfaces.RepositoryCallback
import com.example.randommusicplayer.interfaces.ViewModelCallback
import com.example.randommusicplayer.model.Song
import com.example.randommusicplayer.model.SongImage
import com.example.randommusicplayer.model.SongURL
import com.example.randommusicplayer.repository.SongRepository
import com.example.randommusicplayer.singleton.MySingleton
import com.example.randommusicplayer.singleton.PlaylistSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var songs : ArrayList<Song>
    lateinit var songsURL : ArrayList<SongURL>
    lateinit var songsImage: ArrayList<SongImage>
    lateinit var singerNamesList : ArrayList<String>
    private val repository : SongRepository = SongRepository()

    fun getData(callback : ViewModelCallback, queue : MySingleton, playlistQueue: PlaylistSingleton, token : String){
        GlobalScope.launch (Dispatchers.IO){
            repository.getData(object : RepositoryCallback {
                override fun onSuccess() {
                    songs = repository.songs
                    songsURL = repository.songsURL
                    songsImage = repository.songsImage
                    singerNamesList = repository.singerNamesList
                    callback.onSuccess()
                }
            }, queue, playlistQueue, token)
        }
    }

    fun getPlaylist(callback : ViewModelCallback,  playlistQueue: PlaylistSingleton, token : String){
        GlobalScope.launch (Dispatchers.IO){
            repository.getPlaylist(object : RepositoryCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                }
            },playlistQueue, token)
        }
    }
}