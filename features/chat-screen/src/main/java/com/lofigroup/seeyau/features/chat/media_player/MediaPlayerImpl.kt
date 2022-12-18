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
  private val state = MutableStateFlow(MediaPlayerState())

  private var progressJob: Job? = null
  private val scope = CoroutineScope(Dispatchers.Main)

  private val listener = object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
      state.set { it.copy(playbackState = if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED) }
    }
  }

  init {
    player.prepare()
    player.addListener(listener)
  }

  override fun observePlaybackState(): Flow<MediaPlayerState> = state

  override fun seekTo(relativePosition: Float) {
    Timber.e("Seeking to $relativePosition")
    player.seekTo((player.duration * relativePosition).toLong())
    setProgressData()
  }

  override fun playMedia(mediaItem: MediaItem, id: Int) {
    Timber.e("Play media")
    if (state.value.currentItemId == id) {
      resume()
    }
    else {
      player.setMediaItem(mediaItem)
      state.set { it.copy(currentItemId = id) }
      play()
    }
  }

  override fun resume() {
    Timber.e("Resume: currentPosition: ${player.currentPosition}, duration: ${player.duration}")
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

  override fun isCurrentItem(itemId: Int): Boolean {
    return state.value.currentItemId == itemId
  }

  private fun setProgressData() {
    state.set { it.copy(progressData = ProgressData(progress = player.currentPosition, duration = player.duration)) }
  }
}