package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seayau.common.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.network.BackendConstants
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.ui.TopBar
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.util.getDefaultImageCropperOptions
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun AddPhotoScreen(
  topBar: @Composable () -> Unit,
  imageUri: String,
  setImageUri: (Uri) -> Unit,
  throwError: (String) -> Unit,
  update: () -> Unit
) {
  val context = LocalContext.current

  val pickImageResult =
    rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
      if (result.isSuccessful) {
        val uri = result.uriContent
          ?: return@rememberLauncherForActivityResult throwError(context.getString(CommonR.string.image_crop_error))

        setImageUri(uri)
      } else {
        throwError(context.getString(CommonR.string.image_crop_error))
      }
    }

  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.align(Alignment.TopCenter)
    ) {
      topBar()

      Text(
        text = stringResource(id = R.string.be_recognizable),
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(top = LocalSpacing.current.extraLarge)
      )
    }

    RemoteImage(
      model = imageUri,
      placeholderResId = CommonR.drawable.ic_baseline_account_circle_24,
      errorPlaceholderResId = CommonR.drawable.ic_baseline_account_circle_24,
      modifier = Modifier
        .fillMaxWidth(0.7f)
        .aspectRatio(1f)
        .padding(8.dp)
        .align(Alignment.Center),
      onClick = {
        pickImageResult.launch(getDefaultImageCropperOptions())
      }
    )

    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = LocalSpacing.current.large)
    ) {
      if (imageUri.isBlank()) {
        TextButton(
          onClick = {
            pickImageResult.launch(getDefaultImageCropperOptions())
          },
        ) {
          Text(
            text = stringResource(id = R.string.add_photo),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.secondary
          )
        }
      } else {
        TextButton(onClick = update) {
          Text(
            text = stringResource(id = CommonR.string.lets_shine),
            style = MaterialTheme.typography.h5
          )
        }
      }
    }

  }
}

@Preview
@Composable
fun AddPhotoScreenPreview() {
  AppTheme() {
    Surface() {
      Column() {
        AddPhotoScreen(
          topBar = { TopBar() },
          imageUri = "",
          setImageUri = {},
          throwError = {},
          update = {}
        )
      }
    }
  }
}