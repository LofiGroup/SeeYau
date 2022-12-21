package com.lofigroup.core.util

fun String.getIndexOrEmpty(index: Int): String {
  return if (index >= length) ""
  else get(index).toString()
}