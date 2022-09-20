package com.lofigroup.seayau.common.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class IconsSize(
  val medium: Dp = 42.dp
)

val LocalIconsSize = compositionLocalOf {
  IconsSize()
}