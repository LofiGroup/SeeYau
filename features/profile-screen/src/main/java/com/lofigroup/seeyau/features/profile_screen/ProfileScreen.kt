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
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.applyActivityBarPaddings
import com.sillyapps.core.ui.util.getDefaultImageCropperOptions
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

  Column(
    modifier = Modifier
      .background(LocalExtendedColors.current.backgroundGradient)
      .fillMaxWidth()
      .applyActivityBarPaddings()
  ) {
    RemoteImage(
      model = state.imageUrl,
      errorPlaceholderResId = CommonR.drawable.ic_baseline_account_circle_24,
      placeholderResId = CommonR.drawable.ic_baseline_account_circle_24,
      onClick = {
        pickImageResult.launch(getDefaultImageCropperOptions())
      }
    )


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