package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.media3.common.MediaItem
import com.lofigroup.seeyau.features.chat.R
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.media_player.model.PlaybackState
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.ui.rememberMediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

val defaultMediaState = MediaPlayerState()

@Composable
fun AudioPlayerControls(
  audioContent: UIMessageType.Audio,
  id: Long
) {
  val state = rememberMediaPlayerState(id = id, duration = audioContent.duration)

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = LocalSpacing.current.medium, end = LocalSpacing.current.small),
    verticalAlignment = Alignment.CenterVertically
  ) {
    PlayerProgressBar(
      state = state,
      modifier = Modifier
        .weight(1f)
    )

    PlaybackControls(
      mediaItem = audioContent.mediaItem,
      state = state,
      id = id
    )
  }
}

@Composable
fun PlaybackControls(
  mediaItem: MediaItem,
  state: MediaPlayerState,
  id: Long,
  modifier: Modifier = Modifier,
  notifyInteractedWith: () -> Unit = {},
) {
  val mediaPlayer = LocalMediaPlayer.current
  val isPlaying = state.playbackState == PlaybackState.PLAYING

  IconButton(
    onClick = {
      notifyInteractedWith()
      if (isPlaying)
        mediaPlayer.pause()
      else
        mediaPlayer.playMedia(mediaItem, id)
    },
    modifier = modifier
  ) {
    Icon(
      painter = painterResource(id = if (isPlaying) R.drawable.ic_pause_rounded else R.drawable.ic_play_arrow_rounded),
      contentDescription = null,
      modifier = Modifier.size(LocalSize.current.medium)
    )
  }
}

@Composable
fun PlayerProgressBar(
  state: MediaPlayerState,
  modifier: Modifier = Modifier,
  notifyInteractedWith: () -> Unit = {},
) {
  val mediaPlayer = LocalMediaPlayer.current

  BoxWithConstraints(
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .align(Alignment.Center)
        .pointerInput(state.isCurrentItem) {
          detectTapGestures { offset: Offset ->
            notifyInteractedWith()
            if (state.isCurrentItem) {
              mediaPlayer.seekTo(offset.x / maxWidth.toPx())
            }
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
  }
}