package com.lanier.music.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.lanier.music.entity.SongAction
import com.lanier.music.entity.SongController

/**
 * Create by Eric
 * on 2023/4/1
 */
class MediaPlayerService: Service() {

    var songController: SongController? = null
    private val binder: MediaPlayerBinder = MediaPlayerBinder()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (SongAction.values()[intent?.action?.toInt() ?: SongAction.NOTHING.ordinal]) {
            SongAction.PAUSE -> songController?.pause()
            SongAction.RESUME -> songController?.resume()
            SongAction.STOP -> songController?.stop()
            SongAction.NOTHING -> {}
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class MediaPlayerBinder: Binder() {

        val service = this@MediaPlayerService
    }
}