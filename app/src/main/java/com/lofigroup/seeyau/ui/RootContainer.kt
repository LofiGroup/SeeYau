package com.lofigroup.seeyau.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.ui.BottomBar
import com.lofigroup.seeyau.ui.navigation.AppNavHost

@Composable
fun RootContainer(
  navigatorComponent: NavigatorComponent
) {
  val navController = rememberNavController()

  Scaffold(
    bottomBar = { BottomBar(navController) },
    modifier = Modifier.fillMaxSize(),
  ) {
    AppNavHost(
      navController = navController,
      navigatorComponent = navigatorComponent,
      modifier = Modifier.padding(it)
    )
  }
}