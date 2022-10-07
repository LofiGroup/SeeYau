package com.lofigroup.core.util

fun getFileExtFromPath(path: String): String {
  val startIndex = path.lastIndexOf(".")
  if (startIndex == -1) return ""

  return path.substring(startIndex + 1, path.length)
}

fun<T> List<T>.transformItemAt(index: Int, transform: (T) -> T): List<T> {
  if (index > lastIndex) return this

  val mutableList = toMutableList()
  mutableList[index] = transform(mutableList[index])

  return mutableList
}