package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MediaPlayer {

  fun registerState(id: Long, duration: Long = 0L): Flow<MediaPlayerState>
  fun unregisterState(id: Long) {}

  fun playMedia(mediaItem: MediaItem, id: Long) {}
  fun resume() {}
  fun pause() {}
  fun stop() {}

  fun obtainPlayer(): Player? = null
  fun seekTo(relativePosition: Float) {}

  fun destroy() {}

}

object FakeMediaPlayer: MediaPlayer {
  override fun registerState(id: Long, duration: Long): Flow<MediaPlayerState> = flow {}
}