package com.example.randommusicplayer.services

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.randommusicplayer.interfaces.VolleyCallback
import com.example.randommusicplayer.model.Song
import com.example.randommusicplayer.model.SongImage
import com.example.randommusicplayer.model.SongSinger
import com.example.randommusicplayer.model.SongURL
import com.example.randommusicplayer.singleton.MySingleton
import com.example.randommusicplayer.singleton.PlaylistSingleton
import com.google.gson.Gson
import org.json.JSONException
import kotlin.random.Random

class SongService private constructor (private val queue: MySingleton,
                                       playlistQueue: PlaylistSingleton,
                                       private val token: String) {

    private val songsURL: ArrayList<SongURL> = ArrayList()
    private val songs : ArrayList<Song> = ArrayList()
    private val songsImage : ArrayList<SongImage> = ArrayList()
    private val singerNamesList : ArrayList<String> = ArrayList()
    private val playlistService = PlaylistService.getInstance(playlistQueue,token)
    private var playlists : ArrayList<String> = playlistService.getPlaylist()

    companion object {
        @Volatile
        private var INSTANCE: SongService? = null
        fun getInstance(queue : MySingleton,playlistQueue: PlaylistSingleton,token : String) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SongService(queue,playlistQueue,token ).also {
                    INSTANCE = it
                }
                return SongService(queue,playlistQueue,token)
            }
    }

    fun playedTracks(callback: VolleyCallback) {
        val currentPlaylist = Random.nextInt(0, playlists.size)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, playlists[currentPlaylist], null,
            Response.Listener {
                val gson = Gson()
                val jsonArray = it.optJSONArray("items")
                if (jsonArray?.length()!! > 0) {
                    for (n in 0 until jsonArray.length()) {
                        try {
                            var getObject = jsonArray.getJSONObject(n)
                            getObject = getObject.optJSONObject("track")

                            val album = getObject.optJSONObject("album")
                            album?.let {
                                val images = album.getJSONArray("images")
                                val imagesObject = images.optJSONObject(0)
                                if(images.length() > 0) {
                                    val imageURL = gson.fromJson(
                                        imagesObject.toString(),
                                        SongImage::class.java
                                    )
                                    songsImage.add(imageURL)
                                }
                            }

                            val stringBuilder = StringBuilder()
                            val singer = getObject.optJSONArray("artists")
                            singer?.let {
                                for (name in 0 until singer.length()) {
                                    val nameObject = singer.getJSONObject(name)
                                    val singerName = gson.fromJson(
                                        nameObject.toString(),
                                        SongSinger::class.java
                                    )
                                    stringBuilder.append(singerName.name).append(", ")
                                }
                                singerNamesList.add(
                                    stringBuilder.deleteAt(stringBuilder.length - 2)
                                        .toString()
                                )
                                stringBuilder.delete(0, stringBuilder.length - 1)
                            }

                            val song =
                                gson.fromJson(getObject.toString(), Song::class.java)
                            songs.add(song)

                            getObject = getObject.optJSONObject("external_urls")
                            val songURL =
                                gson.fromJson(getObject.toString(), SongURL::class.java)
                            songsURL.add(songURL)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
                callback.onSuccess()
            },
            Response.ErrorListener {
                Log.d("ERROR", it.printStackTrace().toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                return headers
            }
        }
        queue.addToRequestQueue(jsonObjectRequest)
    }

    fun getSongURL(): ArrayList<SongURL> {
        return songsURL
    }

    fun getSong(): ArrayList<Song> {
        return songs
    }

    fun getSongImage(): ArrayList<SongImage> {
        return songsImage
    }

    fun getSinger(): ArrayList<String> {
        return singerNamesList
    }
}