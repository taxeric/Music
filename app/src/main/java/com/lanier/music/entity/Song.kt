package com.lanier.music.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by Eric
 * on 2023/4/1
 */
@Entity(tableName = "table_songs")
data class Song (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "song_name") val name: String,
    @ColumnInfo(name = "song_path") val path: String,
    @ColumnInfo(name = "song_duration") val duration: Long = 0L,
) {

    companion object {
        val default = Song(
            id = -1,
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