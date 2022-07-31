package com.lofigroup.seeyau.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
  val route: String,
  val imageVector: ImageVector
  ) {

  object NavigatorScreen: Screen(
    route = "navigator_screen",
    imageVector = Icons.Filled.Search
  )

  object DialogScreen: Screen(
    route = "dialog_screen",
    imageVector = Icons.Filled.Message
  )

  object NotificationScreen: Screen(
    route = "notification_screen",
    imageVector = Icons.Filled.Notifications
  )

  object SettingsScreen: Screen(
    route = "settings_screen",
    imageVector = Icons.Filled.Settings
  )

}
