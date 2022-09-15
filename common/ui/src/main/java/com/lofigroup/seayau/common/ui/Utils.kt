package com.lofigroup.seayau.common.ui

import android.content.Context
import com.sillyapps.core_time.LastSeen
import com.sillyapps.core_time.millisToLastSeen

fun getLocalizedLastSeen(millis: Long, context: Context): String {
  return when (val lastSeen = millisToLastSeen(millis)) {
    is LastSeen.HoursAgo -> context.resources.getQuantityString(R.plurals.seen_hours_ago, lastSeen.hours)
    is LastSeen.MinutesAgo -> context.resources.getQuantityString(R.plurals.seen_hours_ago, lastSeen.minutes)
    is LastSeen.LongAgo -> lastSeen.date
    LastSeen.Recently -> context.resources.getString(R.string.seen_recently)
  }
}