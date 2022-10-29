package com.sillyapps.core.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
  val veryLarge: Dp = 120.dp,
  val large: Dp = 80.dp,
  val medium: Dp = 44.dp,
  val small: Dp = 24.dp,
  val verySmall: Dp = 14.dp
)

val LocalSize = compositionLocalOf {
  Size()
}
