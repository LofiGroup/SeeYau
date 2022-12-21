package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
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
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.lofigroup.seeyau.features.auth_screen_flow.model.AuthFlowState
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
  flowState: AuthFlowState,
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

  Column(modifier = Modifier.align(Alignment.Center)) {
    TopBar(onUpButtonClick = onUpButtonClick)
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      Box(modifier = Modifier.align(Alignment.Center)) {
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
                .border(
                  width = 2.dp,
                  brush = LocalExtendedColors.current.secondaryVerticalGradient,
                  shape = CircleShape
                )
            ) {
              Image(painter = painterResource(id = R.drawable.ic_camera), contentDescription = null)
            }
          },
          modifier = Modifier
            .padding(bottom = LocalSize.current.small / 2)
            .fillMaxWidth(0.7f)
            .aspectRatio(1f)
        )

        if (imageUri.isNotBlank())
          Icon(
            painter = painterResource(id = CommonR.drawable.ic_change_photo_icon),
            contentDescription = null,
            modifier = Modifier
              .size(LocalSize.current.small)
              .align(Alignment.BottomCenter)
          )
      }

      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = LocalSpacing.current.medium)
          .padding(horizontal = LocalSpacing.current.medium)
      ) {
        when (flowState) {
          AuthFlowState.WAITING_FOR_INPUT -> {
            Text(
              text = stringResource(
                id = if (imageUri.isBlank()) R.string.add_photo_so_people_can_recongize_you
                else R.string.connect_with_people
              ),
              modifier = Modifier
                .fillMaxWidth()

            )
          }
          AuthFlowState.SYNCING_DATA, AuthFlowState.ALL_DATA_IS_VALID -> {
            CircularProgressIndicator()
          }
          AuthFlowState.ERROR -> {
            Text(
              text = stringResource(R.string.something_went_wrong),
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }

    ButtonWithText(
      text = stringResource(id = R.string.lets_go),
      onClick = update,
      enabled = imageUri.isNotBlank() && flowState != AuthFlowState.SYNCING_DATA,
      backgroundColor = LocalExtendedColors.current.secondaryGradient,
      modifier = Modifier
        .padding(bottom = LocalSpacing.current.large)
        .padding(horizontal = LocalSpacing.current.medium)
        .navigationBarsPadding()
    )
  }

  BackHandler(onBack = onUpButtonClick)
}

@Preview
@Composable
fun AddPhotoScreenPreview() {
  AppTheme() {
    Surface() {
      Box(Modifier.fillMaxSize()) {
        AddPhotoScreen(
          imageUri = "fds",
          setImageUri = {},
          throwError = {},
          update = {},
          onUpButtonClick = {},
          flowState = AuthFlowState.WAITING_FOR_INPUT
        )
      }
    }
  }
}