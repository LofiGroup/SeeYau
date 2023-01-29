package com.lofigroup.features.nearby_service.search_mode

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lofigroup.features.nearby_service.search_mode.model.BtSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

class BtSettingsDataSourceImpl @Inject constructor(): DefaultLifecycleObserver, BtSettingsDataSource {

  private val state = MutableStateFlow(BtSettings.DefaultSetting)

  override fun init() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }

  override fun destroy() {
    ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
  }

  override fun getState(): StateFlow<BtSettings> = state

  override fun onStart(owner: LifecycleOwner) {
    Timber.e("App goes into foreground, setting Normal mode")
    state.value = BtSettings.DefaultSetting
  }

  override fun onStop(owner: LifecycleOwner) {
    Timber.e("App goes into background, setting BatterySave mode")
    state.value = BtSettings.LowConsumptionSetting
  }

}