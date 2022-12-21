package com.lofigroup.seeyau.features.chat_screen.ui.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.DefaultTopBar
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.domain.profile.model.Profile

@Composable
fun TopBar(
  profile: Profile,
  onProfileButtonClick: () -> Unit
) {
  DefaultTopBar(
    leftContent = {
      ProfileSettingsButton(
        profile = profile,
        onClick = onProfileButtonClick
      )
    }
  )
}

@Preview
@Composable
fun TopBarPreview() {
  AppTheme() {
    Surface() {
      TopBar(
        profile = Profile(0L, "", "", 1),
        onProfileButtonClick = {}
      )
    }
  }
}