package com.lanier.music.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lanier.music.di.MusicEnvironment
import com.lanier.music.entity.MusicAction
import com.lanier.music.entity.MusicState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Create by Eric
 * on 2023/4/1
 */
class MusicVM (private val environment: MusicEnvironment): ViewModel() {

    private val _state = MutableStateFlow(MusicState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            environment.getPlayingState().collect {
                _state.emit(_state.value.copy(isPlaying = it))
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