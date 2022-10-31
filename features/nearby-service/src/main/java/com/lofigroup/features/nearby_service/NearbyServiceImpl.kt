package com.lofigroup.features.nearby_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.lofigroup.core.util.ResourceState
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.features.nearby_service.di.DaggerNearbyServiceComponent
import com.lofigroup.seayau.common.ui.permissions.PermissionRequestChannel
import com.lofigroup.seayau.common.ui.permissions.PermissionRequestChannelProvider
import com.lofigroup.seayau.common.ui.permissions.model.BluetoothPermission
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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
  private val state = MutableStateFlow(ResourceState.LOADING)
  private val canStart = MutableStateFlow(false)

  private val permissionChannel by lazy { (application as PermissionRequestChannelProvider).providePermissionChannel() }

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
    if (state.value == ResourceState.LOADING) {
      Timber.e("Nearby service is not initialized!")
      return
    }
    Timber.e("Starting discovery...")
    scope.launch {
      val isGranted = permissionChannel.requestPermission(BluetoothPermission)
      Timber.e("Is granted = $isGranted")
      if (!isGranted) return@launch

      nearbyBtClient?.startDiscovery()
    }
  }

  private fun stopDiscovery() {
    if (state.value != ResourceState.LOADING) {
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
      Timber.e("Observing visibility")
      combine(
        getVisibilityUseCase(),
        canStart
      ) { visibility, canStart -> Pair(visibility, canStart) }.collect { (visibility, canStart) ->
        Timber.e("Visibility: ${visibility.isVisible}, canStart: $canStart")
        if (visibility.isVisible && canStart)
          startDiscovery()
        else
          stopDiscovery()
      }
    }
  }

  private fun init() {
    if (state.value != ResourceState.LOADING) return

    val component = DaggerNearbyServiceComponent.builder()
      .navigatorComponent((application as NavigatorComponentProvider).provideNavigatorComponent())
      .profileComponent((application as ProfileComponentProvider).provideProfileComponent())
      .settingsComponent((application as SettingsComponentProvider).provideSettingsModule())
      .context(this)
      .coroutineScope(scope)
      .build()

    component.inject(this)
    state.value = ResourceState.INITIALIZED
    Timber.e("Nearby service is initialized")

    observeVisibilitySetting()
  }

  override fun onDestroy() {
    Timber.e("Destroying NearbyService")
    dataSyncServiceConnection.unbind(this)
    nearbyBtClient?.stopDiscovery()
    serviceJob.cancel()

    super.onDestroy()
  }

  override fun observeState(): Flow<ResourceState> {
    return state
  }

  override fun start() {
    canStart.value = true
  }
}