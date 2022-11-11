package com.sillyapps.core_time

fun shouldUpdate(time: Long?, interval: Long): Boolean {
  if (time == null) return true

  if (time < System.currentTimeMillis() - interval) {
    return true
  }
  return false
}