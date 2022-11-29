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
import com.lofigroup.seayau.common.ui.theme.SkyBlue
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.Purple
import com.sillyapps.core.ui.theme.PurpleDarker

data class ChatMessageStyles(
  val myMessageStyle: ChatMessageStyle,
  val partnerMessageStyle: ChatMessageStyle
)

data class ChatMessageStyle(
  val brush: Brush,
  val alignment: Alignment,
  val startPadding: Dp,
  val endPadding: Dp,
  val datePadding: Dp,
  val hasMessageMark: Boolean
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
  startPadding = 64.dp,
  endPadding = 16.dp,
  brush = SolidColor(SkyBlue),
  datePadding = 50.dp,
  hasMessageMark = true
)

private val partnerMessageStyleDefault = ChatMessageStyle(
  alignment = Alignment.CenterStart,
  startPadding = 16.dp,
  endPadding = 64.dp,
  brush = Brush.horizontalGradient(listOf(Purple, PurpleDarker)),
  datePadding = 30.dp,
  hasMessageMark = false
)

