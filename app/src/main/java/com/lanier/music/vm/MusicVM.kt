package com.lanier.music.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lanier.music.di.MusicEnvironment
import com.lanier.music.entity.MusicAction
import com.lanier.music.entity.MusicState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Create by Eric
 * on 2023/4/1
 */
class MusicVM (private val environment: MusicEnvironment): ViewModel() {

    private val _state = MutableStateFlow(MusicState())
    val state = _state.asStateFlow()

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
                    _state.emit(_state.value.copy(curPlaySong = action.song))
                }
            }
            is MusicAction.UpdateSongs -> {
                viewModelScope.launch {
                    environment.updateSongs(action.songs)
                    _state.emit(_state.value.copy(songs = action.songs))
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