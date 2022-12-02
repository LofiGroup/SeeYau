package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.features.chat.components.message_contents.BaseMessage
import com.lofigroup.seeyau.features.chat.components.message_contents.LikeMessage
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.styling.ChatMessageStyleProvider
import com.lofigroup.seeyau.features.chat.styling.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ChatMessageItem(
  chatMessage: UIChatMessage,
  modifier: Modifier = Modifier,
  isCurrentItem: Boolean = false,
  maxLines: Int = Int.MAX_VALUE
) {
  val style =
    if (chatMessage.authorIsMe) LocalChatMessageStyles.current.myMessageStyle
    else LocalChatMessageStyles.current.partnerMessageStyle

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(start = style.startPadding, end = style.endPadding)
      .padding(bottom = LocalSpacing.current.small)
  ) {
    when (chatMessage.type) {
      UIMessageType.Like -> {
        LikeMessage(style = style)
      }
      else -> {
        BaseMessage(message = chatMessage, style = style, maxLines = maxLines, isCurrentItem = isCurrentItem)
      }
    }
  }
}

@Preview
@Composable
fun ChatMyMessagePreview() {
  AppTheme() {
    Surface() {
      ChatMessageStyleProvider() {
        ChatMessageItem(
          chatMessage = getPreviewPrivateMessage()
        )
      }
    }
  }
}

@Preview
@Composable
fun ChatPartnerMessagePreview() {
  AppTheme() {
    Surface() {
      ChatMessageStyleProvider() {
        ChatMessageItem(
          chatMessage = getPreviewPrivateMessage(authorIsMe = false)
        )
      }
    }
  }
}
