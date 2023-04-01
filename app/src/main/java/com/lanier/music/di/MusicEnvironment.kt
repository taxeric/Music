package com.lanier.music.di

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lanier.music.entity.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Create by Eric
 * on 2023/4/1
 */
class MusicEnvironment constructor(
    context: Context
) {

    private val _isPlaying = MutableStateFlow(false)
    private val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _hasStopped = MutableStateFlow(false)
    private val hasStopped: StateFlow<Boolean> = _hasStopped

    private val _songs = MutableStateFlow(emptyList<Song>())
    private val songs: StateFlow<List<Song>> = _songs

    val exoPlayer = ExoPlayer
        .Builder(context)
        .build()
        .apply {
            addListener(
                object : Player.Listener{
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == Player.STATE_ENDED) {
                            println("the song has finished")
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        _isPlaying.tryEmit(isPlaying)
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)
                        println("play err >> $error")
                    }

                    override fun onPlayerErrorChanged(error: PlaybackException?) {
                        super.onPlayerErrorChanged(error)
                        println("play err >> $error")
                    }
                }
            )
        }

    fun updateSongs(songs: List<Song>) {
        _songs.tryEmit(songs)

    }

    fun play(song: Song) {
        val uri = song.path.toUri()
        println("play uri >> $uri")
        exoPlayer.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
        exoPlayer.play()
        _hasStopped.tryEmit(false)
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun resume() {
        if (!_hasStopped.value) {
            exoPlayer.play()
        }
    }

    fun stop() {
        if (!_hasStopped.value) {
            exoPlayer.stop()
            _hasStopped.tryEmit(true)
        }
    }

    fun getPlayingState() = _isPlaying
}