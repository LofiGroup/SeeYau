package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lofigroup.features.navigator_screen.model.PreviewMessage
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seeyau.features.chat.styling.ChatMessageStyleProvider
import com.lofigroup.seeyau.features.chat.styling.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.pxToDp

@Composable
fun UserNewMessages(
  selectedUser: UserItemUIModel?
) {
  ChatMessageStyleProvider(
    partnerMessageStyleBrush = LocalExtendedColors.current.secondaryGradient
  ) {
    if (selectedUser != null && selectedUser.messagesIsCollapsed) {
      Column() {
        selectedUser.newMessages.groupBy { it.dateTime.date }.forEach { (date, messages) ->
          for (message in messages) {
            PreviewMessage(message)
          }
          DateHeader(date = date)
        }
      }
    }
  }

}

@Composable
fun PreviewMessage(
  message: PreviewMessage
) {
  /*val style = LocalChatMessageStyles.current.partnerMessageStyle

  val context = LocalContext.current

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
        text = message.message,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
          .padding(
            end = LocalSpacing.current.extraSmall + context.pxToDp(labelSize.width)
          )
          .align(Alignment.TopCenter)
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .onSizeChanged { size ->
            labelSize = size
          }
      ) {
        Text(
          text = chatMessage.dateTime.time,
          style = MaterialTheme.typography.caption,
        )

        if (chatMessage.authorIsMe) {
          Spacer(modifier = Modifier.width(2.dp))
          Image(
            painter = painterResource(id = if (chatMessage.isRead) R.drawable.ic_check_mark_read else R.drawable.ic_check_mark_received),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }*/
}

@Composable
fun DateHeader(date: String) {
  Text(
    text = date,
    style = MaterialTheme.typography.caption,
    textAlign = TextAlign.Center,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium)
      .fillMaxWidth()
  )
}