package com.sillyapps.core_time

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

fun getLocalCurrentTimeMillis(): Long {
  val tz = TimeZone.getDefault()
  val currentTimeInUTC = System.currentTimeMillis()
  val offsetFromUTC = tz.getOffset(currentTimeInUTC)

  return currentTimeInUTC + offsetFromUTC
}

fun convertToMillis(hours: Int, minutes: Int, seconds: Int = 0): Long {
  return ((hours*60L + minutes)*60 + seconds)*1000L
}

fun getHoursAndMinutes(timeInMillis: Long): Pair<Int, Int> {
  val overallMinutes = (timeInMillis / 60000).toInt()
  val minutes = overallMinutes % 60
  val overallHours = overallMinutes / 60
  val hours = overallHours % 24

  return Pair(hours, minutes)
}

fun convertMillisToStringFormatWithDays(millis: Long): String {
  val seconds = (millis / 1000) % 60
  val overallMinutes = millis / 60000
  val minutes = overallMinutes % 60
  val overallHours = overallMinutes / 60
  val hours = overallHours % 24
  val days = overallHours / 24

  return when {
    days > 0 -> "$days days, $hours hours"
    hours > 0 -> "$hours hours, $minutes minutes"
    minutes > 0 -> "$minutes minutes, $seconds seconds"
    else -> "$seconds seconds"
  }
}

fun convertMillisToStringFormat(millis: Long): String {
  val overallMinutes = abs(millis) / 60000
  val minutes = overallMinutes % 60
  val overallHours = overallMinutes / 60
  val hours = overallHours % 24

  return formatIfNeeded(hours.toInt(), minutes.toInt())
}

fun convertMillisToStringFormatWithSign(millis: Long): String {
  val sign = if (millis < 0L) "-" else "+"

  return "$sign${convertMillisToStringFormat(abs(millis))}"
}


fun convertMillisToStringFormatWithSeconds(millis: Long): String {
  val overallSeconds = millis / 1000
  val seconds = overallSeconds % 60

  val overallMinutes = overallSeconds / 60
  val minutes = overallMinutes % 60

  val overallHours = overallMinutes / 60
  val hours = overallHours % 24

  var formattedSeconds = ":$seconds"
  if (seconds < 10)
    formattedSeconds = ":0$seconds"

  return formatIfNeeded(hours.toInt(), minutes.toInt(), formattedSeconds)
}

fun formatIfNeeded(hours: Int, minutes: Int, secondsFormatted: String = ""): String {
  var stringHours = hours.toString()
  var stringMinutes = minutes.toString()

  if (hours < 10) {
    stringHours = "0$hours"
  }
  if (minutes < 10) {
    stringMinutes = "0$minutes"
  }
  return "$stringHours:$stringMinutes$secondsFormatted"
}

fun formatValue(value: Int): String {
  if (value < 10) {
    return "0$value"
  }
  return "$value"
}

fun getFormattedValuesInRange(size: Int): Array<String> {
  return Array(size) {
    if (it < 10) return@Array "0$it"
    return@Array it.toString()
  }
}

fun getLocalTimeFromMillis(millis: Long): String {
  val time = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalTime()
  return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}


fun getLocalDateAndTimeFromMillis(millis: Long): Pair<String, String> {
  val dateTime = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())

  val timeFormatted = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

  val date = dateTime.toLocalDate()
  val now = LocalDate.now()
  val dateFormatted =
    when {
      date.isEqual(now) -> "Today"
      date.isEqual(now.minusDays(1)) -> "Yesterday"
      else -> {
        date.format(DateTimeFormatter.ofPattern("d MMM"))
      }
    }

  return Pair(timeFormatted, dateFormatted)
}

fun millisToLastSeen(utcMillis: Long): LastSeen {
  val period = System.currentTimeMillis() - utcMillis

  return when {
    period > Time.m -> {
      LastSeen.Recently
    }
    period > Time.h -> LastSeen.MinutesAgo((period / Time.m).toInt())
    period > 24 * Time.h -> LastSeen.HoursAgo((period / Time.h).toInt())
    else -> {
      val date = Instant.ofEpochMilli(utcMillis).atZone(ZoneId.systemDefault()).toLocalDate()
      LastSeen.LongAgo(date.toString())
    }
  }
}

sealed class LastSeen {

  object Recently: LastSeen()

  class MinutesAgo(val minutes: Int): LastSeen()

  class HoursAgo(val hours: Int): LastSeen()

  class LongAgo(val date: String): LastSeen()

}