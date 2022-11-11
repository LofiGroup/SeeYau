package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seayau.common.ui.components.UserIcon
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.common.chat.components.MessageStatusIcon
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat_screen.R
import com.lofigroup.seeyau.features.chat_screen.model.FolderChat
import com.sillyapps.core.ui.components.OneLiner
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_time.getLocalTimeFromMillis
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun ChatItem(
  chat: FolderChat,
  onClick: (Long) -> Unit,
  onIconClick: (String?) -> Unit,
) {
  Surface(
    modifier = Modifier
      .clickable { onClick(chat.id) }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.medium)
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {
      UserIcon(
        imageUri = chat.partner.imageUrl,
        isOnline = chat.partner.isOnline,
        onClick = { onIconClick(chat.partner.imageUrl) }
      )

      when (chat) {
        is FolderChat.LikeChat -> {
          LikeChatItemContent(chat = chat)
        }
        is FolderChat.MemoryChat -> {
          DraftChatItemContent(chat = chat)
        }
        is FolderChat.DefaultChat -> {
          when (val msg = chat.lastMessage) {
            is ChatMessage.LikeMessage -> {
              LikeChatItemContent(chat = chat)
            }
            is ChatMessage.PlainMessage -> {
              PlainChatItemContent(chat = chat, plainMessage = msg)
            }
            null -> {
              DefaultChatItemContent(chat = chat)
            }
          }
        }
      }
    }
  }
}

@Composable
fun RowScope.BaseChatItemContent(
  chat: FolderChat,
  messagePlaceholder: @Composable () -> Unit,
  messageInfoPlaceholder: @Composable RowScope.() -> Unit = {},
  iconsPlaceholder: @Composable RowScope.() -> Unit = {},
) {
  Column(
    modifier = Modifier
      .weight(1f)
      .padding(start = LocalSpacing.current.small)
  ) {
    Text(
      text = chat.partner.name,
      style = MaterialTheme.typography.body2,
      modifier = Modifier.padding(bottom = LocalSpacing.current.extraSmall)
    )

    messagePlaceholder()
  }

  Box(
    modifier = Modifier.fillMaxHeight()
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(bottom = LocalSpacing.current.small)
        .align(Alignment.TopEnd)
    ) {
      messageInfoPlaceholder()
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .align(Alignment.BottomEnd)
    ) {
      iconsPlaceholder()
    }
  }
}

@Composable
fun RowScope.DraftChatItemContent(
  chat: FolderChat.MemoryChat
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(
        text = chat.message,
        style = MaterialTheme.typography.subtitle2.copy(color = Color.Gray)
      )
    },
    messageInfoPlaceholder = {
      Text(
        text = stringResource(id = R.string.draft),
        style = MaterialTheme.typography.caption,
      )
    },
  )
}

@Composable
fun RowScope.LikeChatItemContent(
  chat: FolderChat
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(
        text = stringResource(id = CommonR.string.sent_you_like),
        style = MaterialTheme.typography.subtitle2.copy(color = Color.Gray)
      )
    },
    iconsPlaceholder = {
      Image(
        painter = painterResource(id = CommonR.drawable.ic_like),
        contentDescription = null
      )
    }
  )
}

@Composable
fun RowScope.PlainChatItemContent(
  plainMessage: ChatMessage.PlainMessage,
  chat: FolderChat.DefaultChat
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(text = plainMessage.message)
    },
    messageInfoPlaceholder = {
      if (plainMessage.author == 0L)
        MessageStatusIcon(messageStatus = plainMessage.status)

      Spacer(modifier = Modifier.width(LocalSpacing.current.extraSmall))
      
      Text(
        text = getLocalTimeFromMillis(plainMessage.createdIn),
        style = MaterialTheme.typography.caption
      )
    },
    iconsPlaceholder = {
      if (chat.newMessagesCount > 0)
        TextLabel(
          text = "+${chat.newMessagesCount}",
          modifier = Modifier.padding(start = LocalSpacing.current.extraSmall)
        )
    }
  )
}

@Composable
fun RowScope.DefaultChatItemContent(
  chat: FolderChat.DefaultChat
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(text = stringResource(id = R.string.say_hello))
    }
  )
}

@Preview
@Composable
fun ChatItemPreview() {
  val item = FolderChat.DefaultChat(
    id = 0,
    partner = User(
      id = 0,
      name = "Honda",
      imageUrl = "",
      isNear = true,
      lastConnection = 0,
      isOnline = true,

      likedYouAt = null,
      blacklistedYou = true,
      likedAt = null
    ),
    lastMessage = ChatMessage.PlainMessage(
      id = 0,
      message = "Hello!",
      author = 0,
      createdIn = 0L,
      status = MessageStatus.SENT
    ),
    newMessagesCount = 1,
    createdIn = 0L
  )

  AppTheme {
    ChatItem(
      chat = item,
      onClick = {},
      onIconClick = {}
    )
  }
}