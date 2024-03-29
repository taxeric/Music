package com.lanier.music.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lanier.music.di.MusicEnvironment
import com.lanier.music.entity.MusicAction
import com.lanier.music.entity.MusicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Create by Eric
 * on 2023/4/1
 */
@HiltViewModel
class MusicVM @Inject constructor(
    private val environment: MusicEnvironment,
): ViewModel() {

    private val _state = MutableStateFlow(MusicState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            environment.getPlayingState().collect {
                _state.emit(_state.value.copy(isPlaying = it))
            }
        }
        viewModelScope.launch {
            environment.getDuration().collect {
                _state.emit(_state.value.copy(curDuration = it))
            }
        }
        viewModelScope.launch {
            environment.getSongs().collect {
                _state.emit(_state.value.copy(songs = it))
            }
        }
        viewModelScope.launch {
            environment.getCurPlaySong().collect {
                _state.emit(_state.value.copy(curPlaySong = it))
            }
        }
    }

    fun dispatch(action: MusicAction) {
        when (action) {
            MusicAction.Pause -> {
                viewModelScope.launch {
                    environment.pause()
                }
            }
            is MusicAction.SeekTo -> {
                viewModelScope.launch {
                    environment.seekTo(action.duration)
                }
            }
            is MusicAction.Play -> {
                viewModelScope.launch {
                    environment.play(action.song)
                }
            }
            is MusicAction.UpdateSongs -> {
                viewModelScope.launch {
                    environment.updateSongs(action.songs)
                }
            }
            MusicAction.Resume -> {
                viewModelScope.launch {
                    environment.resume()
                }
            }
            MusicAction.Stop -> {
                viewModelScope.launch {
                    environment.stop()
                }
            }
        }
    }
}