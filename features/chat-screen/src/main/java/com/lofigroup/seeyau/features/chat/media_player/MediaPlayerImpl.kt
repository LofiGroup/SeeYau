package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.core.util.set
import com.lofigroup.seeyau.features.chat.media_player.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

class MediaPlayerImpl @Inject constructor(
  private val player: Player
) : MediaPlayer {
  private val states = HashMap<Int, MutableStateFlow<MediaPlayerState>>()
  private var currentItemId = -1

  private var progressJob: Job? = null
  private val scope = CoroutineScope(Dispatchers.Main)

  private val listener = object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
      states[currentItemId]?.set { it.copy(playbackState = if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED) }
    }
  }

  init {
    player.prepare()
    player.addListener(listener)
  }

  override fun registerState(id: Int, duration: Long): Flow<MediaPlayerState> {
    Timber.e("Registering state with id: $id")
    val isCurrentItem = currentItemId == id
    val initialState =
      if (isCurrentItem)
        MediaPlayerState(
          progressData = ProgressData.calculate(progress = player.currentPosition, duration = duration),
          playbackState = if (player.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED,
          isCurrentItem = true
        )
      else
        MediaPlayerState(
          progressData = ProgressData.calculate(duration = duration)
        )

    val state = MutableStateFlow(initialState)
    states[id] = state
    return state
  }

  override fun unregisterState(id: Int) {
    Timber.e("Unregistering state with id: $id")
    states.remove(id)
  }

  override fun seekTo(relativePosition: Float) {
    Timber.e("Player duration is ${player.duration}, seekTo: ${player.duration * relativePosition}, relative pos is $relativePosition")
    player.seekTo((player.duration * relativePosition).toLong())
    setProgressData()
  }

  override fun playMedia(mediaItem: MediaItem, id: Int) {
    Timber.e("Play media")
    if (currentItemId == id) {
      resume()
    } else {
      resetCurrentItem()
      currentItemId = id
      states[id]?.set { it.copy(isCurrentItem = true) }

      player.setMediaItem(mediaItem)
      play()
    }
  }

  override fun resume() {
    Timber.e("Resume: currentPosition: ${player.currentPosition}, duration: ${player.duration}")
    if (abs(player.currentPosition - player.duration) <= 50) {
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

  private fun resetCurrentItem() {
    states[currentItemId]?.set { it.reset() }
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
    states[currentItemId]?.set {
      it.copy(
        progressData = ProgressData.calculate(
          progress = player.currentPosition,
          duration = player.duration
        )
      )
    }
  }
}