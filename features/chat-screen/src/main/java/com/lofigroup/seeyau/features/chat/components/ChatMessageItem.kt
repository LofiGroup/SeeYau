package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.features.chat.model.PrivateMessage
import org.intellij.lang.annotations.JdkConstants

@Composable
fun BoxScope.ChatMessageItem(
  chatMessage: PrivateMessage
) {
  val style =
    if (chatMessage.authorIsMe) ChatMessageStyle.myMessageStyle
    else ChatMessageStyle.partnerMessageStyle

  Surface(
    color = style.color,
    shape = MaterialTheme.shapes.small,
    modifier = Modifier
      .padding(12.dp)
      .align(style.alignment)
  ) {
    Text(
      text = chatMessage.message,
      textAlign = style.textAlign,
      modifier = Modifier
        .padding(8.dp)
    )
  }

}

class ChatMessageStyle(val color: Color, val textAlign: TextAlign, val alignment: Alignment) {
  companion object {
    val myMessageStyle = ChatMessageStyle(
      color = Color.Cyan.copy(alpha = 0.7f),
      textAlign = TextAlign.End,
      alignment = Alignment.CenterEnd
    )
    val partnerMessageStyle = ChatMessageStyle(
      color = Color.Gray.copy(alpha = 0.7f),
      textAlign = TextAlign.Start,
      alignment = Alignment.CenterStart
    )
  }
}

@Preview
@Composable
fun ChatMessagePreview() {
  AppTheme() {
    Box(
      modifier = Modifier.fillMaxWidth()
    ) {
      ChatMessageItem(
        chatMessage = PrivateMessage(
          id = 0,
          message = "Hello!",
          authorIsMe = true,
          createdIn = 0L
        )
      )
    }
  }

}
