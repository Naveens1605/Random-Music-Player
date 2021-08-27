package com.example.randommusicplayer.utils

import android.content.Context
import android.content.Intent
import com.example.randommusicplayer.activity.PlayActivity

class StartPlayActivity constructor (private val context: Context){
    fun startPlatActivity(url: String) {
        val intent = Intent(context, PlayActivity::class.java)
        intent.putExtra("URL",url)
        context.startActivity(intent)
    }
}