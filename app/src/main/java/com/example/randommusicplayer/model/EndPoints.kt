package com.example.randommusicplayer.model

enum class EndPoints(private val endpoint: String) {
    SEARCH("https://api.spotify.com/v1/search?query=Bollywood&type=playlist&market=IN&offset=%d&limit=50"),
    RECENTLY_PLAYED("https://api.spotify.com/v1/me/player/recently-played"),
    TRACKS("https://api.spotify.com/v1/me/tracks"),
    PLAYLIST("https://api.spotify.com/v1/playlists/%s/tracks"),
    PLAYLISTME("https://api.spotify.com/v1/me/playlists"),
    FEATUREDPLAYLISTS("https://api.spotify.com/v1/browse/featured-playlists"),
    USER("https://api.spotify.com/v1/me");

    override fun toString(): String {
        return endpoint
    }
}