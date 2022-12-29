package com.lofigroup.seeyau.features.chat.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.ui.PlayerView
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.UpButton
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.ui.rememberMediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.ui.components.message_contents.VideoPlayer
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.components.ZoomableBox

private const val closeUpVideoId = -3L

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CloseUpVideo(
  visible: Boolean,
  videoContent: UIMessageType.Video,
  onDismiss: () -> Unit
) {
  val mediaPlayer = LocalMediaPlayer.current
  val state = rememberMediaPlayerState(id = closeUpVideoId, duration = videoContent.duration)

  LaunchedEffect(visible) {
    if (visible)
      mediaPlayer.playMedia(videoContent.mediaItem, closeUpVideoId)
  }

  AnimatedVisibility(
    visible = visible,
    enter = scaleIn(),
    exit = scaleOut() + slideOutHorizontally { it / 2 }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(if (visible) MaterialTheme.colors.background else Color.Transparent)
        .systemBarsPadding()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = onDismiss
        )
    ) {
      if (visible)
        DefaultTopBar(
          leftContent = { UpButton(onClick = onDismiss) },
          modifier = Modifier.zIndex(1f)
        )

      VideoPlayer(
        videoContent = videoContent,
        state = state,
        id = closeUpVideoId,
        mediaPlayer = mediaPlayer,
        onFullScreenButtonClick = onDismiss,
        fullScreen = true
      )
    }
  }

  if (visible)
    BackHandler {
      onDismiss()
    }
}