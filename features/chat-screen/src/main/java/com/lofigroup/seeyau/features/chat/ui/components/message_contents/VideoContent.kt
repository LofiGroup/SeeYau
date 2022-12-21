package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.ui.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.ui.rememberMediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun VideoContent(
  videoItem: UIMessageType.Video,
  message: UIChatMessage
) {
  val mediaPlayer = LocalMediaPlayer.current
  val state = rememberMediaPlayerState(id = message.id)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(9 / 16f)
      .padding(bottom = LocalSpacing.current.extraSmall)
      .clip(MaterialTheme.shapes.large)
  ) {
    if (state.isCurrentItem) {
      Box(
        modifier = Modifier
          .clip(MaterialTheme.shapes.large)
          .background(LocalExtendedColors.current.darkBackground.copy(alpha = 0.2f))
      ) {
        AndroidView(
          factory = { context ->
            PlayerView(context).also {
              it.player = mediaPlayer.obtainPlayer()
            }
          },
          modifier = Modifier
            .fillMaxSize()
        )
      }
    } else {
      RemoteImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(videoItem.mediaItem.localConfiguration?.uri)
          .crossfade(true)
          .decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options) }
          .build(),
        shape = RectangleShape,
        modifier = Modifier
          .fillMaxSize()
      )

      IconButton(
        onClick = { mediaPlayer.playMedia(videoItem.mediaItem, message.id) },
        modifier = Modifier.align(Alignment.Center)
      ) {
        Icon(
          imageVector = Icons.Filled.PlayArrow,
          contentDescription = null,
          modifier = Modifier.size(LocalSize.current.medium)
        )
      }
    }

    MessageDate(
      message = message,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(LocalSpacing.current.small)
    )
  }
}

@Preview
@Composable
fun VideoMessagePreview() {
  AppTheme {
    Surface() {
      ChatMessageItem(
        chatMessage = getPreviewMessage(
          type = MessageType.Video("")
        )
      )
    }
  }
}