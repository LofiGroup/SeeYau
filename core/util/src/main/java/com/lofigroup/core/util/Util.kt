package com.lofigroup.core.util

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

fun <T> List<T>.splitInTwo(
  shouldBeInMainList: (T) -> Boolean
): Pair<List<T>, List<T>> {
  val mainList = mutableListOf<T>()
  val otherList = mutableListOf<T>()

  for (item in this) {
    if (shouldBeInMainList(item))
      mainList.add(item)
    else
      otherList.add(item)
  }

  return Pair(mainList, otherList)
}

fun IntRange.toIntArray(): IntArray {
  if (last < first)
    return IntArray(0)

  val result = IntArray(last - first + 1)
  var index = 0
  for (element in this)
    result[index++] = element
  return result
}

fun<T> List<T>.indexOfFirstFrom(
  index: Int,
  isRightItem: (T) -> Boolean
): Int {
  for (i in index until size) {
    if (isRightItem(get(i))) return i
  }
  return size
}