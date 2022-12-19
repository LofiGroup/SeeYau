package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MediaPlayer {

  fun registerState(id: Int, duration: Long = 0L): Flow<MediaPlayerState>
  fun unregisterState(id: Int) {}

  fun playMedia(mediaItem: MediaItem, id: Int) {}
  fun resume() {}
  fun pause() {}
  fun stop() {}

  fun obtainPlayer(): Player? = null
  fun seekTo(relativePosition: Float) {}

  fun destroy() {}

}

object FakeMediaPlayer: MediaPlayer {
  override fun registerState(id: Int, duration: Long): Flow<MediaPlayerState> = flow {}
}