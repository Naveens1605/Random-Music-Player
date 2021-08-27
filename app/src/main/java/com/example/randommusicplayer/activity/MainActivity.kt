package com.example.randommusicplayer.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.randommusicplayer.R
import com.example.randommusicplayer.interfaces.InternetConnection
import com.example.randommusicplayer.interfaces.ViewModelCallback
import com.example.randommusicplayer.internetconnection.InternetAvailability
import com.example.randommusicplayer.singleton.MySingleton
import com.example.randommusicplayer.singleton.PlaylistSingleton
import com.example.randommusicplayer.utils.*
import com.example.randommusicplayer.viewmodel.SongViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1337
    private var mVariables: Variables = Variables()
    private val auth = Authentication.getInstance(this@MainActivity)
    private var setSongData : SetSongData = SetSongData(this,mVariables)
    private var startPlayActivity : StartPlayActivity = StartPlayActivity(this)
    private var playSong : PlaySong = PlaySong(mVariables,startPlayActivity)
    private var nextRandomSong : NextRandomSong = NextRandomSong(mVariables,setSongData)
    private var internetAvailability : InternetAvailability = InternetAvailability.getInstance(this@MainActivity)
    private lateinit var viewModel : SongViewModel
    private lateinit var getData: GetData

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mVariables.mRefreshToast = Toast.makeText(this,"Playlist Refreshed",Toast.LENGTH_SHORT)
        mVariables.mNoSongsToast = Toast.makeText(this,"No Songs in Playlist",Toast.LENGTH_LONG)
        mVariables.mNoInternetToast = Toast.makeText(this,"No Internet Available Please Check Internet Connection",Toast.LENGTH_LONG)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(SongViewModel::class.java)

            internetAvailability.isInternetAvailable(object : InternetConnection {
            override fun onConnected(connected: Boolean) {

                if (connected) {

                    mVariables.playlistQueue = PlaylistSingleton.getInstance(applicationContext)
                    mVariables.queue = MySingleton.getInstance(applicationContext)

                    auth.userAuth(REQUEST_CODE)

                    mVariables.playBtn = findViewById(R.id.playBtn)
                    mVariables.nextBtn = findViewById(R.id.nextBtn)
                    mVariables.prevBtn = findViewById(R.id.prevBtn)
                    mVariables.refreshTrack = findViewById(R.id.refreshTrack)
                    mVariables.imageView = findViewById(R.id.imageView)
                    mVariables.songName = findViewById(R.id.songName)
                    mVariables.songPopularity = findViewById(R.id.songPopularity)
                    mVariables.splashScreen = findViewById(R.id.splashScreen)
                    mVariables.progressBar = findViewById(R.id.progress_circular)
                    mVariables.singerName = findViewById(R.id.singerName)

                    mVariables.playBtn.setOnClickListener {
                        playSong.playSong(mVariables.currIndex)
                    }

                    mVariables.nextBtn.setOnClickListener {
                        nextRandomSong.nextRandomSong()
                    }

                    mVariables.prevBtn.setOnClickListener {
                        if (mVariables.prevSongs.size > 1) {
                            mVariables.currIndex = mVariables.prevSongs[mVariables.prevSongs.size - 2]
                            setSongData.setSongData(mVariables.currIndex)
                            mVariables.prevSongs.removeAt(mVariables.prevSongs.size - 1)
                        }
                    }

                    mVariables.refreshTrack.setOnClickListener {
                        mVariables.prevSongs.clear()
                        getData.getData()
                    }
                }
                else{
                    mVariables.mNoInternetToast.show()
                    mVariables.calledFromMain = true
                }
            }
        })
    }

    override fun onResume() {
        internetAvailability.isInternetAvailable(object : InternetConnection{
            override fun onConnected(connected: Boolean) {
                if (connected && !mVariables.isAuth) {

                    mVariables.playlistQueue = PlaylistSingleton.getInstance(applicationContext)
                    mVariables.queue = MySingleton.getInstance(applicationContext)

                    auth.userAuth(REQUEST_CODE)

                    mVariables.playBtn = findViewById(R.id.playBtn)
                    mVariables.nextBtn = findViewById(R.id.nextBtn)
                    mVariables.prevBtn = findViewById(R.id.prevBtn)
                    mVariables.refreshTrack = findViewById(R.id.refreshTrack)
                    mVariables.imageView = findViewById(R.id.imageView)
                    mVariables.songName = findViewById(R.id.songName)
                    mVariables.songPopularity = findViewById(R.id.songPopularity)
                    mVariables.splashScreen = findViewById(R.id.splashScreen)
                    mVariables.progressBar = findViewById(R.id.progress_circular)
                    mVariables.singerName = findViewById(R.id.singerName)

                    mVariables.playBtn.setOnClickListener {
                        playSong.playSong(mVariables.currIndex)
                    }

                    mVariables.nextBtn.setOnClickListener {
                        nextRandomSong.nextRandomSong()
                    }

                    mVariables.prevBtn.setOnClickListener {
                        if (mVariables.prevSongs.size > 1) {
                            mVariables.currIndex =
                                mVariables.prevSongs[mVariables.prevSongs.size - 2]
                            setSongData.setSongData(mVariables.currIndex)
                            mVariables.prevSongs.removeAt(mVariables.prevSongs.size - 1)
                        }
                    }

                    mVariables.refreshTrack.setOnClickListener {
                        mVariables.prevSongs.clear()
                        getData.getData()
                    }
                }
                else if(!connected && !mVariables.calledFromMain){
                    mVariables.mNoInternetToast.show()
                }
                mVariables.calledFromMain = false
            }
        })
        super.onResume()
    }

    override fun onDestroy() {
        auth.destroyInstance()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    mVariables.token = response.accessToken
                    mVariables.progressBar.visibility = View.VISIBLE
                    viewModel.getPlaylist(object: ViewModelCallback{
                        override fun onSuccess() {
                            getData = GetData(mVariables,viewModel,setSongData)
                            getData.getData()
                        }
                    },mVariables.playlistQueue,mVariables.token)
                    mVariables.isAuth = true
                }
                AuthenticationResponse.Type.ERROR -> {
                    Toast.makeText(this,"Authentication Error ${response.error}",Toast.LENGTH_LONG).show()
                }
                else -> {
                }
            }
        }
    }
}