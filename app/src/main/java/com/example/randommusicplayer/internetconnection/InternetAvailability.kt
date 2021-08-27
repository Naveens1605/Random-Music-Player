package com.example.randommusicplayer.internetconnection

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.randommusicplayer.interfaces.InternetConnection

class InternetAvailability private constructor (private val context: Context){

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: InternetAvailability? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: InternetAvailability(context).also {
                    INSTANCE = it
                }
                return InternetAvailability(context)
            }
    }

    fun isInternetAvailable(connected : InternetConnection) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        connected.onConnected(isConnected)
    }
}