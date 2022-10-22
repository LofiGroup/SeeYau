package com.sillyapps.core_time

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun getLocalTimeFromMillis(millis: Long): String {
  val time = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalTime()
  return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}


fun getLocalDateAndTimeFromMillis(millis: Long): DateAndTime {
  val dateTime = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())

  val timeFormatted = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

  val date = dateTime.toLocalDate()
  val now = LocalDate.now()
  val dateFormatted =
    when {
      date.isEqual(now) -> DateAndTime.TODAY
      date.isEqual(now.minusDays(1)) -> DateAndTime.YESTERDAY
      else -> {
        date.format(DateTimeFormatter.ofPattern("d MMM"))
      }
    }

  return DateAndTime(time = timeFormatted, date = dateFormatted)
}

fun millisToLastSeen(utcMillis: Long): LastSeen {
  if (utcMillis == Time.IS_ONLINE) return LastSeen.Online

  val period = System.currentTimeMillis() - utcMillis

  return when {
    period < Time.m -> {
      LastSeen.Recently
    }
    period < Time.h -> LastSeen.MinutesAgo((period / Time.m).toInt())
    period < 24 * Time.h -> LastSeen.HoursAgo((period / Time.h).toInt())
    else -> {
      val date = Instant.ofEpochMilli(utcMillis).atZone(ZoneId.systemDefault()).toLocalDate()
      LastSeen.LongAgo(date.toString())
    }
  }
}

data class DateAndTime(
  val date: String,
  val time: String
) {
  companion object {
    const val TODAY = "Today"
    const val YESTERDAY = "Yesterday"
  }
}

sealed class LastSeen {

  object Online: LastSeen()

  object Recently: LastSeen()

  class MinutesAgo(val minutes: Int): LastSeen()

  class HoursAgo(val hours: Int): LastSeen()

  class LongAgo(val date: String): LastSeen()

}