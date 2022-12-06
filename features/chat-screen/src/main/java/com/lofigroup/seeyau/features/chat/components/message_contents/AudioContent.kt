package com.lofigroup.seeyau.features.chat.components.message_contents

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.components.defaultMediaState
import com.lofigroup.seeyau.features.chat.media_player.model.PlaybackState
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing


@Composable
fun AudioContent(
  audioContent: UIMessageType.Audio,
  pos: Int,
  isCurrentItem: Boolean
) {
  val mediaPlayer = LocalMediaPlayer.current
  val playbackState by mediaPlayer.observePlaybackState().collectAsState(initial = defaultMediaState)
  val state = if (isCurrentItem) playbackState else defaultMediaState

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    PlaybackControls(
      onPlayButtonClick = { mediaPlayer.playMedia(audioContent.mediaItem, pos) },
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
          .pointerInput(isCurrentItem) {
            detectTapGestures { offset: Offset ->
              if (isCurrentItem)
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

@Preview
@Composable
fun AudioMessagePreview() {
  AppTheme {
    Surface {
      ChatMessageItem(chatMessage = getPreviewPrivateMessage(
        type = MessageType.Audio(uri = "", duration = 0L),
      ))
    }
  }
}