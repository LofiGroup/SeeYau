package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.features.chat_screen.R
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun ChatList(
  nearby: List<ChatBrief>,
  metToday: List<ChatBrief>,
  chats: List<ChatBrief>,

  onItemClick: (Long) -> Unit,
  onGridItemClick: (ChatBrief) -> Unit,
  onDeleteChat: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {

  val (nearbyExpanded, setNearbyExpanded) = rememberSaveable() {
    mutableStateOf(true)
  }
  val (metExpanded, setMetExpanded) = rememberSaveable() {
    mutableStateOf(true)
  }

  LazyColumn(modifier = modifier) {
    FolderItem(
      content = {
        GridChatList(
          items = nearby,
          onItemClick = onGridItemClick
        )
      },
      expanded = nearbyExpanded,
      setExpanded = setNearbyExpanded,
      textResId = R.string.nearby,
      imageResId = R.drawable.ic_nearby_icon,
    )
    FolderItem(
      content = {
        GridChatList(
          items = metToday,
          onItemClick = onGridItemClick
        )
      },
      expanded = metExpanded,
      setExpanded = setMetExpanded,
      textResId = R.string.met,
      imageResId = R.drawable.ic_met_icon,
    )
    FolderItem(
      content = {
        items(chats) { chat ->
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
      textResId = R.string.chats,
      imageResId = CommonR.drawable.ic_interaction
    )
  }
}

