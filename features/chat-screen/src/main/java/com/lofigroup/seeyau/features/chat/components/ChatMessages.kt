package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.lastVisibleItemKey
import kotlinx.coroutines.delay

@Composable
fun ColumnScope.ChatMessages(
  items: List<PrivateMessage>
) {
  val listState = rememberLazyListState()

  Box(modifier = Modifier
    .weight(1f)) {
    LazyColumn(
      reverseLayout = true,
      state = listState
    ) {
      items.groupBy { it.date }.forEach { (date, messages) ->
        items(
          items = messages,
          key = { it.positionInList },
          contentType = { "chatMessageItem" }
        ) { message ->
          ChatMessageItem(
            chatMessage = message
          )
        }
        item(
          key = date,
          contentType = "dateHeader"
        ) {
          DateHeader(date = date)
        }
      }
    }

    val lastItemKey = listState.lastVisibleItemKey()
    val item = if (lastItemKey is Int)
      items[lastItemKey]
    else
      null


    DateStickyLabel(
      item = item,
      scrollInProgress = listState.isScrollInProgress
    )
  }
}

@Composable
fun DateStickyLabel(
  item: PrivateMessage?,
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

  if (item != null && isVisible)
    DateHeader(date = item.date)
}

@Composable
fun DateHeader(date: String) {
  Text(
    text = date,
    style = MaterialTheme.typography.caption,
    textAlign = TextAlign.Center,
    modifier = Modifier
      .padding(horizontal = LocalSpacing.current.medium)
      .fillMaxWidth()
  )
}