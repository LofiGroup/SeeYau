package com.lofigroup.seeyau.features.chat.media_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow

interface MediaPlayer {

  fun observeState(): Flow<MediaPlayerState>

  fun playMedia(mediaItem: MediaItem)
  fun isPlayingMediaItem(mediaItem: MediaItem): Boolean

  fun pause()
  fun stop()

  fun obtainPlayer(): Player?

  fun destroy()

}