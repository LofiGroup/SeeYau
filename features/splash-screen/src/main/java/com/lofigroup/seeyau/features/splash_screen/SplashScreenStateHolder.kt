package com.lofigroup.seeyau.features.splash_screen

import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import kotlinx.coroutines.flow.Flow

interface SplashScreenStateHolder {

  fun getState(): Flow<SplashScreenState>

}