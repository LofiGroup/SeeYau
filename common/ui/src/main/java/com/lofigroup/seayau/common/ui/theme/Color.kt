package com.lofigroup.seayau.common.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Purple = Color(0xFFEB00EB)
val PurpleDarker = Color(0xFFC500C5)
val PurpleDarkest = Color(0xFF7A04D8)
val SkyBlue = Color(0xFF0092E3)
val Red = Color(0xFFFF2222)

val PurpleGradient = Brush.horizontalGradient(listOf(PurpleDarkest, PurpleDarker))

val Gray = Color(0xFFA3A3A3)
val DarkGray = Color(0xFF616161)

data class ExtendedColors(
  val disabled: Color= Gray,
  val onDisabled: Color = Color.Black,
  val primaryGradient: Brush = PurpleGradient
)

val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors()
}

@Preview
@Composable
fun PreviewPrimaryGradient() {
  Box(Modifier
    .size(50.dp, 20.dp)
    .background(PurpleGradient)
  ) {

  }
}