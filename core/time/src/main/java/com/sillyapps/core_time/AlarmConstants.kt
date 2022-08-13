package com.sillyapps.core_time

object AlarmConstants {
  const val mo = 0b00000001
  const val tu = 0b00000010
  const val we = 0b00000100
  const val th = 0b00001000
  const val fr = 0b00010000
  const val sa = 0b00100000
  const val su = 0b01000000

  const val outOfTheWeek = 0b10000000

  const val everyDay = 0b01111111
  const val onlyOnce = 0b00000000

  const val ONE_DAY_DURATION = 86400000
}