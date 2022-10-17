package com.lofigroup.core.util

import org.junit.Test

class TestUtilFunctions {

  @Test
  fun test_AddToOrderedList() {
    val list = listOf(10, 9, 8, 6, 5, 4, 3, 2, 1)

    val itemToAdd = 7

    val output = list.addToOrderedDesc(itemToAdd) { it.toLong() }
    val expected = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)

    print("Output:\n$output\nExpected:\n$expected")
    assert(output == expected)
  }

}