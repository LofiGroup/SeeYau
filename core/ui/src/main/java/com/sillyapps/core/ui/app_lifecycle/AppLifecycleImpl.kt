package com.sillyapps.core.ui.app_lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.sillyapps.core.ui.app_lifecycle.model.AppLifecycleState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class AppLifecycleImpl: AppLifecycle, DefaultLifecycleObserver {

  private val state = MutableStateFlow(AppLifecycleState.ON_FOREGROUND)

  override fun observeLifecycle() = state

  fun init() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }

  override fun onStart(owner: LifecycleOwner) {
    Timber.e("App goes into foreground")
    state.value = AppLifecycleState.ON_FOREGROUND
  }

  override fun onStop(owner: LifecycleOwner) {
    Timber.e("App goes into background")
    state.value = AppLifecycleState.ON_BACKGROUND
  }

}