package com.sillyapps.core.ui.util

import android.content.Context
import android.content.pm.PackageManager

fun isBleSupported(context: Context): Boolean {
  return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
}