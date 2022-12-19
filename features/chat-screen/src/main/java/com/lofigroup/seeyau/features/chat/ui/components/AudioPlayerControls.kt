package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.media3.common.MediaItem
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.media_player.model.PlaybackState
import com.lofigroup.seeyau.features.chat.media_player.model.ProgressData
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

val defaultMediaState = MediaPlayerState()

@Composable
fun AudioPlayerControls(
  audioContent: UIMessageType.Audio,
  id: Int
) {
  val mediaPlayer = LocalMediaPlayer.current

  var state by remember {
    mutableStateOf(defaultMediaState)
  }

  LaunchedEffect(Unit) {
    mediaPlayer.registerState(id = id, duration = audioContent.duration).collect() { state = it }
  }

  DisposableEffect(key1 = Unit) {
    onDispose { mediaPlayer.unregisterState(id) }
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    PlaybackControls(
      onPlayButtonClick = { mediaPlayer.playMedia(mediaItem = audioContent.mediaItem, id = id) },
      onPauseButtonClick = { mediaPlayer.pause() },
      isPlaying = state.playbackState == PlaybackState.PLAYING
    )

    PlayerProgressBar(
      isPlaying = state.isCurrentItem,
      progressData = state.progressData,
      onSeekTo = { mediaPlayer.seekTo(it) }
    )
  }
}

@Composable
fun PlaybackControls(
  onPlayButtonClick: () -> Unit,
  onPauseButtonClick: () -> Unit,
  isPlaying: Boolean
) {
  IconButton(onClick = { if (isPlaying) onPauseButtonClick() else onPlayButtonClick() }) {
    Icon(
      imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
      contentDescription = null,
      modifier = Modifier.size(LocalSize.current.medium)
    )
  }
}

@Composable
fun RowScope.PlayerProgressBar(
  isPlaying: Boolean,
  progressData: ProgressData,
  onSeekTo: (Float) -> Unit
) {
  BoxWithConstraints(
    modifier = Modifier.weight(1f)
  ) {
    Box(
      modifier = Modifier
        .align(Alignment.Center)
        .pointerInput(isPlaying) {
          detectTapGestures { offset: Offset ->
            if (isPlaying) {
              onSeekTo(offset.x / maxWidth.toPx())
            }
          }
        }
    ) {
      LinearProgressIndicator(
        progress = progressData.relativeProgress,
        color = MaterialTheme.colors.onBackground,
        backgroundColor = Color.LightGray,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = LocalSpacing.current.extraSmall)
      )
    }

    Text(
      text = "${progressData.progress} / ${progressData.duration}",
      style = MaterialTheme.typography.caption,
      modifier = Modifier
        // TODO not adaptive
        .padding(top = 20.dp)
        .align(Alignment.CenterEnd)
    )
  }
}