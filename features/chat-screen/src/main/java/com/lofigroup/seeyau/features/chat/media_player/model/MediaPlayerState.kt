package com.lofigroup.seeyau.features.chat.media_player.model

data class MediaPlayerState(
  val playbackState: PlaybackState = PlaybackState.PAUSED,
  val progressData: ProgressData = ProgressData(),
)

enum class PlaybackState {
  PLAYING, PAUSED
}