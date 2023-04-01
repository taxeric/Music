package com.lanier.music.entity

import androidx.compose.runtime.compositionLocalOf

/**
 * Create by Eric
 * on 2023/4/1
 */
data class MusicState(
    val songs: List<Song> = emptyList(),
    val isPlaying: Boolean = false,
    val curPlaySong: Song = Song.default
)

val LocalMusicState = compositionLocalOf { MusicState() }
