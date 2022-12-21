package com.lofigroup.core.audio_recorder.di

import com.lofigroup.core.audio_recorder.AudioRecorder
import com.lofigroup.core.audio_recorder.AudioRecorderImpl
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface AudioRecorderModule {
  @Binds
  fun bindAudioRecorder(audioRecorderImpl: AudioRecorderImpl): AudioRecorder
}