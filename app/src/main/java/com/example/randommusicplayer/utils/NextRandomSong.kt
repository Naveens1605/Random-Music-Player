package com.example.randommusicplayer.utils

import kotlin.random.Random

class NextRandomSong constructor (private val variables: Variables, private val setSongData: SetSongData){

    fun nextRandomSong(){
        variables.i = Random.nextInt(0,variables.songsURL.size)
        variables.currIndex = variables.i
        variables.prevSongs.add(variables.i)
        setSongData.setSongData(variables.i)
    }
}