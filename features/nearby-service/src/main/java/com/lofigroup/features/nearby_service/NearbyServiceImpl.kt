package com.lofigroup.features.nearby_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.lofigroup.core.bluetooth.BluetoothRequesterChannel
import com.lofigroup.core.bluetooth.BluetoothRequesterProvider
import com.lofigroup.core.util.ResourceState
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.features.nearby_service.di.DaggerNearbyServiceComponent
import com.lofigroup.core.permission.PermissionRequestChannelProvider
import com.lofigroup.core.permission.model.BluetoothPermission
import com.lofigroup.features.nearby_service.notification.NearbyServiceNotificationBuilder
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.notifications.NotificationRequesterProvider
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannelProvider
import com.lofigroup.seeyau.domain.base.api.BaseComponentProvider
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.settings.api.SettingsComponentProvider
import com.lofigroup.seeyau.domain.settings.usecases.GetVisibilityUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NearbyServiceImpl : Service(), NearbyService {

  @Inject
  lateinit var nearbyBtClient: NearbyBtClient

  @Inject
  lateinit var notificationBuilder: NearbyServiceNotificationBuilder

  @Inject
  lateinit var getVisibilityUseCase: GetVisibilityUseCase

  private val serviceJob = Job()
  private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val binder = LocalBinder()
  private val state = MutableStateFlow(ResourceState.LOADING)
  private val canStart = MutableStateFlow(false)

  private val permissionChannel by lazy { (application as PermissionRequestChannelProvider).providePermissionChannel() }
  private val bluetoothRequester by lazy { (application as BluetoothRequesterProvider).provideBluetoothRequester() }

  override fun onBind(p0: Intent?): IBinder {
    return binder
  }

  inner class LocalBinder : Binder() {
    fun getService(): NearbyService = this@NearbyServiceImpl
  }

  override fun onCreate() {
    super.onCreate()
    observeDataSyncState()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (intent == null) return START_STICKY_COMPATIBILITY

    if (state.value == ResourceState.LOADING) return START_NOT_STICKY

    when (intent.action) {
      ACTION_BLUETOOTH_IS_ON -> {
        canStart.value = true
      }
      ACTION_BLUETOOTH_IS_OFF -> {
        canStart.value = false
      }
      ACTION_STOP -> {
        stopForegroundCompat()
      }
    }

    return START_NOT_STICKY
  }

  private fun startDiscovery() {
    if (state.value == ResourceState.LOADING) {
      Timber.e("Trying to start Nearby service while it is not initialized!")
      return
    }
    Timber.e("Starting discovery...")
    scope.launch {
      val isGranted = permissionChannel.requestPermission(BluetoothPermission)
      if (!isGranted) return@launch

      if (!nearbyBtClient.bluetoothIsOn()) {
        val bluetoothIsOn = bluetoothRequester.requestToTurnBluetoothOn()

        if (!bluetoothIsOn) return@launch
      }

      nearbyBtClient.startDiscovery()
      goForeground()
    }
  }

  private fun stopDiscovery() {
    if (state.value == ResourceState.LOADING) {
      Timber.e("Trying to stop Nearby service while it is not initialized!")
      return
    }
    nearbyBtClient.stopDiscovery()

    stopForegroundCompat()
  }

  private fun observeDataSyncState() {
    val baseComponent = (application as BaseComponentProvider).provideBaseComponent()
    scope.launch {
      baseComponent.moduleStateHolder().observe().collect()  { state ->
        when (state) {
          ResourceState.IS_READY, ResourceState.INITIALIZED -> {
            init()
          }
          else -> {}
        }
      }
    }
  }

  private fun goForeground() {
    startForeground(SERVICE_ID, notificationBuilder.getNotification())
  }

  private fun observeVisibilitySetting() {
    scope.launch {
      combine(
        getVisibilityUseCase(),
        canStart
      ) { visibility, canStart -> Pair(visibility, canStart) }.collect { (visibility, canStart) ->
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
      .mainScreenEventChannel((application as MainScreenEventChannelProvider).providerMainScreenEventChannel())
      .notificationRequester((application as NotificationRequesterProvider).provideNotificationRequester())
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
    if (state.value != ResourceState.LOADING) {
      nearbyBtClient.destroy()
      serviceJob.cancel()
    }

    super.onDestroy()
  }

  override fun observeState(): Flow<ResourceState> {
    return state
  }

  override fun start() {
    canStart.value = true
  }

  companion object {
    const val SERVICE_ID = 1

    const val ACTION_BLUETOOTH_IS_ON = "BluetoothIsOn"
    const val ACTION_BLUETOOTH_IS_OFF = "BluetoothIsOff"
    const val ACTION_STOP = "NearbyServiceStop"
  }
}