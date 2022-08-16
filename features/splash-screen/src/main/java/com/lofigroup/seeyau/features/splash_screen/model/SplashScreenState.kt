package com.lofigroup.seeyau.features.splash_screen.model

data class SplashScreenState(
  val isLoggedIn: Boolean = false,
  val isReady: Boolean = false,
  val errorMessage: String? = null
)