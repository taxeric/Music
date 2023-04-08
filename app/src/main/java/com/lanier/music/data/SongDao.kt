package com.lanier.music.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lanier.music.entity.Song
import kotlinx.coroutines.flow.Flow

/**
 * Create by Eric
 * on 2023/4/8
 */
@Dao
interface SongDao {

    @Query("select * from table_songs")
    fun getAllSongs(): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg song: Song)
}