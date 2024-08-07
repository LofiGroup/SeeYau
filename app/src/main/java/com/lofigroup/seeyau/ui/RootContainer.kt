package com.lofigroup.seeyau.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lofigroup.seeyau.AppModules
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.ui.navigation.AppNavHost

@Composable
fun RootContainer(
  appModules: AppModules,
  mainScreenEvent: MainScreenEvent?,
  onStartNearbyService: () -> Unit
) {
  val navController = rememberNavController()

  Scaffold(
    topBar = {},
    modifier = Modifier
      .fillMaxSize()
      .padding(),
  ) {
    AppNavHost(
      mainScreenEvent,
      navController = navController,
      appModules = appModules,
      onStartNearbyService = onStartNearbyService,
      modifier = Modifier.padding(it)
    )
  }
}