package com.lofigroup.seeyau.features.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.model.getPreviewMessage
import com.lofigroup.seeyau.features.chat.ui.components.message_contents.*
import com.lofigroup.seeyau.features.chat.ui.providers.ChatMessageStyleProvider
import com.lofigroup.seeyau.features.chat.ui.providers.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ChatMessageItem(
  chatMessage: UIChatMessage,
  modifier: Modifier = Modifier,
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
    Box(
      modifier = Modifier
        .align(style.alignment)
    ) {
      when (chatMessage.type) {
        UIMessageType.Like -> {
          LikeMessage(style = style)
        }
        is UIMessageType.Video -> {
          VideoContent(
            videoItem = chatMessage.type,
            message = chatMessage
          )
        }
        is UIMessageType.Image -> {
          ImageContent(
            content = chatMessage.type,
            message = chatMessage
          )
        }
        is UIMessageType.Audio -> {
          AudioContent(
            message = chatMessage,
            audioContent = chatMessage.type,
            modifier = Modifier
              .clip(MaterialTheme.shapes.large)
              .background(MaterialTheme.colors.primaryVariant)
          )
        }
        else -> {
          PlainMessage(
            message = chatMessage,
            style = style,
            maxLines = maxLines,
            modifier = Modifier
              .clip(MaterialTheme.shapes.large)
              .background(MaterialTheme.colors.primaryVariant)
          )
        }
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
          chatMessage = getPreviewMessage()
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
          chatMessage = getPreviewMessage(authorIsMe = false)
        )
      }
    }
  }
}
