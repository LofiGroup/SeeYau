package com.lofigroup.seeyau.features.profile_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.canhub.cropper.CropImageContract
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.components.LikesLabel
import com.lofigroup.seeyau.common.ui.components.UpButton
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.component.SettingButton
import com.lofigroup.seeyau.features.profile_screen.component.UserIcon
import com.lofigroup.seeyau.features.profile_screen.component.VisibilitySwitch
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
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
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = ProfileScreenState())

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
      title = stringResource(id = R.string.settings),
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
      onClick = {  },
      iconResId = R.drawable.ic_fluent_person_edit_24_filled,
      label = stringResource(id = R.string.account)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

    SettingButton(
      onClick = {  },
      iconResId = R.drawable.ic_fluent_person_key_20_filled,
      label = stringResource(id = R.string.privacy_policy)
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

    SettingButton(
      onClick = {  },
      iconResId = R.drawable.ic_fluent_person_star_48_filled,
      label = stringResource(id = R.string.about_app)
    )

    Spacer(modifier = Modifier.weight(1f))

    VisibilitySwitch(
      visible = state.isVisible,
      onSetVisibilityState = stateHolder::setVisibility
    )

    Spacer(modifier = Modifier.height(LocalSpacing.current.large))

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

    override fun throwError(errorMessage: String) {
      state.value = state.value.copy(errorMessage = errorMessage)
    }

    override fun setImageUri(uri: Uri) {

    }

    override fun setVisibility(isVisible: Boolean) {

    }

  }

  AppTheme() {
    Surface() {
      ProfileScreen(
        stateHolder = stateHolder,
        onUpButtonClick = {}
      )
    }
  }

}