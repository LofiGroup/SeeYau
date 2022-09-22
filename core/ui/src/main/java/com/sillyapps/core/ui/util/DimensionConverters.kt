package com.sillyapps.core.ui.util

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Context.pxToDp(px: Int): Dp {
  return (px / resources.displayMetrics.density).dp
}