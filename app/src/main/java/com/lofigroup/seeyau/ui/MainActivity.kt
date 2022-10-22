package com.lofigroup.seeyau.ui

import android.content.Intent
import android.os.Build
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
import com.sillyapps.core.ui.util.hasPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val job = Job()
  private val scope = CoroutineScope(Dispatchers.Main + job)

  private val nearbyServiceConnection =
    ServiceModuleConnection<NearbyService>(
      NearbyServiceImpl::class.java,
      serviceIsConnected = {
        observeNearbyServiceState(it)
      }
    )
  private val dataSyncServiceConnection =
    ServiceModuleConnection<DataSyncService>(
      DataSyncServiceImpl::class.java,
      serviceIsConnected = {}
    )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startServices()
    requestPermissions()
    bindServices()

    val app = (application as App)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      AppTheme() {
        RootContainer(
          appModules = app.appModules,
          onAuthorized = {
          },
          onStartNearbyService = {
          }
        )
      }
    }
  }

  private fun observeNearbyServiceState(service: NearbyService) {
    scope.launch {
      service.observeState().collect() {
        when (it) {
          ResourceState.LOADING -> {}
          ResourceState.IS_READY -> {

          }
        }
      }
    }
  }

  private fun requestPermissions() {
    if (!hasPermissions(this, RequiredPermissions.permissions) &&
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    ) {
      requestPermissions(RequiredPermissions.permissions, 1)
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