package com.lofigroup.seeyau.features.chat.ui.providers

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lofigroup.seeyau.common.ui.theme.SkyBlue
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.Purple
import com.sillyapps.core.ui.theme.PurpleDarker

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

@Composable
fun ChatMessageStyleProvider(
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalChatMessageStyles provides ChatMessageStyles(
      myMessageStyleDefault.copy(color = MaterialTheme.colors.primaryVariant),
      partnerMessageStyleDefault.copy(color = LocalExtendedColors.current.itemsColors)
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
  color = Color.DarkGray,
  datePadding = 35.dp,
  hasMessageMark = true
)

private val partnerMessageStyleDefault = ChatMessageStyle(
  alignment = Alignment.CenterStart,
  startPadding = 16.dp,
  endPadding = 64.dp,
  color = Color.Red,
  datePadding = 35.dp,
  hasMessageMark = false
)

