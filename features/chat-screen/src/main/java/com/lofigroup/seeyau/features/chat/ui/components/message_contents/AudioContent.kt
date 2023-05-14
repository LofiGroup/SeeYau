package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.common.ui.getFormattedFileSize
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MediaData
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.media_player.ui.rememberMediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.ui.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.ui.components.PlaybackControls
import com.lofigroup.seeyau.features.chat.ui.components.PlayerProgressBar
import com.lofigroup.seeyau.features.chat.ui.composition_locals.LocalChatMediaDownloader
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_network.file_downloader.models.DownloadProgress
import timber.log.Timber

@Composable
fun AudioContent(
  message: UIChatMessage,
  audioContent: UIMessageType.Audio,
  modifier: Modifier = Modifier
) {
  val state = rememberMediaPlayerState(id = message.id, duration = audioContent.duration)

  Column(
    modifier = modifier
      .padding(start = LocalSpacing.current.small, end = 10.dp)
      .padding(top = LocalSpacing.current.small)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (audioContent.mediaData.isSavedLocally) {
        PlaybackControls(
          mediaItem = audioContent.mediaItem,
          state = state,
          id = message.id,
          modifier = Modifier
            .size(LocalSize.current.medium)
        )
      } else {
        DownloadAudioButton(
          messageId = message.id,
          modifier = Modifier
            .size(LocalSize.current.medium)
        )
      }

      Spacer(modifier = Modifier.width(LocalSpacing.current.small))

      PlayerProgressBar(
        state = state,
        modifier = Modifier.weight(1f)
      )

      Spacer(modifier = Modifier.width(LocalSpacing.current.medium))

      MessageDate(message = message)
    }

    Text(
      text = "${state.progressData.progress} / ${state.progressData.duration}",
      style = MaterialTheme.typography.caption,
      modifier = Modifier.padding(
        start = LocalSpacing.current.small,
        bottom = LocalSpacing.current.small
      )
    )
  }
}

@Composable
fun DownloadAudioButton(
  messageId: Long,
  modifier: Modifier = Modifier
) {
  val fileDownloader = LocalChatMediaDownloader.current

  val loadingProgress by fileDownloader.subscribe(messageId).collectAsState(initial = DownloadProgress())

  Box(modifier = modifier) {
    if (loadingProgress.isStarted) {
      LaunchedEffect(key1 = loadingProgress) {
        Timber.e("New progress: ${loadingProgress.progress}")
      }
      CircularProgressIndicator(
        progress = loadingProgress.progress,
        color = LocalExtendedColors.current.disabled,
      )
    } else {
      IconButton(
        onClick = { fileDownloader.startDownload(messageId) },
      ) {
        Icon(
          imageVector = Icons.Filled.Download,
          contentDescription = null,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

@Preview
@Composable
fun AudioContentPreview() {
  AppTheme {
    Surface() {
      ChatMessageItem(chatMessage = getPreviewMessage(
        type = MessageType.Audio(MediaData(uri = "", fileSize = 1), duration = 10000L),
      ))
    }
  }
}