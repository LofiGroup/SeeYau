package com.lofigroup.seayau.common.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.sillyapps.core.ui.theme.ExtendedColors

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

val extendedColors = ExtendedColors(
  disabled = DarkGray,
  primaryGradient = BluePurpleGradient,
  secondaryGradient = PurpleGradient,
  backgroundGradient = PurpleVerticalGradient,
  lightBackground = DarkerGray,
  darkBackground = DarkestGray
)