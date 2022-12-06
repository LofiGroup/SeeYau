package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.lofigroup.seeyau.common.ui.components.ButtonWithText
import com.lofigroup.seeyau.common.ui.components.FullScreenImage
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.ui.components.TopBar
import com.sillyapps.core.ui.components.RemoteImage
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSize
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.getDefaultImageCropperOptions
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun BoxScope.AddPhotoScreen(
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

  FullScreenImage(painter = painterResource(id = R.drawable.profile_picture_background))

  TopBar(onUpButtonClick = onUpButtonClick)

  RemoteImage(
    model = imageUri,
    onClick = { pickImageResult.launch(getDefaultImageCropperOptions()) },
    loadingPlaceholder = {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(modifier = Modifier.size(LocalSize.current.bigMedium))
      }
    },
    errorPlaceholder = {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White)
          .border(width = 2.dp, brush = LocalExtendedColors.current.secondaryVerticalGradient, shape = CircleShape)
      ) {
        Image(painter = painterResource(id = R.drawable.ic_camera), contentDescription = null)
      }
    },
    modifier = Modifier
      .fillMaxWidth(0.7f)
      .aspectRatio(1f)
      .padding(8.dp)
      .align(Alignment.Center)
  )

  Column(
    modifier = Modifier
      .align(Alignment.BottomCenter)
      .padding(bottom = LocalSpacing.current.large)
      .padding(horizontal = LocalSpacing.current.medium)
      .navigationBarsPadding()
  ) {
    Text(text = stringResource(id = R.string.add_photo_so_people_can_recongize_you))

    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

    ButtonWithText(
      text = stringResource(id = R.string.lets_go),
      onClick = update,
      enabled = imageUri.isNotBlank(),
      backgroundColor = LocalExtendedColors.current.secondaryGradient
    )
  }
}

@Preview
@Composable
fun AddPhotoScreenPreview() {
  AppTheme() {
    Surface() {
      Box() {
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