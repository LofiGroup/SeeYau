package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun ChatList(
  nearby: List<ChatBrief>,
  metToday: List<ChatBrief>,
  chats: List<ChatBrief>,
  isVisible: Boolean,

  onItemClick: (Long) -> Unit,
  onGridItemClick: (ChatBrief) -> Unit,
  onDeleteChat: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {

  var nearbyExpanded by remember() {
    mutableStateOf(true)
  }
  var metExpanded by remember() {
    mutableStateOf(true)
  }

  val context = LocalContext.current

  LazyColumn(modifier = modifier) {
    FolderItem(
      content = {
        if (isVisible)
          GridChatList(
            items = nearby,
            onItemClick = onGridItemClick,
            keyPrefix = "nearby"
          )
        else
          item { InvisibleInfo() }
      },
      expanded = nearbyExpanded,
      toggleExpanded = { nearbyExpanded = !nearbyExpanded },
      title = context.getString(R.string.folder_title_nearby),
      imageResId = CommonR.drawable.nearby_icon,
    )
    FolderItem(
      content = {
        GridChatList(
          items = metToday,
          onItemClick = onGridItemClick,
          keyPrefix = "met"
        )
      },
      expanded = metExpanded,
      toggleExpanded = { metExpanded = !metExpanded },
      title = context.getString(R.string.folder_title_met),
      imageResId = CommonR.drawable.ic_history,
    )
    FolderItem(
      content = {
        items(
          items = chats,
          key = { "chat_${it.id}" },
          contentType = { "chat_item" }
        ) { chat ->
          ChatItem(
            chat = chat,
            onClick = onItemClick,
            onDeleteChat = onDeleteChat
          )
        }
        item {
          Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))
        }
      },
      title = context.getString(R.string.chats),
      imageResId = CommonR.drawable.ic_interaction
    )
  }
}

@Composable
fun InvisibleInfo() {
  Box(
    modifier = Modifier
      .fillMaxWidth(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = stringResource(id = R.string.not_visible_info),
      color = LocalExtendedColors.current.lightBackground,
      textAlign = TextAlign.Center
    )
  }
}
