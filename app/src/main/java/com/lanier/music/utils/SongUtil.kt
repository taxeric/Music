package com.lanier.music.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.lanier.music.entity.Song

/**
 * Create by Eric
 * on 2023/4/6
 */
object SongUtil {

    fun getSong(
        context: Context
    ): List<Song> {
        val songs = mutableListOf<Song>()
        val audioUriExternal = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val songProjection = listOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
        )
        val cursorIndexSongID: Int
        val cursorIndexSongTitle: Int
        val cursorIndexSongDuration: Int

        context.contentResolver.query(
            audioUriExternal,
            songProjection.toTypedArray(),
            null,
            null,
            null,
        )?.use { cursor ->
            cursorIndexSongID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            cursorIndexSongTitle = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            cursorIndexSongDuration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val audioId = cursor.getLong(cursorIndexSongID)
                val title = cursor.getString(cursorIndexSongTitle)
                val duration = cursor.getLong(cursorIndexSongDuration)

                val path = Uri.withAppendedPath(audioUriExternal, "" + audioId)

                val song = Song(
                    id = audioId,
                    path = path.toString(),
                    name = title,
                    duration = duration,
                )

                songs.add(song)
            }
        }

        return songs
    }
}