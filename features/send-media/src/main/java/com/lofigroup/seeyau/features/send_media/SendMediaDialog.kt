package com.lofigroup.seeyau.features.send_media

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lofigroup.seeyau.common.ui.components.SwipeableBox
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.showToast
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SendMediaDialog(
  isVisible: Boolean,
  onDismiss: () -> Unit,
  onSendMessage: (Uri) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current.applicationContext
  val swipeableState = rememberSwipeableState(initialValue = 1)
  val coroutineScope = rememberCoroutineScope()

  fun dismiss() {
    coroutineScope.launch {
      swipeableState.animateTo(1)
      onDismiss()
    }
  }

  var tempImageUri by remember {
    mutableStateOf(initTempUri(context))
  }

  val getContent =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
      if (it != null) {
        context.contentResolver.takePersistableUriPermission(
          it,
          Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        onSendMessage(it)
      }
    }

  val getContact =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.PickContact()) {

    }

  val cameraLauncher =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
      if (success)
        onSendMessage(tempImageUri)
      tempImageUri = initTempUri(context)
    }

  if (isVisible) {
    SwipeableBox(
      onDismiss = onDismiss,
      swipeableState = swipeableState,
      modifier = Modifier.imePadding()
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
          .padding(LocalSpacing.current.medium)
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colors.background)
          .padding(LocalSpacing.current.medium)
          .navigationBarsPadding()
      ) {
        Row(
          horizontalArrangement = Arrangement.SpaceEvenly,
          modifier = Modifier.fillMaxWidth()
        ) {
          SendMediaItem(
            onClick = {
              dismiss()
              getContent.launch(arrayOf("image/*", "video/*"))
            },
            imageVector = Icons.Filled.BrowseGallery,
            title = stringResource(id = R.string.gallery)
          )

          SendMediaItem(
            onClick = {
              dismiss()
              getContent.launch(arrayOf("audio/*"))
            },
            imageVector = Icons.Filled.AudioFile,
            title = stringResource(id = R.string.audio)
          )

          SendMediaItem(
            onClick = {
              showToast(context, context.getString(R.string.not_implemented_yet))
              dismiss()
            },
            imageVector = Icons.Filled.FileCopy,
            title = stringResource(id = R.string.file)
          )
        }

        Row(
          horizontalArrangement = Arrangement.SpaceEvenly,
          modifier = Modifier.fillMaxWidth()
        ) {
          SendMediaItem(
            onClick = {
              dismiss()
              cameraLauncher.launch(tempImageUri)
            },
            imageVector = Icons.Filled.Camera,
            title = stringResource(id = R.string.camera)
          )

          SendMediaItem(
            onClick = {
              showToast(context, context.getString(R.string.not_implemented_yet))
              dismiss()
            },
            imageVector = Icons.Filled.Contacts,
            title = stringResource(id = R.string.contact)
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun SendMediaDialogPreview() {
  var isVisible by remember {
    mutableStateOf(false)
  }

  val context = LocalContext.current

  AppTheme {
    Surface() {
      Box(modifier = Modifier.fillMaxSize()) {
        TextButton(
          onClick = { isVisible = true },
          modifier = Modifier.align(Alignment.Center)
        ) {
          Text(text = "Show")
        }

        SendMediaDialog(
          isVisible = isVisible,
          onDismiss = { isVisible = false },
          modifier = Modifier.align(Alignment.BottomCenter),
          onSendMessage = { showToast(context, it.toString()) },
        )
      }
    }
  }
}