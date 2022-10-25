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
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatDraft
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.OneLiner
import com.sillyapps.core.ui.components.TextLabel
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_time.getLocalTimeFromMillis
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun ChatItem(
  chat: ChatBrief,
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

      val chatDraft = chat.draft
      val lastMessage = chat.lastMessage
      if (chatDraft.isNotBlank()) {
        DraftChatItemContent(draft = chatDraft, chat = chat)
      } else if (chat.partner.blacklistedYou) {
        BlacklistedChatItemContent(chat)
      } else {
        when (lastMessage) {
          is ChatMessage.LikeMessage -> LikeChatItemContent(likeMessage = lastMessage, chat = chat)
          is ChatMessage.PlainMessage -> PlainChatItemContent(plainMessage = lastMessage, chat = chat)
          null -> DefaultChatItemContent(chat = chat)
        }
      }
    }
  }
}

@Composable
fun RowScope.BaseChatItemContent(
  chat: ChatBrief,
  messagePlaceholder: @Composable () -> Unit,
  messageInfoPlaceholder: @Composable RowScope.() -> Unit = {},
  iconsPlaceholder: @Composable RowScope.() -> Unit = {},
  showDefaultIcons: Boolean = true,
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
      if (showDefaultIcons && chat.newMessagesCount > 0)
        TextLabel(
          text = "+${chat.newMessagesCount}",
          modifier = Modifier.padding(start = LocalSpacing.current.extraSmall)
        )
    }
  }
}

@Composable
fun RowScope.BlacklistedChatItemContent(
  chat: ChatBrief
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(
        text = stringResource(id = CommonR.string.user_is_blacklisted_you),
        style = MaterialTheme.typography.subtitle2.copy(color = LocalExtendedColors.current.disabled)
      )
    },
    iconsPlaceholder = {
      Icon(
        painter = painterResource(id = R.drawable.ic_baseline_warning_24),
        contentDescription = null,
        modifier = Modifier.size(LocalSize.current.small)
      )
    },
    showDefaultIcons = false
  )
}

@Composable
fun RowScope.DraftChatItemContent(
  draft: String,
  chat: ChatBrief
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(
        text = draft,
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
  likeMessage: ChatMessage.LikeMessage,
  chat: ChatBrief
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
  chat: ChatBrief
) {
  BaseChatItemContent(
    chat = chat,
    messagePlaceholder = {
      OneLiner(text = plainMessage.message)
    },
    messageInfoPlaceholder = {
      val resId = if (plainMessage.isRead) CommonR.drawable.ic_check_mark_read
      else CommonR.drawable.ic_check_mark_received

      Image(
        painter = painterResource(id = resId),
        contentDescription = null
      )
      Text(
        text = getLocalTimeFromMillis(plainMessage.createdIn),
        style = MaterialTheme.typography.caption
      )
    }
  )
}

@Composable
fun RowScope.DefaultChatItemContent(
  chat: ChatBrief
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
  val item = ChatBrief(
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
      isRead = true
    ),
    newMessagesCount = 1,
    draft = "",
  )

  AppTheme {
    ChatItem(
      chat = item,
      onClick = {},
      onIconClick = {}
    )
  }
}