package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.lofigroup.core.util.cancelAndLaunch
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.ui.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.ui.rememberMediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.ui.components.PlaybackControls
import com.lofigroup.seeyau.features.chat.ui.components.PlayerProgressBar
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun VideoContent(
  videoItem: UIMessageType.Video,
  message: UIChatMessage,
  onVideoClick: (UIMessageType.Video) -> Unit
) {
  val mediaPlayer = LocalMediaPlayer.current
  val state = rememberMediaPlayerState(id = message.id, duration = videoItem.duration)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(videoItem.aspectRatio)
      .padding(bottom = LocalSpacing.current.extraSmall)
      .clip(MaterialTheme.shapes.large)
  ) {
    if (state.isCurrentItem) {
      VideoPlayer(
        mediaPlayer = mediaPlayer,
        id = message.id,
        state = state,
        videoContent = videoItem,
        onFullScreenButtonClick = { onVideoClick(videoItem) },
        modifier = Modifier
          .background(LocalExtendedColors.current.darkBackground.copy(alpha = 0.2f))
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
        onClick = { mediaPlayer.playMedia(videoItem.mediaItem, message.id) },
        modifier = Modifier.align(Alignment.Center)
      ) {
        Icon(
          imageVector = Icons.Filled.PlayArrow,
          contentDescription = null,
          modifier = Modifier.size(LocalSize.current.medium)
        )
      }

      MessageDate(
        message = message,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(LocalSpacing.current.small)
      )
    }
  }
}

@Composable
fun VideoPlayer(
  videoContent: UIMessageType.Video,
  state: MediaPlayerState,
  id: Long,
  mediaPlayer: MediaPlayer,
  onFullScreenButtonClick: () -> Unit,
  modifier: Modifier = Modifier,
  fullScreen: Boolean = false,
) {
  val scope = rememberCoroutineScope()
  var controlsVisible by remember {
    mutableStateOf(false)
  }

  Box(
    modifier = modifier
  ) {
    AndroidView(
      factory = { context ->
        PlayerView(context).apply {
          player = mediaPlayer.obtainPlayer()
          useController = false
        }
      },
      modifier = Modifier
        .align(Alignment.Center)
    )

    VideoPlayerControls(
      isVisible = controlsVisible,
      isInteractedWith = { clickedBackground ->
        if (clickedBackground && controlsVisible) {
          controlsVisible = false
          return@VideoPlayerControls
        }

        controlsVisible = true

        scope.cancelAndLaunch {
          delay(5000L)
          controlsVisible = false
        }
      },
      videoContent = videoContent,
      state = state,
      id = id,
      onFullScreenButtonClick = onFullScreenButtonClick,
      fullScreen = fullScreen
    )
  }
}

@Composable
fun VideoPlayerControls(
  isVisible: Boolean,
  videoContent: UIMessageType.Video,
  isInteractedWith: (Boolean) -> Unit,
  state: MediaPlayerState,
  id: Long,
  onFullScreenButtonClick: () -> Unit,
  fullScreen: Boolean
) {
  AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = { isInteractedWith(true) }
        )
    ) {
      PlaybackControls(
        mediaItem = videoContent.mediaItem,
        state = state,
        id = id,
        modifier = Modifier
          .align(Alignment.Center),
        notifyInteractedWith = { isInteractedWith(false) }
      )

      Column(
        modifier = Modifier
          .padding(horizontal = LocalSpacing.current.small)
          .align(Alignment.BottomCenter)
      ) {
        PlayerProgressBar(
          state = state,
          modifier = Modifier
            .fillMaxWidth(),
          notifyInteractedWith = { isInteractedWith(false) }
        )

        Box(
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "${state.progressData.progress} / ${state.progressData.duration}",
            style = MaterialTheme.typography.caption,
            modifier = Modifier
              .align(Alignment.CenterStart)
          )

          IconButton(
            onClick = {
              onFullScreenButtonClick()
              isInteractedWith(false)
            },
            modifier = Modifier
              .align(Alignment.CenterEnd)
          ) {
            Icon(
              imageVector = if (fullScreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
              contentDescription = null
            )
          }
        }
      }
    }
  }

  if (!isVisible) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = { isInteractedWith(true) }
        ),
    ) {}
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
        ),
        onImageClick = {},
        onVideoClick = {}
      )
    }
  }
}