package com.lofigroup.seeyau.features.chat.media_player.model

data class MediaPlayerState(
  val playbackState: PlaybackState = PlaybackState.PAUSED,
  val progressData: ProgressData = ProgressData(),
  val currentItemId: Int = -1
)

enum class PlaybackState {
  PLAYING, PAUSED
}