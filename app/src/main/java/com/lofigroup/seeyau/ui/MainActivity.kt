package com.lofigroup.seeyau.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import coil.imageLoader
import com.lofigroup.core.bluetooth.BluetoothRequesterChannel
import com.lofigroup.core.bluetooth.BluetoothRequesterProvider
import com.lofigroup.core.permission.PermissionRequestChannelProvider
import com.lofigroup.features.nearby_service.NearbyService
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seeyau.common.ui.components.specific.ExplainPermissionDialog
import com.lofigroup.core.permission.model.PermissionRationale
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.main_screen_event_channel.MainScreenEventChannelImpl
import com.sillyapps.core.ui.components.showToast
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  private val permissionChannel by lazy {
    (application as PermissionRequestChannelProvider).providePermissionChannel()
  }

  private val bluetoothRequester by lazy {
    (application as BluetoothRequesterProvider).provideBluetoothRequester()
  }

  private var nearbyService: NearbyService? = null

  @Inject lateinit var viewModel: MainViewModel

  private val nearbyServiceConnection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
      val binder = service as NearbyServiceImpl.LocalBinder
      val boundService = binder.getService()

      nearbyService = boundService
    }

    override fun onServiceDisconnected(arg0: ComponentName) {
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    permissionChannel.registerForPermissions(target = this)
    bluetoothRequester.registerActivity(target = this)

    startServices()
    bindServices()

    val mainScreenEvent = getMainScreenEvent()

    val app = (application as App)

    app.appModules.mainActivityComponent.inject(this)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      var rationale by remember {
        mutableStateOf<PermissionRationale?>(null)
      }

      permissionChannel.registerRationaleCallback { rationale = it }

      val context = LocalContext.current
      LaunchedEffect(Unit) {
        val baseComponent = app.appModules.baseDataModule.domainComponent
        baseComponent.getUserNotificationChannel().observe().collect() {
          showToast(context, it.message)
        }
      }

      AppTheme() {
        RootContainer(
          appModules = app.appModules,
          onStartNearbyService = {
            nearbyService?.start()
          },
          mainScreenEvent = mainScreenEvent
        )

        ExplainPermissionDialog(
          rationale = rationale,
          onDismiss = {
            rationale = null
            permissionChannel.notifyRationaleIsClosed()
          }
        )
      }
    }
  }

  private fun getMainScreenEvent(): MainScreenEvent? {
    return MainScreenEvent.deserialize(intent.getStringExtra(MainScreenEventChannelImpl.INTENT_EXTRA_ID))
  }

  private fun startServices() {
    Intent(this, NearbyServiceImpl::class.java).also { intent ->
      startService(intent)
    }
  }

  override fun onDestroy() {
    unbindServices()
    permissionChannel.unregister()
    bluetoothRequester.unregister()
    imageLoader.shutdown()

    (application as App).appModules.navigatorModule.domainComponent.getRepository().disconnectFromWebsocket()

    super.onDestroy()
  }

  private fun bindServices() {
    Intent(this, NearbyServiceImpl::class.java).also {
      bindService(it, nearbyServiceConnection, Context.BIND_AUTO_CREATE)
    }
  }

  private fun unbindServices() {
    unbindService(nearbyServiceConnection)
  }

}