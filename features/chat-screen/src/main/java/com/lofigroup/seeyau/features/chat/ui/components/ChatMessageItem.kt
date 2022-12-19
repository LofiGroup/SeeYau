package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.ui.components.message_contents.BaseMessage
import com.lofigroup.seeyau.features.chat.ui.components.message_contents.LikeMessage
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.ui.components.message_contents.ImageContent
import com.lofigroup.seeyau.features.chat.ui.components.message_contents.VideoContent
import com.lofigroup.seeyau.features.chat.ui.providers.ChatMessageStyleProvider
import com.lofigroup.seeyau.features.chat.ui.providers.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ChatMessageItem(
  chatMessage: UIChatMessage,
  modifier: Modifier = Modifier,
  isFocused: Boolean = false,
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
      is UIMessageType.Video -> {
        VideoContent(
          videoItem = chatMessage.type,
          id = chatMessage.pos
        )
      }
      is UIMessageType.Image -> {
        ImageContent(
          content = chatMessage.type,
          createdIn = chatMessage.dateTime.time
        )
      }
      else -> {
        BaseMessage(message = chatMessage, style = style, maxLines = maxLines)
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
