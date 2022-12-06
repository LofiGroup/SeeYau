package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lofigroup.seeyau.features.chat_screen.R
import com.lofigroup.seeyau.features.chat_screen.model.FolderChat
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun ChatList(
  memoryFolder: List<FolderChat.MemoryChat>,
  likesFolder: List<FolderChat.LikeChat>,
  interactionFolder: List<FolderChat.DefaultChat>,

  onItemClick: (Long) -> Unit,
  onIconClick: (String?) -> Unit,
  modifier: Modifier = Modifier,
) {
  val (memoriesExpanded, setMemoriesExpanded) = rememberSaveable {
    mutableStateOf(true)
  }
  val (likesExpanded, setLikesExpanded) = rememberSaveable {
    mutableStateOf(true)
  }
  val (interactionExpanded, setInteractionExpanded) = rememberSaveable {
    mutableStateOf(true)
  }

  LazyColumn(modifier = modifier) {
    FolderItem(
      isExpanded = memoriesExpanded,
      setExpanded = setMemoriesExpanded,
      content = memoryFolder,
      onItemClick = onItemClick,
      textResId = R.string.memory,
      imageResId = CommonR.drawable.ic_work_history,
      onIconClick = onIconClick
    )
    FolderItem(
      isExpanded = likesExpanded,
      setExpanded = setLikesExpanded,
      content = likesFolder,
      onItemClick = onItemClick,
      textResId = R.string.likes,
      imageResId = CommonR.drawable.ic_like_small,
      onIconClick = onIconClick
    )
    FolderItem(
      isExpanded = interactionExpanded,
      setExpanded = setInteractionExpanded,
      content = interactionFolder,
      onItemClick = onItemClick,
      textResId = R.string.Interaction,
      imageResId = CommonR.drawable.ic_interaction,
      onIconClick = onIconClick
    )
  }
}

fun LazyListScope.FolderItem(
  isExpanded: Boolean,
  setExpanded: (Boolean) -> Unit,
  content: List<FolderChat>,
  onItemClick: (Long) -> Unit,
  onIconClick: (String?) -> Unit,
  textResId: Int,
  imageResId: Int,
) {
  if (content.isNotEmpty()) {
    item {
      FolderHeader(
        isExpanded = isExpanded,
        text = stringResource(id = textResId, content.size),
        painter = painterResource(id = imageResId),
        onClick = { setExpanded(!isExpanded) }
      )
    }

    if (isExpanded) {
      items(content) {
        ChatItem(chat = it, onClick = onItemClick, onIconClick = onIconClick)
      }
    }
  }
}

@Composable
fun FolderHeader(
  isExpanded: Boolean,
  text: String,
  painter: Painter,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    Icon(
      painter = painter,
      contentDescription = null,
      modifier = Modifier.size(LocalSize.current.small)
    )
    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = text,
      style = MaterialTheme.typography.subtitle2
    )

    Spacer(modifier = Modifier.weight(1f))

    Icon(
      painter = painterResource(id = if (isExpanded) CommonR.drawable.ic_arrow_up else CommonR.drawable.ic_arrow_down),
      contentDescription = null
    )
  }
}