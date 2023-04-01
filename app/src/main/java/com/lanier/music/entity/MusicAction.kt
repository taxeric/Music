package com.lanier.music.entity

/**
 * Create by Eric
 * on 2023/4/1
 */
sealed interface MusicAction {

    data class Play(val song: Song) : MusicAction
    data class UpdateSongs(val songs: List<Song>): MusicAction
    object Pause: MusicAction
    object Resume: MusicAction
    object Stop: MusicAction
}