package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.UserIcon
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.common.chat.components.MessageStatusIcon
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.lofigroup.seeyau.domain.profile.model.User
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.components.OneLiner
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import timber.log.Timber
import com.lofigroup.seeyau.common.ui.R as CommonR

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatItem(
  chat: ChatBrief,
  onClick: (Long) -> Unit,
  onDeleteChat: (Long) -> Unit,
) {
  val dismissState = rememberDismissState()
  val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)

  LaunchedEffect(isDismissed) {
    if (isDismissed) {
      dismissState.reset()
      onDeleteChat(chat.partner.id)
    }
  }

  SwipeToDismiss(
    state = dismissState,
    background = { SwipeToDismissBackground() },
    directions = setOf(DismissDirection.EndToStart)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .clickable { onClick(chat.id) }
        .padding(LocalSpacing.current.medium)
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {

      BaseChatItemContent(chat = chat)
    }
  }
}

@Composable
fun SwipeToDismissBackground() {
  Box(
    contentAlignment = Alignment.CenterEnd,
    modifier = Modifier
      .fillMaxSize()
  ) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth(0.5f)
        .fillMaxHeight()
        .background(MaterialTheme.colors.error)
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(end = LocalSpacing.current.small)
    ) {
      Text(text = stringResource(id = R.string.delete))
      Spacer(modifier = Modifier.width(LocalSpacing.current.small))
      Icon(painter = painterResource(id = R.drawable.trash), contentDescription = null)
    }
  }
}

@Composable
fun RowScope.BaseChatItemContent(
  chat: ChatBrief,
) {
  UserIcon(
    imageUri = chat.partner.imageUrl,
    isOnline = chat.partner.isOnline,
    onClick = {}
  )

  MessageContent(lastMessage = chat.lastMessage, draft = chat.draft.message)

  IconContent(messageType = chat.lastMessage?.type, newMessagesCount = chat.newMessagesCount)
}

@Composable
fun RowScope.MessageContent(
  lastMessage: ChatMessage?,
  draft: String
) {
  Row(
    Modifier
      .weight(1f)
      .padding(horizontal = LocalSpacing.current.small)
  ) {
    when {
      draft.isNotBlank() -> {
        InformationText(text = stringResource(id = R.string.draft))
        OneLiner(text = draft)
      }
      lastMessage == null -> OneLiner(text = stringResource(id = R.string.say_hello))
      lastMessage.type is MessageType.Like -> OneLiner(text = stringResource(id = CommonR.string.sent_you_like))
      else -> {
        if (lastMessage.author == 0L) InformationText(text = stringResource(id = R.string.you))
        when (lastMessage.type) {
          is MessageType.Audio -> SmallIcon(imageVector = Icons.Filled.AudioFile)
          is MessageType.Image -> SmallIcon(imageVector = Icons.Filled.Image)
          is MessageType.Video -> SmallIcon(imageVector = Icons.Filled.VideoFile)
          else -> Unit
        }
        OneLiner(text = lastMessage.message)
      }
    }
  }
}

@Composable
fun RowScope.InformationText(
  text: String,
) {
  Text(
    text = text,
    color = LocalExtendedColors.current.darkBackground,
    modifier = Modifier.padding(end = LocalSpacing.current.extraSmall)
  )
}

@Composable
fun IconContent(
  messageType: MessageType?,
  newMessagesCount: Int
) {
  Box(
    modifier = Modifier.size(LocalSize.current.verySmall)
  ) {
    when {
      messageType == MessageType.Like -> Image(
        painter = painterResource(id = CommonR.drawable.ic_like_new),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
        )
      newMessagesCount > 0 -> Icon(
        painter = painterResource(id = R.drawable.ic_indicator),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
  
}

@Composable
fun SmallIcon(imageVector: ImageVector) {
  Icon(
    imageVector = imageVector,
    contentDescription = null,
    modifier = Modifier
      .size(LocalSize.current.small)
      .padding(end = LocalSpacing.current.extraSmall)
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
      likedAt = null,
      lastContact = 0L
    ),
    lastMessage = ChatMessage(
      id = 0,
      message = "Hello!",
      author = 0,
      createdIn = 0L,
      status = MessageStatus.SENT,
      type = MessageType.Like,
      chatId = 0L
    ),
    newMessagesCount = 1,
    createdIn = 0L
  )

  AppTheme {
    ChatItem(
      chat = item,
      onClick = {},
      onDeleteChat = {}
    )
  }
}