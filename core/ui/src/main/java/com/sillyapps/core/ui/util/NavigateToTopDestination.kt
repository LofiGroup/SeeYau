package com.sillyapps.core.ui.util

import androidx.navigation.NavHostController

fun NavHostController.navigateToTopDestination(
  route: String
) {
  navigate(route = route) {
    popUpTo(0)
  }
}