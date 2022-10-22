package com.sillyapps.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

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
val DarkerGray = Color(0xFF282726)
val DarkestGray = Color(0xFF1C1C1E)

data class ExtendedColors(
  val disabled: Color = DarkGray,

  val primaryGradient: Brush = BluePurpleGradient,

  val secondaryGradient: Brush = PurpleGradient,
  val secondaryVerticalGradient: Brush = PurpleGradient,
  val secondaryTransparentGradient: Brush = PurpleGradient,

  val backgroundGradient: Brush = PurpleVerticalGradient,

  val lightBackground: Color = DarkerGray,
  val darkBackground: Color = DarkestGray,

  val secondaryAsBrush: Brush
)


val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors(secondaryAsBrush = SolidColor(SkyBlueVariant))
}