package com.lofigroup.features.nearby_service.search_mode

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lofigroup.core.util.mapState
import com.lofigroup.features.nearby_service.search_mode.model.BtSettings
import com.sillyapps.core.ui.app_lifecycle.AppLifecycle
import com.sillyapps.core.ui.app_lifecycle.model.AppLifecycleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

class BtSettingsDataSourceImpl @Inject constructor(
  private val appLifecycle: AppLifecycle,
  private val scope: CoroutineScope
): BtSettingsDataSource {

  override fun getState(): StateFlow<BtSettings> = appLifecycle.observeLifecycle().mapState(scope, BtSettings.DefaultSetting) {
    when (it) {
      AppLifecycleState.ON_FOREGROUND -> BtSettings.DefaultSetting
      AppLifecycleState.ON_BACKGROUND -> BtSettings.LowConsumptionSetting
    }
  }

}