package com.lofigroup.seeyau.features.chat.media_player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.lofigroup.seeyau.features.chat.media_player.FakeMediaPlayerControls
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayerControls

val LocalMediaPlayer = staticCompositionLocalOf<MediaPlayerControls> {
  FakeMediaPlayerControls
}

@Composable
fun LocalPlayerProvider(
  mediaPlayer: MediaPlayerControls,
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalMediaPlayer provides mediaPlayer
  ) {
    content()
  }
}