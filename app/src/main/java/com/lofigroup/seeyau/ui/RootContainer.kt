package com.lofigroup.seeyau.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.ui.navigation.AppNavHost

@Composable
fun RootContainer(
  navigatorComponent: NavigatorComponent,
  authComponent: AuthComponent,
  profileComponent: ProfileComponent,
  startNearbyService: () -> Unit
) {
  val navController = rememberNavController()

  val (bottomBarIsVisible, setBottomBarVisibility) = remember {
    mutableStateOf(false)
  }

  Scaffold(
    bottomBar = {
      if (bottomBarIsVisible)
        BottomBar(navController)
    },
    modifier = Modifier.fillMaxSize(),
  ) {
    AppNavHost(
      navController = navController,
      navigatorComponent = navigatorComponent,
      authComponent = authComponent,
      profileComponent = profileComponent,
      startNearbyService = startNearbyService,
      modifier = Modifier.padding(it),
      setBottomBarVisibility = setBottomBarVisibility,
    )
  }
}