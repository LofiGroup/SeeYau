package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.dpToPx
import com.sillyapps.core.ui.util.pxToDp
import com.sillyapps.core_time.getLocalTimeFromMillis
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun ChatMessageItem(
  chatMessage: PrivateMessage
) {
  val style =
    if (chatMessage.authorIsMe) ChatMessageStyle.MyMessageStyle(SolidColor(MaterialTheme.colors.secondary))
    else ChatMessageStyle.PartnerMessage(LocalExtendedColors.current.secondaryGradient)

  var labelSize by remember {
    mutableStateOf(IntSize(
      width = if (chatMessage.authorIsMe) 150 else 80,
      height = 64
    ))
  }

  var parentWidth by remember {
    mutableStateOf(0)
  }

  var isSingleLine by remember {
    mutableStateOf(true)
  }

  var labelFit by remember {
    mutableStateOf(true)
  }

  val context = LocalContext.current
  val mediumSpacing = context.dpToPx(LocalSpacing.current.medium)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = style.startPadding, end = style.endPadding)
      .padding(bottom = LocalSpacing.current.medium)
      .onSizeChanged {
        parentWidth = it.width - mediumSpacing
      }
  ) {
    Box(
      modifier = Modifier
        .clip(MaterialTheme.shapes.large)
        .background(style.brush)
        .padding(
          vertical = LocalSpacing.current.extraSmall,
          horizontal = LocalSpacing.current.small
        )
        .align(style.alignment)
    ) {
      val labelPadding = if (isSingleLine) context.pxToDp(labelSize.width) else 0.dp
      Text(
        text = chatMessage.message,
        style = MaterialTheme.typography.body1,
        onTextLayout = { result ->
          isSingleLine = result.lineCount == 1
          val lastLineEndCoord = result.getLineRight(result.lineCount-1)

          labelFit = parentWidth - lastLineEndCoord > labelSize.width
        },
        modifier = Modifier
          .padding(
            end = LocalSpacing.current.extraSmall + labelPadding,
            bottom = if (labelFit) 0.dp else context.pxToDp(labelSize.height)
          )
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .onSizeChanged { size ->
            labelSize = size
          }
      ) {
        Text(
          text = getLocalTimeFromMillis(chatMessage.createdIn),
          style = MaterialTheme.typography.caption,
        )

        if (chatMessage.authorIsMe) {
          Spacer(modifier = Modifier.width(2.dp))
          Image(
            painter = painterResource(id = if (chatMessage.isRead) CommonR.drawable.ic_check_mark_read else CommonR.drawable.ic_check_mark_received),
            contentDescription = null
          )
        }
      }
    }
  }
}

sealed class ChatMessageStyle(
  val brush: Brush,
  val alignment: Alignment,
  val startPadding: Dp,
  val endPadding: Dp
) {
  class MyMessageStyle(
    brush: Brush
  ) : ChatMessageStyle(
    brush = brush,
    alignment = Alignment.CenterEnd,
    startPadding = 32.dp,
    endPadding = 16.dp
  )

  class PartnerMessage(brush: Brush) : ChatMessageStyle(
    brush = brush,
    alignment = Alignment.CenterStart,
    startPadding = 16.dp,
    endPadding = 32.dp
  )
}

@Preview
@Composable
fun ChatMyMessagePreview() {
  AppTheme() {
    Surface() {
      ChatMessageItem(
        chatMessage = PrivateMessage(
          id = 0,
          message = "Hello! Hello!Hello!Hello!",
          authorIsMe = true,
          createdIn = 0L,
          isRead = true
        )
      )
    }
  }
}

@Preview
@Composable
fun ChatPartnerMessagePreview() {
  AppTheme() {
    Surface() {
      ChatMessageItem(
        chatMessage = PrivateMessage(
          id = 0,
          message = "Hello!Hello!Hello!Hello!Hello!",
          authorIsMe = false,
          createdIn = 0L,
          isRead = true
        )
      )
    }
  }
}
