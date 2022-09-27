package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lofigroup.seeyau.domain.chat.models.ChatBrief

@Composable
fun ChatList(
  chats: List<ChatBrief>,
  modifier: Modifier = Modifier,
  onItemClick: (Long) -> Unit
) {
  LazyColumn(modifier = modifier) {
    items(items = chats) {
      ChatItem(
        chat = it,
        onClick = onItemClick
      )
    }
  }
}