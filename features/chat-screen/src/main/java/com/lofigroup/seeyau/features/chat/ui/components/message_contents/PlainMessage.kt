package com.lofigroup.seeyau.features.chat.ui.components.message_contents

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.features.chat.model.UIChatMessage
import com.lofigroup.seeyau.features.chat.ui.composition_locals.ChatMessageStyle
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun PlainMessage(
  message: UIChatMessage,
  style: ChatMessageStyle,
  modifier: Modifier = Modifier,
  maxLines: Int = Int.MAX_VALUE
) {
  Box(
    modifier = modifier
      .padding(vertical = LocalSpacing.current.small, horizontal = 10.dp)
  ) {
    Text(
      text = message.message,
      style = MaterialTheme.typography.body1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .padding(end = LocalSpacing.current.extraSmall + style.datePadding)
        .align(Alignment.TopCenter),
      maxLines = maxLines
    )

    MessageDate(
      message = message,
      modifier = Modifier.align(Alignment.BottomEnd)
    )
  }

}

@Composable
fun MessageDate(
  message: UIChatMessage,
  modifier: Modifier = Modifier
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
  ) {
    Text(
      text = message.dateTime.time,
      style = MaterialTheme.typography.body1,
    )
  }
}