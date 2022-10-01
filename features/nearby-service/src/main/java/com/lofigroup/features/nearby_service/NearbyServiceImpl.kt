package com.lofigroup.features.nearby_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.features.nearby_service.di.DaggerNearbyServiceComponent
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.settings.api.SettingsComponentProvider
import com.lofigroup.seeyau.domain.settings.usecases.GetVisibilityUseCase
import com.lofigroup.seeyau.features.data_sync_service.DataSyncService
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceState
import com.sillyapps.core.ui.service.ServiceBinder
import com.sillyapps.core.ui.service.ServiceModuleConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NearbyServiceImpl : Service(), NearbyService {

  @Inject
  @JvmField
  var nearbyBtClient: NearbyBtClient? = null

  @Inject
  lateinit var getVisibilityUseCase: GetVisibilityUseCase

  private val serviceJob = Job()
  private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val dataSyncServiceConnection =
    ServiceModuleConnection<DataSyncService>(
      DataSyncServiceImpl::class.java,
      serviceIsConnected = { service ->
        observeDataSyncServiceState(service)
      }
    )

  private val binder = LocalBinder()

  private var isInitialized: Boolean = false

  override fun onBind(p0: Intent?): IBinder {
    return binder
  }

  inner class LocalBinder : Binder(), ServiceBinder<NearbyService> {
    override fun getService(): NearbyService = this@NearbyServiceImpl
  }

  override fun onCreate() {
    super.onCreate()
    dataSyncServiceConnection.bind(this)
  }

  private fun startDiscovery() {
    if (!isInitialized) {
      Timber.e("Nearby service is not initialized!")
      return
    }
    Timber.e("Starting discovery...")
    nearbyBtClient?.startDiscovery()
  }

  private fun stopDiscovery() {
    if (!isInitialized) {
      Timber.e("Nearby service is not initialized!")
      return
    }
    Timber.e("Stopping discovery...")
    nearbyBtClient?.stopDiscovery()
  }

  private fun observeDataSyncServiceState(service: DataSyncService) {
    scope.launch {
      service.getState().collect() { state ->
        when (state) {
          DataSyncServiceState.SYNCED -> {
            init()
          }
          else -> {}
        }
      }
    }
  }

  private fun observeVisibilitySetting() {
    scope.launch {
      getVisibilityUseCase().collect { visibility ->
        if (visibility.isVisible)
          startDiscovery()
        else
          stopDiscovery()
      }
    }
  }

  private fun init() {
    if (isInitialized) return

    val component = DaggerNearbyServiceComponent.builder()
      .navigatorComponent((application as NavigatorComponentProvider).provideNavigatorComponent())
      .profileComponent((application as ProfileComponentProvider).provideProfileComponent())
      .settingsComponent((application as SettingsComponentProvider).provideSettingsModule())
      .context(this)
      .coroutineScope(scope)
      .build()

    component.inject(this)
    isInitialized = true
    Timber.e("Nearby service is initialized")

    observeVisibilitySetting()
  }

  override fun onDestroy() {
    super.onDestroy()
    dataSyncServiceConnection.unbind(this)
    nearbyBtClient?.stopDiscovery()
    serviceJob.cancel()
  }
}