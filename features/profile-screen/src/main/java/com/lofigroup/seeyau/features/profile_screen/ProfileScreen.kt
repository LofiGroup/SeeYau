package com.lofigroup.seeyau.features.profile_screen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.canhub.cropper.CropImageContract
import com.lofigroup.seeyau.common.ui.components.YesNoChoiceDialog
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.LikesLabel
import com.lofigroup.seeyau.common.ui.components.UpButton
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.components.*
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.util.getDefaultImageCropperOptions
import com.sillyapps.core.ui.util.universalBackground
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.lofigroup.seeyau.common.ui.R as CommonR

@Composable
fun ProfileScreen(
  stateHolder: ProfileScreenStateHolder,
  onUpButtonClick: () -> Unit,
  onNavigateToAuthScreen: () -> Unit,
  isFocused: Boolean
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = ProfileScreenState())

  var faqVisible by rememberSaveable() {
    mutableStateOf(false)
  }

  var deleteAccountDialogVisible by rememberSaveable() {
    mutableStateOf(false)
  }

  val context = LocalContext.current

  val pickImageResult =
    rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
      if (result.isSuccessful) {
        val uri = result.uriContent
          ?: return@rememberLauncherForActivityResult stateHolder.throwError(
            context.getString(
              CommonR.string.image_crop_error
            )
          )

        stateHolder.setImageUri(uri)
      } else {
        stateHolder.throwError(context.getString(CommonR.string.image_crop_error))
      }
    }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .universalBackground(MaterialTheme.colors.background)
      .systemBarsPadding()
  ) {
    DefaultTopBar(
      leftContent = { UpButton(onClick = onUpButtonClick) }
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

    UserIcon(
      imageUri = state.imageUrl,
      onClick = { pickImageResult.launch(getDefaultImageCropperOptions()) }
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

    LikesLabel(count = state.likesCount)

    Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

    SettingButton(
      onClick = { faqVisible = true },
      iconResId = R.drawable.ic_faq,
      label = stringResource(id = R.string.faq)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

    SettingFolder(
      iconResId = R.drawable.ic_account_edit,
      label = stringResource(id = R.string.account)
    ) {
      TextButton(
        onClick = { deleteAccountDialogVisible = true }
      ) {
        Text(
          text = stringResource(id = R.string.delete_account),
          style = MaterialTheme.typography.h4,
          color = LocalExtendedColors.current.darkBackground
        )
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    VisibilitySwitch(
      visible = state.isVisible,
      onSetVisibilityState = stateHolder::setVisibility
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

  }

  YesNoChoiceDialog(
    visible = deleteAccountDialogVisible,
    onConfirm = {
      stateHolder.deleteAccount()
      onNavigateToAuthScreen()
    },
    onDismiss = { deleteAccountDialogVisible = false },
    title = stringResource(id = R.string.delete_account_question)
  )

  if (faqVisible) {
    FaqScreen(onUpButtonClick = { faqVisible = false })
  }

  if (isFocused)
    BackHandler(onBack = onUpButtonClick)

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

    override fun throwError(errorMessage: String) {
      state.value = state.value.copy(errorMessage = errorMessage)
    }

    override fun setImageUri(uri: Uri) {

    }

    override fun setVisibility(isVisible: Boolean) {

    }

    override fun deleteAccount() {

    }

  }

  AppTheme() {
    Surface() {
      ProfileScreen(
        stateHolder = stateHolder,
        onUpButtonClick = {},
        onNavigateToAuthScreen = {},
        isFocused = true
      )
    }
  }

}