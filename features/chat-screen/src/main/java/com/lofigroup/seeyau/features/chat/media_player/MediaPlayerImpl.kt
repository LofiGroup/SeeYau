package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.core.util.set
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.media_player.model.PlaybackState
import com.lofigroup.seeyau.features.chat.media_player.model.ProgressData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

class MediaPlayerImpl @Inject constructor(
  private val player: Player
): MediaPlayer {
  private val playbackStateFlow = MutableStateFlow(PlaybackState.PAUSED)
  private val progressFlow = MutableStateFlow(ProgressData())

  private var progressJob: Job? = null
  private val scope = CoroutineScope(Dispatchers.Main)

  private val listener = object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
      playbackStateFlow.set { if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED }
    }
  }

  init {
    player.prepare()
    player.addListener(listener)
  }

  override fun observePlaybackState(): Flow<MediaPlayerState> = combine(playbackStateFlow, progressFlow) { playbackState, progressData ->
    MediaPlayerState(
      playbackState = playbackState,
      progressData = progressData
    )
  }

  override fun seekTo(relativePosition: Float) {
    player.seekTo((player.duration * relativePosition).toLong())
    setProgressData()
  }

  override fun playMedia(mediaItem: MediaItem) {
    player.setMediaItem(mediaItem)
    play()
  }

  override fun resume() {
    if (abs(player.currentPosition - player.duration) <= 20) {
      player.seekToDefaultPosition()
    }
    play()
  }

  override fun pause() {
    player.pause()
    progressJob?.cancel()
    setProgressData()
  }

  override fun stop() {
    player.stop()
    progressJob?.cancel()
    setProgressData()
  }

  override fun obtainPlayer(): Player {
    return player
  }

  override fun destroy() {
    player.release()
  }

  private fun play() {
    player.play()
    progressJob = scope.launch {
      while (true) {
        setProgressData()
        delay(500L)
      }
    }
  }

  private fun setProgressData() {
    progressFlow.set { ProgressData(progress = player.currentPosition, duration = player.duration) }
  }
}