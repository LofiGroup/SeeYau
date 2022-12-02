package com.lofigroup.seeyau.features.chat.components.message_contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.common.chat.components.MessageStatusIcon
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.styling.ChatMessageStyle
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun BoxScope.BaseMessage(
  message: UIChatMessage,
  isCurrentItem: Boolean,
  style: ChatMessageStyle,
  modifier: Modifier = Modifier,
  maxLines: Int = Int.MAX_VALUE
) {
  Column(
    modifier = modifier
      .clip(MaterialTheme.shapes.large)
      .background(style.brush)
      .align(style.alignment)
      .padding(
        vertical = LocalSpacing.current.small,
        horizontal = 10.dp
      ),
    horizontalAlignment = Alignment.End
  ) {
    when (val typeContent = message.type) {
      is UIMessageType.Audio -> {
        AudioContent(audioContent = typeContent, isCurrentItem = isCurrentItem, pos = message.pos)
      }
      is UIMessageType.Video -> {
        VideoContent(videoItem = typeContent, isPlaying = isCurrentItem, pos = message.pos)
      }
      is UIMessageType.Image -> {
        ImageContent(content = typeContent)
      }
      is UIMessageType.Contact -> {

      }
      else -> {}
    }

    Box {
      Text(
        text = message.message,
        style = MaterialTheme.typography.body1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .padding(end = LocalSpacing.current.extraSmall + style.datePadding)
          .align(Alignment.TopCenter),
        maxLines = maxLines
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.BottomEnd)
      ) {
        Text(
          text = message.dateTime.time,
          style = MaterialTheme.typography.caption,
        )

        if (style.hasMessageMark) {
          Spacer(modifier = Modifier.width(2.dp))
          MessageStatusIcon(messageStatus = message.status)
        }
      }
    }
  }

}