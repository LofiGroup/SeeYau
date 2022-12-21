package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.media_player.ui.rememberMediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.ui.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.ui.components.PlaybackControls
import com.lofigroup.seeyau.features.chat.ui.components.PlayerProgressBar
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun AudioContent(
  message: UIChatMessage,
  audioContent: UIMessageType.Audio,
  modifier: Modifier = Modifier
) {
  val state = rememberMediaPlayerState(id = message.id, duration = audioContent.duration)

  Column(
    modifier = modifier
      .padding(start = LocalSpacing.current.small, end = LocalSpacing.current.medium)
      .padding(top = LocalSpacing.current.small)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      PlaybackControls(
        mediaItem = audioContent.mediaItem,
        state = state,
        id = message.id
      )

      Spacer(modifier = Modifier.width(LocalSpacing.current.small))

      PlayerProgressBar(
        state = state
      )

      Spacer(modifier = Modifier.width(LocalSpacing.current.medium))

      MessageDate(message = message)
    }

    Text(
      text = "${state.progressData.progress} / ${state.progressData.duration}",
      style = MaterialTheme.typography.caption,
      modifier = Modifier.padding(start = LocalSpacing.current.small, bottom = LocalSpacing.current.small)
    )
  }
}

@Preview
@Composable
fun AudioContentPreview() {
  AppTheme {
    Surface() {
      ChatMessageItem(chatMessage = getPreviewMessage(
        type = MessageType.Audio(uri = "", duration = 10000L)
      ))
    }
  }
}