package com.lofigroup.seeyau.features.chat.media_player.model

data class MediaPlayerState(
  val playbackState: PlaybackState = PlaybackState.PAUSED,
  val progressData: ProgressData = ProgressData(durationValue = 0L),
  val isCurrentItem: Boolean = false
)

fun MediaPlayerState.reset() = MediaPlayerState(
  progressData = ProgressData(durationValue = progressData.durationValue)
)

enum class PlaybackState {
  PLAYING, PAUSED
}