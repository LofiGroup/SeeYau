package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MediaPlayerControls {
  fun playMedia(mediaItem: MediaItem, pos: Int) {}
  fun pause() {}
  fun obtainPlayer(): Player? = null
  fun observePlaybackState(): Flow<MediaPlayerState>
  fun seekTo(relativePos: Float) {}
}

class MediaPlayerControlsImpl(
  val mediaPlayer: MediaPlayer,
  val onCurrentItemPosChanged: (Int, MediaItem) -> Unit
): MediaPlayerControls {

  override fun playMedia(mediaItem: MediaItem, pos: Int) {
    onCurrentItemPosChanged(pos, mediaItem)
  }

  override fun pause() = mediaPlayer.pause()

  override fun obtainPlayer() = mediaPlayer.obtainPlayer()

  override fun observePlaybackState() = mediaPlayer.observePlaybackState()

  override fun seekTo(relativePos: Float) = mediaPlayer.seekTo(relativePos)

}

object FakeMediaPlayerControls: MediaPlayerControls {
  override fun observePlaybackState(): Flow<MediaPlayerState> = flow { emit(MediaPlayerState()) }
}