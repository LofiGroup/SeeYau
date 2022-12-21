package com.lofigroup.core.audio_recorder

import com.lofigroup.core.audio_recorder.model.AudioRecorderState
import kotlinx.coroutines.flow.Flow

interface AudioRecorder {
  fun observeState(): Flow<AudioRecorderState>
  fun start()
  fun stop()
  fun deleteRecording()
  fun release()
  fun reset()
}