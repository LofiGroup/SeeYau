package com.lofigroup.seeyau.features.chat.ui.composition_locals

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChatMessageStyles(
  val myMessageStyle: ChatMessageStyle,
  val partnerMessageStyle: ChatMessageStyle
)

data class ChatMessageStyle(
  val color: Color,
  val alignment: Alignment,
  val startPadding: Dp,
  val endPadding: Dp,
  val datePadding: Dp,
  val hasMessageMark: Boolean
)

val LocalChatMessageStyles = staticCompositionLocalOf {
  ChatMessageStyles(
    myMessageStyle = myMessageStyleDefault,
    partnerMessageStyle = partnerMessageStyleDefault
  )
}

val myMessageStyleDefault = ChatMessageStyle(
  alignment = Alignment.CenterEnd,
  startPadding = 64.dp,
  endPadding = 16.dp,
  color = Color.DarkGray,
  datePadding = 35.dp,
  hasMessageMark = true
)

val partnerMessageStyleDefault = ChatMessageStyle(
  alignment = Alignment.CenterStart,
  startPadding = 16.dp,
  endPadding = 64.dp,
  color = Color.Red,
  datePadding = 35.dp,
  hasMessageMark = false
)

