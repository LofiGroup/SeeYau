package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.theme.LocalActivityBarHeights
import com.lofigroup.seayau.common.ui.theme.LocalExtendedColors
import com.lofigroup.seayau.common.ui.theme.LocalSpacing
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import com.sillyapps.core_time.getLocalTimeFromMillis
import org.intellij.lang.annotations.JdkConstants

@Composable
fun ChatMessageItem(
  chatMessage: PrivateMessage
) {
  val style = if (chatMessage.authorIsMe) ChatMessageStyle.MyMessageStyle(color = MaterialTheme.colors.secondary)
    else ChatMessageStyle.PartnerMessage(LocalExtendedColors.current.secondaryGradient)

  Box(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier
        .applyChatMessageStyle(style)
        .align(style.alignment)
    ) {
      Text(
        text = chatMessage.message,
        textAlign = style.textAlign,
        style = MaterialTheme.typography.body1,
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = getLocalTimeFromMillis(chatMessage.createdIn),
          style = MaterialTheme.typography.caption
        )

        if (chatMessage.authorIsMe) {
          Spacer(modifier = Modifier.width(2.dp))
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            modifier = Modifier
              .size(12.dp),
            tint = if (chatMessage.isRead) Color.Black.copy(alpha = 0.5f) else Color.LightGray
          )
        }
      }
    }
  }
}

sealed class ChatMessageStyle(
  val textAlign: TextAlign,
  val alignment: Alignment,
  val startPadding: Dp,
  val endPadding: Dp
) {
  class MyMessageStyle(
    val color: Color
  ) : ChatMessageStyle(
    textAlign = TextAlign.End,
    alignment = Alignment.CenterEnd,
    startPadding = 16.dp,
    endPadding = 8.dp
  )

  class PartnerMessage(
    val brush: Brush
  ): ChatMessageStyle(
    textAlign = TextAlign.Start,
    alignment = Alignment.CenterStart,
    startPadding = 8.dp,
    endPadding = 14.dp
  )
}

fun Modifier.applyChatMessageStyle(style: ChatMessageStyle): Modifier = composed {
  clip(MaterialTheme.shapes.large)
  when (style) {
    is ChatMessageStyle.MyMessageStyle -> background(style.color)
    is ChatMessageStyle.PartnerMessage -> background(style.brush)
  }
  padding(start = style.startPadding, end = style.endPadding)
  padding(vertical = 12.dp)

}

@Preview
@Composable
fun ChatMessagePreview() {
  AppTheme() {
    ChatMessageItem(
      chatMessage = PrivateMessage(
        id = 0,
        message = "Hello!",
        authorIsMe = true,
        createdIn = 0L,
        isRead = true
      )
    )
  }
}
