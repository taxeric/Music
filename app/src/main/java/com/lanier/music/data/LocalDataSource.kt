package com.lanier.music.data

import com.lanier.music.entity.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Create by Eric
 * on 2023/4/9
 */
class LocalDataSource @Inject constructor(
    private val dao: SongDao,
) {

    fun getSongs(): Flow<List<Song>> {
        return dao.getAllSongs()
    }

    suspend fun insertSongs(vararg song: Song) {
        dao.insert(*song)
    }
}