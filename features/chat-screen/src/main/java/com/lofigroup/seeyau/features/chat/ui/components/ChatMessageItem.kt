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
import com.lofigroup.seeyau.features.chat.ui.composition_locals.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun ChatMessageItem(
  chatMessage: UIChatMessage,
  modifier: Modifier = Modifier,

  onImageClick: (String) -> Unit = {},
  onVideoClick: (UIMessageType.Video) -> Unit = {},
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
            message = chatMessage,
            onVideoClick = onVideoClick
          )
        }
        is UIMessageType.Image -> {
          ImageContent(
            content = chatMessage.type,
            message = chatMessage,
            onImageClick = onImageClick
          )
        }
        is UIMessageType.Audio -> {
          AudioContent(
            message = chatMessage,
            audioContent = chatMessage.type,
            modifier = Modifier
              .clip(MaterialTheme.shapes.large)
              .background(style.color)
          )
        }
        else -> {
          PlainMessage(
            message = chatMessage,
            style = style,
            maxLines = maxLines,
            modifier = Modifier
              .clip(MaterialTheme.shapes.large)
              .background(style.color)
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
      ChatMessageItem(
        chatMessage = getPreviewMessage(),
      )
    }
  }
}

@Preview
@Composable
fun ChatPartnerMessagePreview() {
  AppTheme() {
    Surface() {
      ChatMessageItem(
        chatMessage = getPreviewMessage(authorIsMe = false),
      )
    }
  }
}
