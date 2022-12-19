package com.lofigroup.seeyau.features.chat.media_player.model

import com.sillyapps.core_time.intervalToString

data class ProgressData(
  val progress: String = "00:00",
  val duration: String = "00:00",
  val relativeProgress: Float = 0f
) {

  companion object {
    fun calculate(progress: Long = 0L, duration: Long = 0L): ProgressData {
      return ProgressData(
        progress = intervalToString(progress),
        duration = intervalToString(duration),
        relativeProgress = if (duration == 0L) 0f else progress.toFloat() / duration
      )
    }
  }
}
