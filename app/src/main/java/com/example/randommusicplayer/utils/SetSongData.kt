package com.example.randommusicplayer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.randommusicplayer.glide.GlideApp

class SetSongData constructor (private val context: Context, private val variables: Variables) {

    @SuppressLint("SetTextI18n")
    fun setSongData(number : Int) {
        variables.progressBar.visibility = View.VISIBLE
        variables.song = variables.songs[number]
        variables.songName.text = variables.song.name
        variables.singerName.text = variables.singerNamesList[number]
        variables.songPopularity.text = "Popularity : ${variables.song.popularity}"
        variables.songImage = variables.songsImage[number]
        GlideApp.with(context).load(variables.songImage.url).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                variables.progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                variables.progressBar.visibility = View.GONE
                return false
            }

        }).into(variables.imageView)
    }
}