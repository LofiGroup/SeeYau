package com.lofigroup.seeyau.features.chat.ui.components

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.media3.common.MediaItem
import com.lofigroup.core.audio_recorder.AudioRecorder
import com.lofigroup.core.audio_recorder.FakeAudioRecorder
import com.lofigroup.core.audio_recorder.model.AudioRecorderState
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.chat.R
import com.lofigroup.seeyau.features.chat.model.UIMessageType
import com.sillyapps.core.ui.components.ImageButton
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core_time.intervalToString
import timber.log.Timber

@Composable
fun MessageInput(
  message: String,
  setMessage: (String) -> Unit,
  sendMessage: (Uri?) -> Unit,
  onPickMedia: () -> Unit,
  audioRecorder: AudioRecorder
) {
  val recorderState by audioRecorder.observeState()
    .collectAsState(initial = AudioRecorderState.Waiting)

  var isDragged by remember {
    mutableStateOf(false)
  }

  Row(
    modifier = Modifier
      .padding(LocalSpacing.current.medium)
      .defaultMinSize(minHeight = LocalSize.current.bigMedium),
    verticalAlignment = Alignment.CenterVertically
  ) {
    when (val state = recorderState) {
      AudioRecorderState.Waiting -> {
        DefaultMessageInput(
          message = message,
          setMessage = setMessage,
          onPickMedia = onPickMedia,
        )
      }
      is AudioRecorderState.Finished -> {
        RecordingFinishedMessageInput(
          audioUri = state.fileUri,
          audioRecorder = audioRecorder
        )
      }
      is AudioRecorderState.Recording -> {
        RecordingMessageInput(isDragged = isDragged, duration = state.duration)
      }
    }

    TrailingIcon(
      messageIsEmpty = message.isBlank(),
      recorderState = recorderState,
      audioRecorder = audioRecorder,
      sendMessage = {
        val state = recorderState
        if (state is AudioRecorderState.Waiting) sendMessage(null)
        else if (state is AudioRecorderState.Finished) sendMessage(state.fileUri)
      },
      setIsDragged = { isDragged = it }
    )
  }
}

@Composable
fun TrailingIcon(
  messageIsEmpty: Boolean,
  recorderState: AudioRecorderState,
  audioRecorder: AudioRecorder,
  sendMessage: () -> Unit,
  setIsDragged: (Boolean) -> Unit
) {
  var isRecording by rememberSaveable() {
    mutableStateOf(false)
  }

  var draggedOffset by remember {
    mutableStateOf(IntOffset.Zero)
  }

  val offset by animateIntOffsetAsState(
    targetValue = if (isRecording) draggedOffset else IntOffset.Zero
  )

  val size = LocalSize.current.medium

  Box(
    modifier = Modifier
      .size(size)
      .pointerInput(Unit) {
        detectTapGestures(
          onLongPress = {
            isRecording = true
            audioRecorder.start()
          },
          onPress = {
            tryAwaitRelease()
            audioRecorder.stop()
            isRecording = false
            draggedOffset = IntOffset.Zero
          })
      }
      .pointerInput(Unit) {
        detectDragGesturesAfterLongPress { change, _ ->
          if (isRecording) {
            val offsetX = change.position.x.toInt()
            if (offsetX < -500) {
              audioRecorder.stop()
              audioRecorder.deleteRecording()
              isRecording = false
              setIsDragged(false)
            } else if (offsetX < -50) {
              setIsDragged(true)
            } else {
              setIsDragged(false)
            }
            draggedOffset = IntOffset(x = offsetX - size.roundToPx() / 2, y = 0)
          }
        }
      }
      .offset() { offset }
  ) {
    when (recorderState) {
      is AudioRecorderState.Finished -> {
        isRecording = false
        ImageButton(
          onClick = {
            sendMessage()
            audioRecorder.reset()
          },
          painter = painterResource(id = R.drawable.ic_send),
          modifier = Modifier.fillMaxSize()
        )
      }
      is AudioRecorderState.Recording -> {
        Image(
          painter = painterResource(id = R.drawable.ic_recorder_active),
          contentDescription = null,
          modifier = Modifier.fillMaxSize()
        )
      }
      AudioRecorderState.Waiting -> {
        isRecording = false
        if (messageIsEmpty)
          Image(
            painter = painterResource(id = R.drawable.ic_recorder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
          )
        else {
          ImageButton(
            onClick = sendMessage,
            painter = painterResource(id = R.drawable.ic_send),
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }
  }

}

@Composable
fun RowScope.DefaultMessageInput(
  message: String,
  setMessage: (String) -> Unit,
  onPickMedia: () -> Unit,
) {
  ImageButton(
    onClick = onPickMedia,
    painter = painterResource(id = R.drawable.ic_image)
  )

  TextField(
    value = message,
    onValueChange = setMessage,
    modifier = Modifier.weight(1f),
    keyboardOptions = KeyboardOptions(
      imeAction = ImeAction.Default,
      keyboardType = KeyboardType.Text
    ),
    colors = TextFieldDefaults.textFieldColors(
      backgroundColor = Color.Transparent,
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent
    ),
    placeholder = {
      Text(text = stringResource(id = R.string.message))
    },
  )
}

@Composable
fun RowScope.RecordingMessageInput(
  isDragged: Boolean,
  duration: Long
) {
  if (isDragged) {
    Image(
      painter = painterResource(id = R.drawable.ic_delete_rotated),
      contentDescription = null,
      modifier = Modifier
        .size(LocalSize.current.medium)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = stringResource(id = R.string.delete),
      color = MaterialTheme.colors.error,
      modifier = Modifier.weight(1f)
    )
  }
  else {
    Image(
      painter = painterResource(id = R.drawable.ic_delete),
      contentDescription = null,
      modifier = Modifier
        .size(LocalSize.current.medium)
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = intervalToString(duration),
      style = MaterialTheme.typography.body1
    )

    Spacer(modifier = Modifier.width(LocalSpacing.current.small))

    Text(
      text = stringResource(id = R.string.recording),
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
fun RowScope.RecordingFinishedMessageInput(
  audioRecorder: AudioRecorder,
  audioUri: Uri,
) {
  val context = LocalContext.current

  ImageButton(
    onClick = { audioRecorder.deleteRecording() },
    painter = painterResource(id = R.drawable.ic_delete),
    modifier = Modifier.size(LocalSize.current.medium)
  )

  Box(modifier = Modifier.weight(1f)) {
    AudioPlayerControls(
      audioContent = UIMessageType.Audio(
        mediaItem = MediaItem.fromUri(audioUri),
        duration = getAudioFileDuration(audioUri.toString(), context)
      ),
      id = -2L,
    )
  }
}

@Preview
@Composable
fun MessageInputPreview() {
  AppTheme {
    Surface() {
      MessageInput(
        message = "Herlo",
        setMessage = {},
        sendMessage = { },
        onPickMedia = { },
        audioRecorder = FakeAudioRecorder,
      )
    }
  }
}

fun getAudioFileDuration(rawUri: String, context: Context): Long {
  if (rawUri.isBlank()) return 0L

  val mmr = MediaMetadataRetriever()

  val uri = Uri.parse(rawUri)
  if (uri.scheme == "content") mmr.setDataSource(context, uri)
  else mmr.setDataSource(rawUri)

  val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "0"
  return duration.toLong()
}