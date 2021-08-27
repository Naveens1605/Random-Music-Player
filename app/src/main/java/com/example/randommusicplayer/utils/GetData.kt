package com.example.randommusicplayer.utils

import android.view.View
import com.example.randommusicplayer.interfaces.ViewModelCallback
import com.example.randommusicplayer.viewmodel.SongViewModel

class GetData constructor( private val variables: Variables,
                           private val viewModel: SongViewModel,
                           private val setSongData: SetSongData) {

    private var buttonVisibility : ButtonVisibility = ButtonVisibility(variables)

    fun getData(){
        variables.progressBar.visibility = View.VISIBLE
        viewModel.getData(object : ViewModelCallback {
            override fun onSuccess() {
                if(viewModel.songs.isNotEmpty()) {
                    variables.songs = viewModel.songs
                    variables.songsURL = viewModel.songsURL
                    variables.songsImage = viewModel.songsImage
                    variables.singerNamesList = viewModel.singerNamesList
                    setSongData.setSongData(0)
                    variables.prevSongs.add(0)
                    buttonVisibility.buttonVisibility()
                    variables.mRefreshToast.show()
                }
                else {
                    variables.progressBar.visibility = View.GONE
                    variables.mNoSongsToast.show()
                }
            }
        }, variables.queue, variables.playlistQueue , variables.token)
    }
}