package com.lofigroup.features.navigator_screen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lofigroup.features.navigator_screen.R
import com.lofigroup.features.navigator_screen.model.PreviewMessage
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.components.ChatMessageItem
import com.lofigroup.seeyau.features.chat.components.DateHeader
import com.lofigroup.seeyau.features.chat.styling.ChatMessageStyleProvider
import com.lofigroup.seeyau.features.chat.styling.LocalChatMessageStyles
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.dpToPx

@Composable
fun UserNewMessages(
  selectedUser: UserItemUIModel,
  showChat: () -> Unit,
  onClick: (Long) -> Unit,
  modifier: Modifier = Modifier
) {

  val (isOverflowed, setIsOverflowed) = remember {
    mutableStateOf(false)
  }
  val (lastItemPos, setLastItemPos) = remember {
    mutableStateOf(-1)
  }

  ChatMessageStyleProvider(
    partnerMessageStyleBrush = LocalExtendedColors.current.secondaryTransparentGradient
  ) {
    if (selectedUser.messagesIsCollapsed) {
      Column(
        modifier = modifier
          .clickable { onClick(selectedUser.id) }
      ) {
        val lastItem = selectedUser.newMessages.getOrNull(lastItemPos)
        if (isOverflowed && lastItem != null) {
          DateHeader(date = lastItem.message.dateTime.date)
          OnOverflowLabel(messagesCount = selectedUser.newMessages.lastIndex - lastItemPos)
        }

        ConstrainedColumn(
          maxHeight = 200.dp,
          setOverflowed = setIsOverflowed,
          setLastItemPos = setLastItemPos
        ) {
          selectedUser.newMessagesMapped.forEach { (date, messages) ->
            for (message in messages)
              UserMessage(message = message)
            DateHeader(date = date)
          }
        }
      }
    }
    else if (selectedUser.hasNewMessages) {
      ControlItem(
        resId = R.drawable.ic_stm_1_icon,
        onClick = showChat,
        modifier = modifier.padding(start = LocalSpacing.current.medium)
      )
    }

  }
}

@Composable
fun ConstrainedColumn(
  maxHeight: Dp,
  setOverflowed: (Boolean) -> Unit,
  setLastItemPos: (Int) -> Unit,
  content: @Composable () -> Unit
) {
  val maxHeightPx = LocalContext.current.dpToPx(maxHeight)

  Layout(
    modifier = Modifier,
    content = content
  ) { measurables, constraints ->
    var height = 0

    val placeables = mutableListOf<Placeable>()
    for (measurable in measurables) {
      val placeable = measurable.measure(constraints)

      placeables.add(placeable)
      height += placeable.height

      if (height > maxHeightPx) {
        break
      }
    }

    if (measurables.size != placeables.size && height > maxHeightPx) {
      val removedItem = placeables.removeLast()
      height -= removedItem.height

      val lastMeasurableId = measurables[placeables.lastIndex].layoutId

      if (lastMeasurableId is Int) {
        setLastItemPos(lastMeasurableId)
      } else {
        placeables.removeLast()
        val id = measurables[placeables.lastIndex].layoutId as Int
        setLastItemPos(id)
      }
      setOverflowed(true)
    } else {
      setOverflowed(false)
      setLastItemPos(-1)
    }

    layout(constraints.maxWidth, height) {
      var yPosition = height

      placeables.forEach {
        yPosition -= it.height
        it.placeRelative(x = 0, y = yPosition)
      }
    }
  }
}

@Composable
fun OnOverflowLabel(
  messagesCount: Int
) {
  val style = LocalChatMessageStyles.current.partnerMessageStyle
  Box(modifier = Modifier
    .padding(start = style.startPadding, end = style.endPadding)
    .padding(bottom = LocalSpacing.current.small)
  ) {
    Row(
      modifier = Modifier
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colors.primary)
        .padding(vertical = LocalSpacing.current.small, horizontal = 10.dp)
    ) {
      Text(
        text = stringResource(id = R.string.more, messagesCount),
        style = MaterialTheme.typography.body1,
      )

      Image(painter = painterResource(id = R.drawable.ic_message), contentDescription = null)
    }
  }
}

@Composable
fun UserMessage(message: PreviewMessage) {
  ChatMessageItem(
    message.message,
    modifier = Modifier.layoutId(message.positionInList),
    maxLines = 4
  )
}

@Preview
@Composable
fun UserNewMessagesPreview() {
  AppTheme {
    Surface() {
      Box() {
        UserNewMessages(
          selectedUser = UserItemUIModel.getPreviewModel(
            newMessages = listOf(
              PreviewMessage.getPreviewModel(),
              PreviewMessage.getPreviewModel(),
              PreviewMessage.getPreviewModel(),
              PreviewMessage.getPreviewModel(),
              PreviewMessage.getPreviewModel(),
              PreviewMessage.getPreviewModel(),
            ),
            messagesIsCollapsed = true
          ),
          onClick = {},
          showChat = {}
        )
      }
    }
  }
}