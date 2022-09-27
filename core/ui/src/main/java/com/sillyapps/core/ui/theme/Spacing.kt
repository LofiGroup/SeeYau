package com.sillyapps.core.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sillyapps.core.ui.util.ActivityBarHeights

data class Spacing(
  val default: Dp = 0.dp,
  val extraSmall: Dp = 4.dp,
  val small: Dp = 8.dp,
  val medium: Dp = 16.dp,
  val bigMedium: Dp = 24.dp,
  val large: Dp = 32.dp,
  val extraLarge: Dp = 64.dp,
)

val LocalSpacing = compositionLocalOf {
  Spacing()
}

val LocalActivityBarHeights = compositionLocalOf {
  ActivityBarHeights()
}

fun Modifier.applyActivityBarPaddings(): Modifier = composed {
  padding(
    top = LocalActivityBarHeights.current.statusBarHeight,
    bottom = LocalActivityBarHeights.current.navigationBarHeight
  )
}