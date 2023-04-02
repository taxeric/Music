package com.lanier.music.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lanier.music.R
import com.lanier.music.entity.LocalMusicState
import com.lanier.music.entity.LocalSongController
import com.lanier.music.entity.Song
import com.lanier.music.entity.SongController
import com.lanier.music.vm.MusicVM
import java.io.File

/**
 * Create by Eric
 * on 2023/4/1
 */
var innerPath = ""
val musicList = mutableStateListOf<Song>()

@Composable
fun WrapMainPage(
    songController: SongController,
    vm: MusicVM
) {
    val state by vm.state.collectAsState()
    CompositionLocalProvider(
        LocalSongController provides songController,
        LocalMusicState provides state
    ) {
        MainPage()
    }
}

@Composable
private fun MainPage() {
    val controller = LocalSongController.current
    LaunchedEffect(key1 = Unit) {
        val parentPath = File(innerPath)
        if (parentPath.isDirectory) {
            musicList.clear()
            parentPath.listFiles()?.let {
                it.forEach { f ->
                    val song = Song(f.absolutePath, f.name)
                    musicList.add(song)
                }
            }
            controller?.updateSongs(musicList)
        }
    }
    Column {
        Text(text = innerPath)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        CurPlay()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        ControllerView()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        LazyColumn {
            items(musicList.size) {
                MusicItem(song = musicList[it]) { result ->
                    controller?.play(result)
                }
            }
        }
    }

}

@Composable
private fun MusicItem(
    song: Song,
    onClick: (song: Song) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(song) }
    ) {
        Text(
            text = song.name,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(8.dp)
        )
    }
}

@Composable
private fun CurPlay() {
    val musicState = LocalMusicState.current
    Column() {
        Text(text = "cur >> ${musicState.curPlaySong.name}")
        Text(text = "size >> ${musicState.songs.size}")
    }
}

@Composable
private fun ControllerView() {
    val songController = LocalSongController.current
    val musicState = LocalMusicState.current
    val isPlaying = musicState.isPlaying
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                if (isPlaying) {
                    songController?.pause()
                } else {
                    songController?.resume()
                }
            }
        ) {
            Icon(
                painter = painterResource(
                    id = if (isPlaying) R.drawable.baseline_pause_24
                    else R.drawable.baseline_play_arrow_24
                ),
                contentDescription = ""
            )
        }
    }
}
