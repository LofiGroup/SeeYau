package com.lofigroup.seayau.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.sillyapps.core.ui.theme.*

private val DarkColorPalette = darkColors(
  primary = SkyBlue,
  primaryVariant = SkyBlue,
  secondary = SkyBlue,
  error = Red,
  background = Color.Black
)

@Composable
fun AppTheme(
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalExtendedColors provides extendedColors(SolidColor(MaterialTheme.colors.secondary)),
    LocalSpacing provides Spacing(),
    LocalIconsSize provides IconsSize(),
    LocalSize provides Size()
  ) {
    MaterialTheme(
      colors = DarkColorPalette,
      typography = Typography,
      shapes = Shapes,
      content = content
    )
  }
}