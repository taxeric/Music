package com.lanier.music.di

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lanier.music.data.Repository
import com.lanier.music.entity.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Create by Eric
 * on 2023/4/1
 */
@OptIn(DelicateCoroutinesApi::class)
class MusicEnvironment @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) {

    private val _isPlaying = MutableStateFlow(false)
    private val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _duration = MutableStateFlow(0L)
    private val duration: StateFlow<Long> =_duration

    private val _hasStopped = MutableStateFlow(false)
    private val hasStopped: StateFlow<Boolean> = _hasStopped

    private val _songs = MutableStateFlow(emptyList<Song>())
    private val songs: StateFlow<List<Song>> = _songs

    private val _curPlay = MutableStateFlow(Song.default)
    private val curPlay: StateFlow<Song> = _curPlay

    init {
        GlobalScope.launch(Dispatchers.IO) {
            repository
                .getAllSongs()
                .distinctUntilChanged()
                .collect {
                    println("environment >> ${it.size}")
                    _songs.tryEmit(it)
                }
        }
    }

    private val exoPlayer = ExoPlayer
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
//        _songs.tryEmit(songs)
    }

    fun play(song: Song) {
//        val uri = song.path.toUri()
        exoPlayer.setMediaItem(MediaItem.fromUri(song.path))
        exoPlayer.prepare()
        exoPlayer.play()
        _curPlay.tryEmit(song)
        _hasStopped.tryEmit(false)
        val duration = if (exoPlayer.duration != -1L) {
            exoPlayer.currentPosition
        } else {
            0L
        }
        _duration.tryEmit(duration)
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

    fun seekTo(duration: Long) {
        exoPlayer.seekTo(duration)
    }

    fun getPlayingState() = _isPlaying

    fun getDuration() = _duration

    fun getSongs() = _songs

    fun getCurPlaySong() = _curPlay
}