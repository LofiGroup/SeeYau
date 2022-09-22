package com.lofigroup.seayau.common.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Purple = Color(0xFFEB00EB)
val PurpleDarker = Color(0xFFC500C5)
val PurpleBright = Color(0xFFF02FF4)
val SkyBlue = Color(0xFF0092E3)
val SkyBlueVariant = Color(0XFF3CD0FF)
val Red = Color(0xFFFF2222)

val BluePurpleGradient = Brush.horizontalGradient(listOf(SkyBlueVariant, PurpleBright))
val PurpleGradient = Brush.horizontalGradient(listOf(Purple, PurpleDarker))
val PurpleVerticalGradient = Brush.verticalGradient(listOf(PurpleDarker, Color.Black))

val Gray = Color(0xFFA3A3A3)
val DarkGray = Color(0xFF616161)
val DarkerGray = Color(0x18181880)

data class ExtendedColors(
  val disabled: Color = DarkGray,
  val primaryGradient: Brush = BluePurpleGradient,
  val secondaryGradient: Brush = PurpleGradient,
  val backgroundGradient: Brush = PurpleVerticalGradient,
  val lightBackground: Color = DarkerGray
)

val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors()
}

@Preview
@Composable
fun PreviewBluePurpleGradient() {
  Box(Modifier
    .fillMaxWidth()
    .height(100.dp)
    .background(BluePurpleGradient)
  ) {

  }
}

@Preview
@Composable
fun PreviewPurpleGradient() {
  Box(Modifier
    .fillMaxWidth()
    .height(100.dp)
    .background(PurpleGradient)
  ) {

  }
}

@Preview
@Composable
fun PreviewBackgroundGradient() {
  Box(Modifier
    .fillMaxSize()
    .background(PurpleVerticalGradient)
  ) {

  }
}