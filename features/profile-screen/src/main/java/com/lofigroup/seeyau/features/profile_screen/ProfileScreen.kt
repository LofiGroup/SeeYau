package com.lofigroup.seeyau.features.profile_screen

import android.graphics.Bitmap
import android.net.Uri
import android.os.strictmode.UntaggedSocketViolation
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.components.ShowToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun ProfileScreen(
  stateHolder: ProfileScreenStateHolder,
  onExit: () -> Unit
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = ProfileScreenState())

  val context = LocalContext.current

  val pickImageResult =
    rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
      if (result.isSuccessful) {
        val uri = result.uriContent
          ?: return@rememberLauncherForActivityResult stateHolder.throwError(context.getString(CommonR.string.image_crop_error))

        stateHolder.setImageUri(uri)
      } else {
        stateHolder.throwError(context.getString(CommonR.string.image_crop_error))
      }
    }

  if (state.navigateOut) {
    LaunchedEffect(state) {
      onExit()
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      RemoteImage(
        model = state.imageUrl,
        placeholderResId = CommonR.drawable.ic_baseline_account_circle_24,
        modifier = Modifier
          .fillMaxWidth(0.4f)
          .aspectRatio(1f)
          .padding(8.dp),
        onClick = {
          pickImageResult.launch(
            options {
              setGuidelines(CropImageView.Guidelines.ON)
              setAspectRatio(1, 1)
              setCropShape(CropImageView.CropShape.RECTANGLE)
              setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
          )
        }
      )

      TextField(
        value = state.name,
        onValueChange = { stateHolder.setName(it) },
        label = { Text(text = "Name") },
        enabled = !state.isLoading,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 16.dp)
      )

      if (state.isLoading) {
        CircularProgressIndicator(
          modifier = Modifier.padding(16.dp)
        )
      }
    }

    FloatingActionButton(
      onClick = { stateHolder.saveProfile() },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      Icon(imageVector = Icons.Filled.Check, contentDescription = null)
    }
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }

}

@Preview
@Composable
fun ProfileScreenPreview() {
  val state = MutableStateFlow(ProfileScreenState())

  val stateHolder = object : ProfileScreenStateHolder {
    override fun getState(): Flow<ProfileScreenState> {
      return state
    }

    override fun setName(name: String) {
      state.value = state.value.copy(name = name)
    }

    override fun saveProfile() {

    }

    override fun throwError(errorMessage: String) {
      state.value = state.value.copy(errorMessage = errorMessage)
    }

    override fun setImageUri(uri: Uri) {

    }

  }

  AppTheme() {
    Surface() {
      ProfileScreen(
        stateHolder = stateHolder,
        onExit = {}
      )
    }
  }

}