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
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

val defaultMediaState = MediaPlayerState()

@Composable
fun AudioPlayerControls(
  audioContent: UIMessageType.Audio,
  id: Int
) {
  val mediaPlayer = LocalMediaPlayer.current
  val playbackState by mediaPlayer.observePlaybackState()
    .collectAsState(initial = defaultMediaState)

  val isPlaying = playbackState.currentItemId == id

  val state = if (isPlaying) playbackState else defaultMediaState

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    PlaybackControls(
      onPlayButtonClick = { mediaPlayer.playMedia(mediaItem = audioContent.mediaItem, id = id) },
      onPauseButtonClick = { mediaPlayer.pause() },
      isPlaying = state.playbackState == PlaybackState.PLAYING
    )
    Box(
      modifier = Modifier.weight(1f)
    ) {
      var barSize by remember {
        mutableStateOf(Size.Zero)
      }
      Box(
        modifier = Modifier
          .align(Alignment.Center)
          .onSizeChanged { barSize = it.toSize() }
          .pointerInput(isPlaying) {
            detectTapGestures { offset: Offset ->
              if (isPlaying)
                mediaPlayer.seekTo(offset.x / barSize.width)
            }
          }
      ) {
        LinearProgressIndicator(
          progress = state.progressData.relativeProgress,
          color = MaterialTheme.colors.onBackground,
          backgroundColor = Color.LightGray,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = LocalSpacing.current.extraSmall)
        )
      }

      Text(
        text = "${state.progressData.progress} / ${audioContent.duration}",
        style = MaterialTheme.typography.caption,
        modifier = Modifier
          // TODO not adaptive
          .padding(top = 20.dp)
          .align(Alignment.CenterEnd)
      )
    }
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