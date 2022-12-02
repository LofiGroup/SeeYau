package com.lofigroup.seeyau.features.chat.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import timber.log.Timber

@Composable
fun VideoContent(
  videoItem: UIMessageType.Video,
  pos: Int,
  isPlaying: Boolean
) {
  val mediaPlayer = LocalMediaPlayer.current

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(9 / 16f)
      .padding(bottom = LocalSpacing.current.extraSmall)
  ) {
    if (isPlaying) {
      AndroidView(
        factory = { context ->
          PlayerView(context).also {
            it.player = mediaPlayer.obtainPlayer()
          }
        },
        modifier = Modifier
          .fillMaxSize()
      )
    } else {
      RemoteImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(videoItem.mediaItem.localConfiguration?.uri)
          .crossfade(true)
          .decoderFactory { result, options, _ -> VideoFrameDecoder(result.source, options) }
          .build(),
        shape = RectangleShape,
        shimmerColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
          .fillMaxSize()
      )

      IconButton(
        onClick = { mediaPlayer.playMedia(videoItem.mediaItem, pos) },
        modifier = Modifier.align(Alignment.Center)
      ) {
        Icon(
          imageVector = Icons.Filled.PlayArrow,
          contentDescription = null,
          modifier = Modifier.size(LocalSize.current.medium)
        )
      }
    }
  }
}

@Preview
@Composable
fun VideoMessagePreview() {
  AppTheme {
    Surface() {
      ChatMessageItem(
        chatMessage = getPreviewPrivateMessage(
          type = MessageType.Video("", "")
        )
      )
    }
  }
}