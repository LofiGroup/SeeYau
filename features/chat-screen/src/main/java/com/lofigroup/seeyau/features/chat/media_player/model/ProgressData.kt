package com.lofigroup.seeyau.features.chat.media_player.model

import com.sillyapps.core_time.intervalToString

data class ProgressData(
  val progressValue: Long = 0L,
  val durationValue: Long
) {
  val progress: String = intervalToString(progressValue)
  val duration: String = intervalToString(durationValue)
  val relativeProgress: Float = if (durationValue == 0L) 0f else progressValue.toFloat() / durationValue

  fun update(progressValue: Long): ProgressData =
    ProgressData(
      progressValue = progressValue,
      durationValue = durationValue
    )
}
