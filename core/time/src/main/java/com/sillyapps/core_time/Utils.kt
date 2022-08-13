package com.sillyapps.core_time

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun getRemainingTime(activeDays: Int, time: Long, from: Long = System.currentTimeMillis()): Long {
  val fromLocalDateTime = Instant.ofEpochMilli(from).atZone(ZoneId.systemDefault()).toLocalDateTime()
  val now = fromLocalDateTime.toMillisAfterStartOfTheDay()

  if (activeDays == AlarmConstants.onlyOnce) {
    if (time >= now) {
      return time - now
    }
    return time - now + AlarmConstants.ONE_DAY_DURATION
  }

  var day = getDayMask(fromLocalDateTime.dayOfWeek)
  var dayCount = 0

  // Если будильник поставлен на сегодня, но время уже прошло
  if (day and activeDays != 0 && time <= now) {
    day = day shl 1
    dayCount++

    if (day == AlarmConstants.outOfTheWeek)
      day = AlarmConstants.mo
  }

  while (day and activeDays == 0) {
    day = day shl 1
    dayCount++

    if (day == AlarmConstants.outOfTheWeek)
      day = AlarmConstants.mo
  }

  return time - now + dayCount * AlarmConstants.ONE_DAY_DURATION
}

fun getDayMask(day: DayOfWeek): Int {
  return when(day) {
    DayOfWeek.MONDAY -> AlarmConstants.mo
    DayOfWeek.TUESDAY -> AlarmConstants.tu
    DayOfWeek.WEDNESDAY -> AlarmConstants.we
    DayOfWeek.THURSDAY -> AlarmConstants.th
    DayOfWeek.FRIDAY -> AlarmConstants.fr
    DayOfWeek.SATURDAY -> AlarmConstants.sa
    DayOfWeek.SUNDAY -> AlarmConstants.su
  }
}

fun getMillisAfterStartOfTheDay(): Long {
  return LocalDateTime.now().toMillisAfterStartOfTheDay()
}

fun LocalDateTime.toMillisAfterStartOfTheDay(): Long {
  return convertToMillis(hour, minute, second)
}