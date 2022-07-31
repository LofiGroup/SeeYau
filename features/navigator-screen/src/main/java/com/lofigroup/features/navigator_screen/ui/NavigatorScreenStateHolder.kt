package com.lofigroup.features.navigator_screen.ui

import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import kotlinx.coroutines.flow.Flow

interface NavigatorScreenStateHolder {

  fun getState(): Flow<NavigatorScreenState>

}