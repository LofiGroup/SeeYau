package com.lofigroup.seeyau.common.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class IconsSize(
  val medium: Dp = 42.dp
)

val LocalIconsSize = compositionLocalOf {
  IconsSize()
}