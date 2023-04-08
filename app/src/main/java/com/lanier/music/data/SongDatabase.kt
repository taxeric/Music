package com.lanier.music.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lanier.music.entity.Song

/**
 * Create by Eric
 * on 2023/4/8
 */
@Database(
    entities = [
        Song::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase: RoomDatabase() {

    abstract fun getDao(): SongDao

    companion object {

        private const val DATABASE_NAME = "local_database.db"

        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(context, SongDatabase::class.java, DATABASE_NAME)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}