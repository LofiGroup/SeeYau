package com.lofigroup.seeyau.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.sillyapps.core.ui.theme.*

@Composable
fun AppTheme(
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalExtendedColors provides extendedColors(),
    LocalSpacing provides Spacing(),
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