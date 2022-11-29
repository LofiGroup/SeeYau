package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.core.util.set
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.media_player.model.PlaybackState
import com.lofigroup.seeyau.features.chat.media_player.model.ProgressData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MediaPlayerImpl @Inject constructor(
  private val player: Player
): MediaPlayer {
  private val playbackStateFlow = MutableStateFlow(PlaybackState.PAUSED)

  private val listener = object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
      playbackStateFlow.set { if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED }
    }
  }

  private val progressFlow = flow {
    while (true) {
      emit(ProgressData(progress = player.currentPosition, duration = player.duration))
      delay(500L)
    }
  }.flowOn(Dispatchers.Main)

  init {
    player.prepare()
    player.addListener(listener)
  }

  override fun observeState(): Flow<MediaPlayerState> = combine(playbackStateFlow, progressFlow) { playbackState, progressData ->
    MediaPlayerState(
      playbackState = playbackState,
      progressData = progressData
    )
  }

  override fun playMedia(mediaItem: MediaItem) {
    player.setMediaItem(mediaItem)
    player.play()
  }

  override fun isPlayingMediaItem(mediaItem: MediaItem): Boolean {
    return player.currentMediaItem == mediaItem
  }

  override fun pause() {
    player.pause()
  }

  override fun stop() {
    player.stop()
  }

  override fun obtainPlayer(): Player {
    return player
  }

  override fun destroy() {
    player.release()
  }
}