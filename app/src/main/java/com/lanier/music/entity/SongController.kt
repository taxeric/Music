package com.lanier.music.entity

import androidx.compose.runtime.compositionLocalOf

/**
 * Create by Eric
 * on 2023/4/1
 */
interface SongController {

    fun play(song: Song)
    fun updateSongs(songs: List<Song>)
    fun pause()
    fun resume()
    fun stop()
}

val LocalSongController = compositionLocalOf<SongController?> { null }