package com.lofigroup.core.audio_recorder

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.lofigroup.core.audio_recorder.model.AudioRecorderState
import com.lofigroup.core.audio_recorder.permission.AudioRecorderPermission
import com.lofigroup.core.permission.PermissionRequestChannel
import com.lofigroup.core.util.set
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AudioRecorderImpl @Inject constructor(
  private val context: Context,
  private val permissionRequestChannel: PermissionRequestChannel
): AudioRecorder {
  private val scope = CoroutineScope(Dispatchers.Main)
  private var job: Job? = null
  private val state = MutableStateFlow<AudioRecorderState>(AudioRecorderState.Waiting)

  private var audioFile: File = File("")
  private var recordingStartedAt: Long = 0L

  private val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    MediaRecorder(context)
  } else {
    MediaRecorder()
  }

  private val listener = MediaRecorder.OnInfoListener { _, what, _ ->
    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
      stop()
    }
  }

  init {
    mediaRecorder.setOnInfoListener(listener)
  }

  private fun canRecordAudio(): Boolean {
    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
  }

  private fun setOutputFile() {
    val recordingsDir = File(context.filesDir, context.getString(R.string.audio_records_dir))
    recordingsDir.mkdir()
    audioFile = File(recordingsDir,  "${System.currentTimeMillis()}.mp3")
    mediaRecorder.setOutputFile(audioFile.path)
  }

  private fun prepareAudioRecorder() {
    mediaRecorder.apply {
      tryReset()
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
      setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)

      setMaxFileSize(2_000_000)

      setOutputFile()
      mediaRecorder.prepare()
    }
  }

  private fun tryReset() {
    try {
      mediaRecorder.reset()
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  private suspend fun recordingTimer() {
    while (true) {
      state.set { AudioRecorderState.Recording(duration = System.currentTimeMillis() - recordingStartedAt) }
      delay(350L)
    }
  }

  override fun stop() {
    job?.cancel()
    if (state.value !is AudioRecorderState.Recording) return

    try {
      mediaRecorder.stop()

      val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", audioFile)
      state.set { AudioRecorderState.Finished(uri) }
    } catch (e: Exception) {
      Timber.e(e)
      state.set { AudioRecorderState.Waiting }
    }
  }

  override fun start() {
    job?.cancel()

    if (!canRecordAudio()) {
      return
    }

    job = scope.launch() {
      val permissionRequestSuccessful = permissionRequestChannel.requestPermission(AudioRecorderPermission)
      if (!permissionRequestSuccessful) return@launch

      try {
        prepareAudioRecorder()
        state.set { AudioRecorderState.Recording(duration = 0L) }
        mediaRecorder.start()
        recordingStartedAt = System.currentTimeMillis()
      }
      catch (e: Exception) {
        Timber.e(e)
        mediaRecorder.reset()
        state.set { AudioRecorderState.Waiting }
      }
      recordingTimer()
    }
  }



  override fun deleteRecording() {
    try {
      audioFile.delete()
    } catch (e: IOException) {
    }
    reset()
  }

  override fun release() {
    job?.cancel()
    tryReset()
    deleteRecording()
    mediaRecorder.release()
  }

  override fun reset() {
    job?.cancel()
    tryReset()
    audioFile = File("")
    state.set { AudioRecorderState.Waiting }
  }

  override fun observeState(): Flow<AudioRecorderState> = state

}