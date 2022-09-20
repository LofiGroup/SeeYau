package com.sillyapps.core.ui.util

import android.app.Activity
import android.graphics.Rect

fun Activity.getStatusBarHeight(): Int {
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
  return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
  else Rect().apply { window.decorView.getWindowVisibleDisplayFrame(this) }.top
}