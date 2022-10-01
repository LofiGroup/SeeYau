package com.sillyapps.core.ui.util

import androidx.navigation.NavHostController

fun NavHostController.navigateToTopDestination(
  route: String
) {
  navigate(route = route) {
    popUpTo(0)
  }
}

fun NavHostController.navigateBackTo(
  destinationRoute: String
) {
  val hasBackstackTheDestinationRoute = backQueue.find {
    it.destination.route == destinationRoute
  } != null

  if (hasBackstackTheDestinationRoute) {
    popBackStack()
  } else {
    navigate(destinationRoute) {
      currentBackStackEntry?.destination?.route?.let {
        popUpTo(it) {
          inclusive = true
        }
      }
    }
  }
}