package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.theme.LocalIconsSize
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User
import com.sillyapps.core.ui.components.RemoteImage
import com.lofigroup.seayau.common.ui.R as CommonR
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_time.getLocalTimeFromMillis

@Composable
fun ChatItem(
  chat: ChatBrief,
  onClick: (Long) -> Unit
) {
  Surface(
    modifier = Modifier
      .clickable { onClick(chat.id) }
  ) {
    Row(modifier = Modifier
      .fillMaxWidth()
      .padding(LocalSpacing.current.medium)
      .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {
      RemoteImage(
        model = chat.partner.imageUrl,
        placeholderResId = CommonR.drawable.ic_baseline_account_box_24,
        errorPlaceholderResId = CommonR.drawable.ic_baseline_account_box_24,
        modifier = Modifier
          .size(LocalIconsSize.current.medium)
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(start = LocalSpacing.current.small)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = LocalSpacing.current.extraSmall)
        ) {
          Text(
            text = chat.partner.name,
            style = MaterialTheme.typography.body2,
          )
        }
        Text(
          text = chat.lastMessage?.message ?: stringResource(id = R.string.say_hello),
          style = MaterialTheme.typography.subtitle2,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1
        )
      }

      val lastMessage = chat.lastMessage
      if (lastMessage != null) {
        Column(horizontalAlignment = Alignment.End) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = LocalSpacing.current.extraSmall)
          ) {
            val resId = if (lastMessage.isRead) CommonR.drawable.ic_check_mark_read
              else CommonR.drawable.ic_check_mark_received
            Image(
              painter = painterResource(id = resId),
              contentDescription = null
            )
            Text(
              text = getLocalTimeFromMillis(lastMessage.createdIn),
              style = MaterialTheme.typography.caption
            )
          }
          TextLabel(text = "+${chat.newMessagesCount}")
        }
      }
    }
  }
}

@Preview
@Composable
fun ChatItemPreview() {
  val item = ChatBrief(
    id = 0,
    partner = User(
      id = 0,
      name = "Honda",
      imageUrl = "",
      isNear = true,
      lastConnection = 0,
      isOnline = true
    ),
    lastMessage = ChatMessage(id = 0, message = "Hello!", author = 0, createdIn = 0L, isRead = true),
    newMessagesCount = 1
  )

  AppTheme {
    ChatItem(
      chat = item,
      onClick = {}
    )
  }
}