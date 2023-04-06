package com.lanier.music

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lanier.music.di.MusicEnvironment
import com.lanier.music.entity.*
import com.lanier.music.page.WrapMainPage
import com.lanier.music.page.innerPath
import com.lanier.music.service.MediaPlayerService
import com.lanier.music.ui.theme.MusicTheme
import com.lanier.music.utils.SongUtil
import com.lanier.music.vm.MusicVM
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), ServiceConnection {

    private lateinit var vm: MusicVM
    private var mediaPlayerService: MediaPlayerService? = null
    private val songController = object : SongController {
        override fun play(song: Song) {
            vm.dispatch(
                MusicAction.Play(song)
            )
        }

        override fun seekTo(duration: Long) {
            vm.dispatch(
                MusicAction.SeekTo(duration)
            )
        }

        override fun updateSongs(songs: List<Song>) {
            vm.dispatch(
                MusicAction.UpdateSongs(songs)
            )
        }

        override fun pause() {
            vm.dispatch(
                MusicAction.Pause
            )
        }

        override fun resume() {
            vm.dispatch(
                MusicAction.Resume
            )
        }

        override fun stop() {
            vm.dispatch(
                MusicAction.Stop
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MusicVM(
                    MusicEnvironment(this@MainActivity)
                ) as T
            }
        }

        vm = ViewModelProvider(this, factory = factory)[MusicVM::class.java]

        val serviceIntent = Intent(this, MediaPlayerService::class.java)
        bindService(serviceIntent, this, BIND_AUTO_CREATE)

        innerPath = getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.path?:""

        setContent {
            MusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WrapMainPage(
                        songController,
                        vm
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val list = SongUtil.getSong(this@MainActivity)
            println("list >> ${list.size}")
            if (list.isNotEmpty()) {
                println(list[0])
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MediaPlayerService.MediaPlayerBinder
        mediaPlayerService = binder.service
        mediaPlayerService!!.songController = songController
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mediaPlayerService = null
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicTheme {
        Greeting("Android")
    }
}