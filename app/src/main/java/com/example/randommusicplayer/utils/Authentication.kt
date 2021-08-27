package com.example.randommusicplayer.utils

import android.annotation.SuppressLint
import android.app.Activity
import com.example.randommusicplayer.model.Credentials
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class Authentication private constructor (private val context: Activity) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: Authentication? = null
        fun getInstance(context: Activity) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Authentication(context).also {
                    INSTANCE = it
                }
                return Authentication(context)
            }
    }

    fun destroyInstance() {
        INSTANCE = null
    }

     fun userAuth(REQUEST_CODE : Int){
        val builder = AuthenticationRequest.Builder(
            Credentials.CLIENT_ID.toString(),
            AuthenticationResponse.Type.TOKEN,
            Credentials.REDIRECT_URI.toString()
        )

        builder.setScopes(arrayOf(Credentials.SCOPE.toString()))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(context, REQUEST_CODE, request)
    }
}