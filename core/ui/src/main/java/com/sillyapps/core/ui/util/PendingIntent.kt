package com.sillyapps.core.ui.util

import android.app.PendingIntent
import android.os.Build

fun getCompatPendingIntentFlags(): Int {
  var piFlags = PendingIntent.FLAG_UPDATE_CURRENT
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    piFlags = piFlags or PendingIntent.FLAG_IMMUTABLE
  }
  return piFlags
}