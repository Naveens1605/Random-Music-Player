package com.example.randommusicplayer.services

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.randommusicplayer.interfaces.PlaylistCallback
import com.example.randommusicplayer.model.EndPoints
import com.example.randommusicplayer.singleton.PlaylistSingleton
import org.json.JSONException

class PlaylistService private constructor(private val playlistQueue: PlaylistSingleton, private val token : String){

    private val playlist : ArrayList<String>  = ArrayList()

    companion object {
        @Volatile
        private var INSTANCE: PlaylistService? = null
        fun getInstance(queue : PlaylistSingleton, token : String) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlaylistService(queue ,token ).also {
                    INSTANCE = it
                }
                return PlaylistService(queue,token)
            }
    }

    fun getPlaylistAPI(callback : PlaylistCallback , offset : Int){
        val url = String.format(EndPoints.SEARCH.toString(),offset)
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                val jsonObject = response.optJSONObject("playlists")
                jsonObject?.let {
                    val jsonArray = jsonObject.optJSONArray("items")
                    jsonArray?.let {
                        for (n in 0 until jsonArray.length()) {
                            try {
                                var musicObject = jsonArray.getJSONObject(n)
                                musicObject = musicObject.optJSONObject("tracks")
                                val href = musicObject.optString("href")
                                playlist.add(href)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                if(offset == 950) {
                    callback.onSuccess()
                }
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    val auth = "Bearer $token"
                    headers["Authorization"] = auth
                    return headers
                }
            }
        playlistQueue.addToRequestQueue(jsonObjectRequest)
    }

    fun getPlaylist(): ArrayList<String> {
        return playlist
    }
}