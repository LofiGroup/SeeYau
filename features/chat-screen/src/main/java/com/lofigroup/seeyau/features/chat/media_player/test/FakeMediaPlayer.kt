package com.lofigroup.seeyau.features.chat.media_player.test

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMediaPlayer: MediaPlayer {
  override fun observeState(): Flow<MediaPlayerState> = flow { emit(MediaPlayerState()) }

  override fun playMedia(mediaItem: MediaItem) {

  }

  override fun isPlayingMediaItem(mediaItem: MediaItem): Boolean {
    return false
  }

  override fun pause() {

  }

  override fun stop() {

  }

  override fun obtainPlayer(): Player? {
    return null
  }

  override fun destroy() {

  }

}