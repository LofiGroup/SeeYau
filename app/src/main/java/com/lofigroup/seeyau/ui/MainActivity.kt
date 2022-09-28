package com.lofigroup.seeyau.ui

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.lofigroup.features.nearby_service.NearbyService
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App
import com.lofigroup.seeyau.features.data_sync_service.DataSyncService
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl
import com.sillyapps.core.ui.service.ServiceModuleConnection
import com.sillyapps.core.ui.util.ActivityBarHeights
import com.sillyapps.core.ui.util.getActivityBarHeights
import com.sillyapps.core.ui.util.hasPermissions
import timber.log.Timber
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  private val nearbyServiceConnection =
    ServiceModuleConnection<NearbyService>(
      NearbyServiceImpl::class.java,
      serviceIsConnected = {  }
    )
  private val dataSyncServiceConnection =
    ServiceModuleConnection<DataSyncService>(
      DataSyncServiceImpl::class.java,
      serviceIsConnected = {  }
    )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bindServices()
    startServices()

    val app = (application as App)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      AppTheme(
        activityBarHeights = getActivityBarHeights()
      ) {
        RootContainer(
          appModules = app.appModules,
          onAuthorized = {
          },
          onStartNearbyService = {
          }
        )
      }
    }

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

  override fun onStop() {
    super.onStop()

  }

  private fun bindServices() {

  }

}