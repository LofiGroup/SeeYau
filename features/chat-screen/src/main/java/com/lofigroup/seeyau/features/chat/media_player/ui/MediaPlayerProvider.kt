package com.lofigroup.seeyau.features.chat.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.lofigroup.seeyau.features.chat.media_player.test.FakeMediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayer

val LocalMediaPlayer = staticCompositionLocalOf<MediaPlayer> {
  FakeMediaPlayer()
}

@Composable
fun LocalPlayerProvider(
  mediaPlayer: MediaPlayer,
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalMediaPlayer provides mediaPlayer
  ) {
    content()
  }
}