package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.lofigroup.seeyau.features.chat.model.getPreviewPrivateMessage
import com.lofigroup.seeyau.features.chat.styling.ChatMessageStyleProvider
import com.lofigroup.seeyau.features.chat.styling.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.pxToDp
import com.sillyapps.core_time.getLocalTimeFromMillis
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun ChatMessageItem(
  chatMessage: PrivateMessage
) {
  val style =
    if (chatMessage.authorIsMe)
      LocalChatMessageStyles.current.myMessageStyle
    else
      LocalChatMessageStyles.current.partnerMessageStyle

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = style.startPadding, end = style.endPadding)
      .padding(bottom = LocalSpacing.current.small)
  ) {
    Box(
      modifier = Modifier
        .clip(MaterialTheme.shapes.large)
        .background(style.brush)
        .padding(
          vertical = LocalSpacing.current.small,
          horizontal = 10.dp
        )
        .align(style.alignment)
    ) {
      Text(
        text = chatMessage.message,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
          .padding(
            end = LocalSpacing.current.extraSmall +
                (if (chatMessage.authorIsMe) 50.dp else 30.dp)
          )
          .align(Alignment.TopCenter)
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.BottomEnd)
      ) {
        Text(
          text = chatMessage.dateTime.time,
          style = MaterialTheme.typography.caption,
        )

        if (chatMessage.authorIsMe) {
          Spacer(modifier = Modifier.width(2.dp))
          Image(
            painter = painterResource(id = if (chatMessage.isRead) CommonR.drawable.ic_check_mark_read else CommonR.drawable.ic_check_mark_received),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
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
