package com.lofigroup.seeyau.features.chat.components.message_contents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.lofigroup.seeyau.features.chat.util.LocalMediaPlayer

@Composable
fun AudioContent(
  audioContent: UIMessageType.Audio
) {
  val mediaPlayer = LocalMediaPlayer.current
  val context = LocalContext.current

  Row(
    modifier = Modifier.fillMaxWidth()
  ) {
    AndroidView(
      factory = {
        PlayerView(context).also {
          it.player = mediaPlayer.obtainPlayer()
        }
      },
      modifier = Modifier.weight(1f)
    )
  }
}