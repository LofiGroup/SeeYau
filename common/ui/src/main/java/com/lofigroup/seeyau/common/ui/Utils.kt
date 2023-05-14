package com.lofigroup.seeyau.common.ui

import android.content.Context
import android.content.res.Resources
import com.sillyapps.core_time.DateAndTime
import com.sillyapps.core_time.LastSeen
import com.sillyapps.core_time.getLocalDateAndTimeFromMillis
import com.sillyapps.core_time.millisToLastSeen

fun getLocalizedLastSeen(millis: Long, resources: Resources): String {
  return when (val lastSeen = millisToLastSeen(millis)) {
    is LastSeen.Online -> resources.getString(R.string.online)
    is LastSeen.HoursAgo -> resources.getQuantityString(R.plurals.seen_hours_ago, lastSeen.hours, lastSeen.hours)
    is LastSeen.MinutesAgo -> resources.getQuantityString(R.plurals.seen_minutes_ago, lastSeen.minutes, lastSeen.minutes)
    is LastSeen.LongAgo -> resources.getString(R.string.last_seen_date, lastSeen.date)
    LastSeen.Recently -> resources.getString(R.string.seen_recently)
  }
}

fun getLocalizedDatedAndTimeFromMillis(millis: Long, resources: Resources): DateAndTime {
  val dateTime = getLocalDateAndTimeFromMillis(millis)
  val date =  when (val temp = dateTime.date) {
    DateAndTime.YESTERDAY -> resources.getString(R.string.yesterday)
    DateAndTime.TODAY -> resources.getString(R.string.today)
    else -> temp
  }
  return dateTime.copy(date = date)
}

fun getFormattedFileSize(sizeInBytes: Long): String {
  return when {
    sizeInBytes >= ByteMeasures.MB -> "%.1f Mb".format(sizeInBytes.toFloat() / ByteMeasures.MB)
    sizeInBytes >= ByteMeasures.KB -> "%.1f Kb".format(sizeInBytes.toFloat() / ByteMeasures.KB)
    else -> "$sizeInBytes bytes"
  }
}

object ByteMeasures {
  const val KB = 1024
  const val MB = 1024 * 1024
}