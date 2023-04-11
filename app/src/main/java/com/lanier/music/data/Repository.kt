package com.lanier.music.data

import com.lanier.music.entity.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Create by Eric
 * on 2023/4/9
 */
class Repository @Inject constructor(
    private val dataSource: LocalDataSource
) {

    fun getAllSongs(): Flow<List<Song>> = dataSource.getSongs()

    suspend fun insertSongs(vararg song: Song) = dataSource.insertSongs(*song)
}