package com.lofigroup.seeyau.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
  val route: String,
  val imageVector: ImageVector
  ) {

  object PagerScreen: Screen(
    route = "pager_screen",
    imageVector = Icons.Filled.Search
  )

  object AuthScreen: Screen(
    route = "auth_screen",
    imageVector = Icons.Filled.Login
  )

  object SplashScreen: Screen(
    route = "splash_screen",
    imageVector = Icons.Filled.WavingHand
  )

}
