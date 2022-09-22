package com.sillyapps.core.ui.util

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ActivityBarHeights(
  val statusBarHeight: Dp = 0.dp,
  val navigationBarHeight: Dp = 0.dp
)

fun Activity.getActivityBarHeights() =
  ActivityBarHeights(
    statusBarHeight = getStatusBarHeight(),
    navigationBarHeight = getNavigationBarHeight()
  )

fun Activity.getNavigationBarHeight(): Dp {
  val resId =
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape"
  val id = resources.getIdentifier(resId, "dimen", "android")
  if (id > 0) {
    return pxToDp(resources.getDimensionPixelSize(id))
  }
  return 0.dp
}

fun Activity.getStatusBarHeight(): Dp {
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
  val px = if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else Rect().apply { window.decorView.getWindowVisibleDisplayFrame(this) }.top
  return pxToDp(px)
}