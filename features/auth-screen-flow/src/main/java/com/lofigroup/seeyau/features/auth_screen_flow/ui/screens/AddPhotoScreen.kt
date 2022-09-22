package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import android.graphics.Bitmap
import android.net.Uri
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
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.ui.TopBar
import com.sillyapps.core.ui.components.RemoteImage
import com.lofigroup.seayau.common.ui.R as CommonR

@Composable
fun AddPhotoScreen(
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

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .padding(top = LocalSpacing.current.extraLarge, bottom = LocalSpacing.current.large)
  ) {
    Text(
      text = stringResource(id = R.string.be_recognizable),
      style = MaterialTheme.typography.h4
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

    RemoteImage(
      model = imageUri,
      placeholderResId = CommonR.drawable.ic_baseline_account_circle_24,
      errorPlaceholderResId = CommonR.drawable.ic_baseline_account_circle_24,
      modifier = Modifier
        .fillMaxWidth(0.7f)
        .aspectRatio(1f)
        .padding(8.dp),
      onClick = {
        navigateToPictureCropper(pickImageResult)
      }
    )

    Spacer(modifier = Modifier.weight(1f))

    if (imageUri.isBlank()) {
      TextButton(
        onClick = {
          navigateToPictureCropper(pickImageResult)
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
    
    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

  }
}

private fun navigateToPictureCropper(pickImageResult: ManagedActivityResultLauncher<CropImageContractOptions, CropImageView.CropResult>) {
  pickImageResult.launch(
    options {
      setGuidelines(CropImageView.Guidelines.ON)
      setCropShape(CropImageView.CropShape.RECTANGLE)
      setOutputCompressFormat(Bitmap.CompressFormat.PNG)
    }
  )
}

@Preview
@Composable
fun AddPhotoScreenPreview() {
  AppTheme() {
    Surface() {
      Column() {
        TopBar()

        AddPhotoScreen(
          imageUri = "",
          setImageUri = {},
          throwError = {},
          update = {}
        )
      }
    }
  }
}