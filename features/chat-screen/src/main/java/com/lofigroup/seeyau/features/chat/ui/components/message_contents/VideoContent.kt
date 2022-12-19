package com.lofigroup.seeyau.features.chat.ui.components.message_contents

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
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.ui.components.defaultMediaState
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun VideoContent(
  videoItem: UIMessageType.Video,
  id: Int
) {
  val mediaPlayer = LocalMediaPlayer.current

  var state by remember {
    mutableStateOf(defaultMediaState)
  }

  LaunchedEffect(Unit) {
    mediaPlayer.registerState(id = id).collect() { state = it }
  }

  DisposableEffect(key1 = Unit) {
    onDispose { mediaPlayer.unregisterState(id) }
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(9 / 16f)
      .padding(bottom = LocalSpacing.current.extraSmall)
      .clip(MaterialTheme.shapes.medium)
  ) {
    if (state.isCurrentItem) {
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
        modifier = Modifier
          .fillMaxSize()
      )

      IconButton(
        onClick = { mediaPlayer.playMedia(videoItem.mediaItem, id) },
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
          type = MessageType.Video("")
        )
      )
    }
  }
}