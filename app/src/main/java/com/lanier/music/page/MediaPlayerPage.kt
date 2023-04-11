package com.lanier.music.page

import android.graphics.Rect
import android.text.TextPaint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lanier.music.R
import com.lanier.music.entity.LocalMusicState
import com.lanier.music.entity.LocalSongController
import com.lanier.music.entity.Song
import com.lanier.music.entity.SongController
import com.lanier.music.vm.MusicVM
import java.io.File
import kotlin.math.roundToLong

/**
 * Create by Eric
 * on 2023/4/1
 */
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
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
        }
    }
}

@Composable
private fun CurrentPlay() {
    val musicState = LocalMusicState.current
    val curSong = musicState.curPlaySong
    curSong.duration
    val duration = musicState.curDuration
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Text(
            text = curSong.name,
        )
        ProgressView()
    }
}

@Composable
private fun MusicItem(
    song: Song,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
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
private fun ControllerView() {
    val songController = LocalSongController.current
    val musicState = LocalMusicState.current
    val isPlaying = musicState.isPlaying
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        /*SongProgress(
            maxDuration = musicState.curPlaySong.duration,
            curDuration = musicState.curDuration,
            onValueChange = {
                songController?.seekTo(it.roundToLong())
            }
        )*/
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
}

@Composable
private fun ProgressView(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    progressMax: Float = 100f,
    progressBarStartColor: Color = Color.White,
    progressBarEndColor: Color = Color.Black,
    progressBarWidth: Dp = 7.dp,
    backgroundProgressBarColor: Color = Color.Gray,
    backgroundProgressBarWidth: Dp = 3.dp,
    roundBorder: Boolean = false,
    startAngle: Float = 0f
) {
    val target = (progress / progressMax) * 360f
    val anim = remember {
        Animatable(target)
    }
    val tvPaint = TextPaint().apply {
        color = android.graphics.Color.BLACK
        textSize = 32f
    }
    LaunchedEffect(key1 = progress) {
        anim.animateTo(target, tween(easing = LinearOutSlowInEasing))
    }
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2 - maxOf(backgroundProgressBarWidth, progressBarWidth).toPx() / 2
        drawCircle(
            color = backgroundProgressBarColor,
            radius = radius,
            center = size.center,
            style = Stroke(width = backgroundProgressBarWidth.toPx())
        )
        drawIntoCanvas { canvas ->
            val rect = Rect()
            val str = "${progress.toInt()}/${progressMax.toInt()}"
            tvPaint.getTextBounds(str, 0, str.length - 1, rect)
            canvas.nativeCanvas.drawText("${progress.toInt()}/${progressMax.toInt()}", size.width / 2f - rect.width() / 2f, size.height / 2f + rect.height() / 2f, tvPaint)
        }
        drawArc(
            brush = Brush.linearGradient(listOf(progressBarStartColor, progressBarEndColor)),
            startAngle = 270f + startAngle,
            sweepAngle = anim.value,
            useCenter = false,
            topLeft = size.center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = progressBarWidth.toPx(),
                cap = if (roundBorder) StrokeCap.Round else StrokeCap.Butt
            )
        )
    }
}
