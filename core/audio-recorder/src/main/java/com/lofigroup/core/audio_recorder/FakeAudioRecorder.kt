package com.lofigroup.core.audio_recorder

import com.lofigroup.core.audio_recorder.model.AudioRecorderState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeAudioRecorder: AudioRecorder {
  override fun observeState(): Flow<AudioRecorderState> = flow{ emit(AudioRecorderState.Waiting) }

  override fun start() {}

  override fun stop() {}

  override fun deleteRecording() {}
  override fun release() {}
  override fun reset() {}
}