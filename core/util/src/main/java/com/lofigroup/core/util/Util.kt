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

fun <T> List<T>.addToOrderedDesc(
  item: T?,
  getComparable: (T) -> Long
): List<T> {
  if (item == null) return this

  val itemComparable = getComparable(item)

  val mutableList = toMutableList()
  for (i in 0 until mutableList.size) {
    val comparable = getComparable(mutableList[i])
    if (comparable < itemComparable) {
      mutableList.add(i, item)
      return mutableList
    }
  }

  mutableList.add(item)
  return mutableList
}