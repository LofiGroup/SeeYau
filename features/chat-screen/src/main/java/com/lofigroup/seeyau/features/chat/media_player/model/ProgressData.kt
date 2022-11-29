package com.lofigroup.seeyau.features.chat.media_player.model

import com.sillyapps.core_time.intervalToString

class ProgressData(
  progress: Long = 0L,
  duration: Long = 0L,
) {
  val progress: String = intervalToString(progress)
  val duration: String = intervalToString(duration)
  val relativeProgress: Float = progress.toFloat() / duration
}
