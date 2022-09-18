package com.lofigroup.core.util

fun getFileExtFromPath(path: String): String {
  val startIndex = path.lastIndexOf(".")
  if (startIndex == -1) return ""

  return path.substring(startIndex + 1, path.length)
}