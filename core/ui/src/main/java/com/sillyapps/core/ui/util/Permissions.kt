package com.sillyapps.core.ui.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
  for (permission in permissions) {
    if (ContextCompat.checkSelfPermission(context, permission)
      != PackageManager.PERMISSION_GRANTED
    ) {
      return false
    }
  }
  return true
}
