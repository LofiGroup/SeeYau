package com.lofigroup.seeyau.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.lofigroup.core.util.ResourceState
import com.lofigroup.features.nearby_service.NearbyService
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App
import com.lofigroup.seeyau.features.data_sync_service.DataSyncService
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val job = Job()

  private val permissionChannel by lazy {
    (application as App).appModules.appComponent.getPermissionChannel()
  }

  private var nearbyService: NearbyService? = null

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
    permissionChannel.register(this)

    startServices()
    bindServices()

    val app = (application as App)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      AppTheme() {
        RootContainer(
          appModules = app.appModules,
          onStartNearbyService = {
            nearbyService?.start()
          }
        )
      }
    }
  }

  private fun startServices() {
    Intent(this, NearbyServiceImpl::class.java).also { intent ->
      startService(intent)
    }
    Intent(this, DataSyncServiceImpl::class.java).also { intent ->
      startService(intent)
    }
  }

  override fun onDestroy() {
    unbindServices()
    permissionChannel.unregister()
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