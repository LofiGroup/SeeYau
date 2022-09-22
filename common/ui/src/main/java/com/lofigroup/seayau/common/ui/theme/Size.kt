package com.lofigroup.seayau.common.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
  val userIconSize: Dp = 80.dp,
  val iconSize: Dp = 44.dp
)

val LocalSize = compositionLocalOf {
  Size()
}
