package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.features.chat.model.PrivateMessage

@Composable
fun ColumnScope.ChatMessages(
  items: List<PrivateMessage>
) {
  LazyColumn(
    reverseLayout = true,
    modifier = Modifier
      .weight(1f)
      .fillMaxWidth(),
  ) {
    items(items = items) { message ->
      Box(modifier = Modifier.fillMaxWidth()) {
        ChatMessageItem(
          chatMessage = message
        )
      }
    }
  }
}