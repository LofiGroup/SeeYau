package com.lofigroup.seeyau.features.chat.media_player.ui

import androidx.compose.runtime.*
import com.lofigroup.seeyau.features.chat.media_player.model.MediaPlayerState
import com.lofigroup.seeyau.features.chat.ui.components.defaultMediaState

@Composable
fun rememberMediaPlayerState(id: Long, duration: Long = 0L): MediaPlayerState {
  val mediaPlayer = LocalMediaPlayer.current

  var state by remember {
    mutableStateOf(defaultMediaState)
  }

  LaunchedEffect(Unit) {
    mediaPlayer.registerState(id = id, duration = duration).collect() { state = it }
  }

  DisposableEffect(key1 = Unit) {
    onDispose { mediaPlayer.unregisterState(id) }
  }

  return state
}