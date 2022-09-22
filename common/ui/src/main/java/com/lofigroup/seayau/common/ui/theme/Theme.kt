package com.lofigroup.seayau.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.sillyapps.core.ui.util.ActivityBarHeights

private val DarkColorPalette = darkColors(
  primary = Purple,
  primaryVariant = PurpleDarker,
  secondary = SkyBlue,
  error = Red
)

@Composable
fun AppTheme(
  activityBarHeights: ActivityBarHeights = ActivityBarHeights(),
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalExtendedColors provides ExtendedColors(),
    LocalSpacing provides Spacing(),
    LocalIconsSize provides IconsSize(),
    LocalSize provides Size(),
    LocalActivityBarHeights provides activityBarHeights
  ) {
    MaterialTheme(
      colors = DarkColorPalette,
      typography = Typography,
      shapes = Shapes,
      content = content
    )
  }
}