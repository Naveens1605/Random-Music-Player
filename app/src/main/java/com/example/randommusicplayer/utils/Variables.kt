package com.example.randommusicplayer.utils

import android.widget.*
import com.example.randommusicplayer.model.Song
import com.example.randommusicplayer.model.SongImage
import com.example.randommusicplayer.model.SongURL
import com.example.randommusicplayer.singleton.MySingleton
import com.example.randommusicplayer.singleton.PlaylistSingleton
import java.util.*
import kotlin.collections.ArrayList

class Variables {
    lateinit var queue: MySingleton
    lateinit var playlistQueue : PlaylistSingleton
    lateinit var songURL: SongURL
    lateinit var song: Song
    lateinit var songImage: SongImage
    lateinit var playBtn: Button
    lateinit var nextBtn: Button
    lateinit var prevBtn: Button
    lateinit var refreshTrack: Button
    lateinit var imageView: ImageView
    lateinit var songName: TextView
    lateinit var songPopularity: TextView
    lateinit var singerName : TextView
    lateinit var splashScreen: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var token: String
    lateinit var mRefreshToast : Toast
    lateinit var mNoSongsToast: Toast
    lateinit var mNoInternetToast : Toast
    var songsURL: ArrayList<SongURL> = ArrayList()
    var songs: ArrayList<Song> = ArrayList()
    var songsImage: ArrayList<SongImage> = ArrayList()
    var singerNamesList : ArrayList<String> = ArrayList()
    var prevSongs: LinkedList<Int> = LinkedList()
    var calledFromMain = false
    var isAuth: Boolean = false
    var i: Int = 0
    var currIndex: Int = 0
}