package com.lofigroup.seayau.common.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sillyapps.core.ui.theme.ExtendedColors
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.PurpleGradient
import com.sillyapps.core.ui.theme.PurpleVerticalGradient

val Purple = Color(0xFFEB00EB)
val PurpleDarker = Color(0xFFC500C5)
val PurpleBright = Color(0xFFF02FF4)
val SkyBlue = Color(0xFF0092E3)
val SkyBlueVariant = Color(0XFF3CD0FF)
val Red = Color(0xFFFF2222)
val RedVariant = Color(0xFFFF2D55)
val DarkViolet = Color(0xFF5D29A1)

val BluePurpleGradient = Brush.horizontalGradient(listOf(SkyBlueVariant, PurpleBright))

val PurpleRedGradient = Brush.horizontalGradient(listOf(Purple, RedVariant))
val PurpleRedTransparentGradient = Brush.horizontalGradient(
  listOf(
    Purple,
    RedVariant.copy(alpha = 0.01f)
  ),
)

val PurpleVioletVerticalGradient = Brush.verticalGradient(
  listOf(
    PurpleDarker.copy(alpha = 0.5f),
    DarkViolet.copy(alpha = 0.01f)
  )
)
val PurpleRedVerticalGradient = Brush.verticalGradient(
  listOf(
    RedVariant, Purple.copy(alpha = 0.1f)
  )
)


val Gray = Color(0xFFA3A3A3)
val DarkGray = Color(0xFF616161)
val DarkerGray = Color(0xFF282726)
val DarkestGray = Color(0xFF1C1C1E)

fun extendedColors(secondaryAsBrush: Brush) = ExtendedColors(
  disabled = DarkGray,
  primaryGradient = BluePurpleGradient,
  secondaryGradient = PurpleRedGradient,
  secondaryVerticalGradient = PurpleRedVerticalGradient,
  secondaryTransparentGradient = PurpleRedTransparentGradient,
  backgroundGradient = PurpleVioletVerticalGradient,
  lightBackground = DarkerGray,
  darkBackground = DarkestGray,
  secondaryAsBrush = secondaryAsBrush
)

@Preview
@Composable
fun PreviewPrimaryGradient() {
  AppTheme {
    HorizontalGradient(brush = LocalExtendedColors.current.primaryGradient)
  }
}

@Preview
@Composable
fun PreviewSecondaryGradient() {
  AppTheme {
    HorizontalGradient(brush = LocalExtendedColors.current.secondaryGradient)
  }
}

@Preview
@Composable
fun PreviewSecondaryTransparentGradient() {
  AppTheme {
    HorizontalGradient(brush = LocalExtendedColors.current.secondaryTransparentGradient)
  }
}

@Preview
@Composable
fun PreviewBackgroundGradient() {
  AppTheme {
    FullScreenGradient(brush = LocalExtendedColors.current.backgroundGradient)
  }
}

@Preview
@Composable
fun PreviewSecondaryVerticalGradient() {
  AppTheme {
    VerticalGradient(brush = LocalExtendedColors.current.secondaryVerticalGradient)
  }
}

@Composable
private fun HorizontalGradient(
  brush: Brush
) {
  Box(
    Modifier
      .fillMaxWidth()
      .height(100.dp)
      .background(brush)
  ) {

  }
}

@Composable
private fun VerticalGradient(
  brush: Brush
) {
  Box(
    Modifier
      .fillMaxHeight()
      .width(100.dp)
      .background(brush)
  ) {

  }
}

@Composable
private fun FullScreenGradient(
  brush: Brush
) {
  Box(
    Modifier
      .fillMaxSize()
      .background(brush)
  ) {

  }
}