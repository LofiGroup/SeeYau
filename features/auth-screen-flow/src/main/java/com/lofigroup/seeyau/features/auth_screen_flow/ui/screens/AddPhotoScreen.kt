package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.ui.TopBar
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.getDefaultImageCropperOptions
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun AddPhotoScreen(
  imageUri: String,
  setImageUri: (Uri) -> Unit,
  throwError: (String) -> Unit,
  update: () -> Unit,
  onUpButtonClick: () -> Unit
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
      TopBar(onUpButtonClick = onUpButtonClick)

      Text(
        text = stringResource(id = R.string.be_recognizable),
        style = MaterialTheme.typography.h2,
        modifier = Modifier.padding(top = LocalSpacing.current.large)
      )
    }

    RemoteImage(
      model = imageUri,
      onClick = { pickImageResult.launch(getDefaultImageCropperOptions()) },
      modifier = Modifier
        .fillMaxWidth(0.7f)
        .aspectRatio(1f)
        .padding(8.dp)
        .align(Alignment.Center)

    )

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = LocalSpacing.current.medium)
    ) {
      if (imageUri.isBlank()) {
        TextButton(
          onClick = update,
          modifier = Modifier.padding(bottom = LocalSpacing.current.small)
        ) {
          Text(
            text = stringResource(id = R.string.skip),
            style = MaterialTheme.typography.h3,
            color = LocalExtendedColors.current.disabled,
          )
        }

        TextButton(
          onClick = {
            pickImageResult.launch(getDefaultImageCropperOptions())
          },
        ) {
          Text(
            text = stringResource(id = R.string.add_photo),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary
          )
        }
      } else {
        TextButton(onClick = update) {
          Text(
            text = stringResource(id = CommonR.string.lets_shine),
            style = MaterialTheme.typography.h3
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
          imageUri = "",
          setImageUri = {},
          throwError = {},
          update = {},
          onUpButtonClick = {}
        )
      }
    }
  }
}