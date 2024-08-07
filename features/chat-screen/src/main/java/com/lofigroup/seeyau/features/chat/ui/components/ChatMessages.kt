package com.lofigroup.seeyau.features.chat.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.media_player.ui.LocalMediaPlayer
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.ui.providers.ChatMessageStyleProvider
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.rememberLastItemKey
import kotlinx.coroutines.delay

@Composable
fun ChatMessages(
  listState: LazyListState,
  items: Map<String, List<UIChatMessage>>,
  onImageClick: (String) -> Unit,
  onVideoClick: (UIMessageType.Video) -> Unit
) {

  ChatMessageStyleProvider() {
    Box(modifier = Modifier
      .fillMaxSize()) {
      LazyColumn(
        reverseLayout = true,
        state = listState,
        modifier = Modifier.fillMaxSize()
      ) {
        items.forEach { (date, messages) ->
          items(
            items = messages,
            key = { "${date}_${it.id}" },
            contentType = { "chatMessageItem" }
          ) { message ->
            ChatMessageItem(
              chatMessage = message,
              onImageClick = onImageClick,
              onVideoClick = onVideoClick
            )
          }
          item(
            contentType = "dateHeader"
          ) {
            DateHeader(date = date)
          }
        }
      }

      val lastItemKey by listState.rememberLastItemKey()
      val key = lastItemKey
      val date = if (key is String)
        key.split("_")[0]
      else
        null

      DateStickyLabel(
        date = date,
        scrollInProgress = listState.isScrollInProgress
      )
    }
  }
}

@Composable
fun DateStickyLabel(
  date: String?,
  scrollInProgress: Boolean
) {
  var isVisible by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(scrollInProgress) {
    isVisible = if (scrollInProgress)
      true
    else {
      delay(1000L)
      false
    }
  }

  if (date != null && isVisible)
    DateHeader(date = date)
}

@Composable
fun DateHeader(date: String) {
  Text(
    text = date,
    style = MaterialTheme.typography.caption,
    textAlign = TextAlign.Center,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium)
      .padding(bottom = LocalSpacing.current.small)
      .fillMaxWidth()
  )
}