package com.lofigroup.seeyau.ui

import android.content.Intent
import android.os.Bundle
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
import com.sillyapps.core.ui.service.ServiceModuleConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val job = Job()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  private val permissionChannel by lazy {
    (application as App).appModules.appComponent.getPermissionChannel()
  }

  private val nearbyServiceConnection =
    ServiceModuleConnection<NearbyService>(
      NearbyServiceImpl::class.java,
      serviceIsConnected = {}
    )
  private val dataSyncServiceConnection =
    ServiceModuleConnection<DataSyncService>(
      DataSyncServiceImpl::class.java,
      serviceIsConnected = {}
    )

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
            nearbyServiceConnection.boundService?.start()
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
    dataSyncServiceConnection.bind(this)
    nearbyServiceConnection.bind(this)
  }

  private fun unbindServices() {
    dataSyncServiceConnection.unbind(this)
    nearbyServiceConnection.unbind(this)
  }

}