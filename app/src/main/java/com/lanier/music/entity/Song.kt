package com.lanier.music.entity

/**
 * Create by Eric
 * on 2023/4/1
 */
data class Song (
    val path: String,
    val name: String,
    val duration: Long = 0L
) {

    companion object {
        val default = Song(
            path = "",
            name = "Unknown"
        )
    }
}

enum class SongAction {
    PAUSE,
    RESUME,
    STOP,

    NOTHING
}