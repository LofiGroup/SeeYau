package com.sillyapps.core_network.utils

import android.content.ContentResolver
import android.net.Uri

fun ContentResolver.canRead(uri: Uri): Boolean {
  return try {
    val descriptor = openAssetFileDescriptor(uri, "r")
    descriptor?.close()
    true
  }
  catch (e: Exception) {
    false
  }
}