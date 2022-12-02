package com.sillyapps.core_time

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs


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
      LastSeen.LongAgo(date.format(DateTimeFormatter.ofPattern("dd.MM.yy")))
    }
  }
}

fun intervalToString(millis: Long): String {
  val millis = if (millis < 0) 0 else millis

  val overallSeconds = millis / 1000
  val seconds = formatIfNeeded(overallSeconds % 60, addColon = false)

  val overallMinutes = overallSeconds / 60
  val minutes = formatIfNeeded(overallMinutes % 60)

  val overallHours = overallMinutes / 60
  val hours = formatIfNeeded(overallHours % 24, true)

  return "$hours$minutes$seconds"
}

fun formatIfNeeded(time: Long, skipIfZero: Boolean = false, addColon: Boolean = true): String {
  val colon = if (addColon) ":" else ""
  return when {
    skipIfZero && time == 0L -> ""
    time < 10 -> "0$time$colon"
    else -> "$time$colon"
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