package com.sillyapps.core.ui.app_lifecycle

import com.sillyapps.core.ui.app_lifecycle.model.AppLifecycleState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppLifecycle {

  fun observeLifecycle(): StateFlow<AppLifecycleState>

}