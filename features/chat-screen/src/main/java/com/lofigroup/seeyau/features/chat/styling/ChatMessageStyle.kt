package com.lofigroup.seeyau.features.chat.styling

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sillyapps.core.ui.theme.LocalExtendedColors

data class ChatMessageStyles(
  val myMessageStyle: ChatMessageStyle,
  val partnerMessageStyle: ChatMessageStyle
)

data class ChatMessageStyle(
  val brush: Brush,
  val alignment: Alignment,
  val startPadding: Dp,
  val endPadding: Dp
)

@Composable
fun ChatMessageStyleProvider(
  myMessageStyleBrush: Brush = SolidColor(MaterialTheme.colors.secondary),
  partnerMessageStyleBrush: Brush = LocalExtendedColors.current.secondaryGradient,
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalChatMessageStyles provides ChatMessageStyles(
      myMessageStyleDefault.copy(brush = myMessageStyleBrush),
      partnerMessageStyleDefault.copy(brush = partnerMessageStyleBrush)
    )
  ) {
    content()
  }
}

val LocalChatMessageStyles = staticCompositionLocalOf {
  ChatMessageStyles(
    myMessageStyle = myMessageStyleDefault,
    partnerMessageStyle = partnerMessageStyleDefault
  )
}

private val myMessageStyleDefault = ChatMessageStyle(
  alignment = Alignment.CenterEnd,
  startPadding = 32.dp,
  endPadding = 16.dp,
  brush = Brush.linearGradient()
)

private val partnerMessageStyleDefault = ChatMessageStyle(
  alignment = Alignment.CenterStart,
  startPadding = 16.dp,
  endPadding = 32.dp,
  brush = Brush.linearGradient()
)

