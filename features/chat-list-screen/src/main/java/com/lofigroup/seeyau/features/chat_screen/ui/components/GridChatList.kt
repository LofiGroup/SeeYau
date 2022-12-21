package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.common.ui.R
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.gridItems

fun LazyListScope.GridChatList(
  items: List<ChatBrief>,
  onItemClick: (ChatBrief) -> Unit,
  keyPrefix: String
) {
  if (items.isEmpty()) {
    item { EmptyFolderContent() }
  } else {
    gridItems(
      data = items,
      columnCount = 4,
      keyPrefix = keyPrefix,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
      GridChatItem(
        item = it,
        onClick = { onItemClick(it) }
      )
    }
  }
}

@Composable
fun GridChatItem(
  item: ChatBrief,
  onClick: () -> Unit
) {
  Box() {
    RemoteImage(
      model = item.partner.imageUrl,
      shape = MaterialTheme.shapes.medium,
      onClick = onClick,
      modifier = Modifier.padding(6.dp)
    )
    if (item.newMessagesCount > 0)
      Icon(
        painter = painterResource(id = R.drawable.ic_interaction),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(LocalSpacing.current.extraSmall)
      )
  }
}