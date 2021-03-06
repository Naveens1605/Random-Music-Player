package com.example.randommusicplayer.model

enum class Credentials(private val credentials: String) {

    CLIENT_ID("YOUR CLIENT ID"),
    REDIRECT_URI("com.example.randommusicplayer://callback"),
    SCOPE("user-read-email,user-read-private,user-library-modify,user-library-read,user-follow-modify,user-follow-read,playlist-modify-public,playlist-modify-private,playlist-read-private,playlist-read-collaborative,user-read-recently-played,user-top-read,user-read-playback-position,user-read-recently-played,user-top-read,user-read-playback-position");

    override fun toString(): String {
        return credentials
    }
}
