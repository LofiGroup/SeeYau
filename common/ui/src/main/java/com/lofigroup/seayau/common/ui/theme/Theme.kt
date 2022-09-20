package com.lofigroup.seayau.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorPalette = darkColors(
  primary = SkyBlue,
  primaryVariant = SkyBlue,
  secondary = Purple,
  error = Red
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  CompositionLocalProvider(
    LocalExtendedColors provides ExtendedColors(),
    LocalSpacing provides Spacing(),
    LocalIconsSize provides IconsSize()
  ) {
    MaterialTheme(
      colors = DarkColorPalette,
      typography = Typography,
      shapes = Shapes,
      content = content
    )
  }
}