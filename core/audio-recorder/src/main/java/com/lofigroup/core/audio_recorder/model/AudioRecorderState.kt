package com.lofigroup.core.audio_recorder.model

import android.net.Uri

sealed interface AudioRecorderState {
  object Waiting: AudioRecorderState
  class Recording(val duration: Long): AudioRecorderState
  class Finished(val fileUri: Uri): AudioRecorderState
}
