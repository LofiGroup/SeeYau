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
import androidx.activity.viewModels
import com.lofigroup.features.nearby_service.NearbyService
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App
import com.lofigroup.seeyau.features.data_sync_service.DataSyncService
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl
import com.sillyapps.core.ui.Factory
import com.sillyapps.core.ui.daggerViewModel
import com.sillyapps.core.ui.service.ServiceModuleConnection
import com.sillyapps.core.ui.util.hasPermissions
import timber.log.Timber
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  private val nearbyServiceConnection =
    ServiceModuleConnection<NearbyService>(
      NearbyServiceImpl::class.java,
      onServiceConnected = { it.startDiscoveringNearbyDevices() }
    )
  private val dataSyncServiceConnection =
    ServiceModuleConnection<DataSyncService>(
      DataSyncServiceImpl::class.java,
      onServiceConnected = { it.sync() }
    )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val app = (application as App)

    setContent {
      AppTheme() {
        RootContainer(
          appModules = app.appModules,
          onStart = {
            bindServices()
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

  override fun onStop() {
    super.onStop()
    nearbyServiceConnection.unbind(this)
    dataSyncServiceConnection.unbind(this)
  }

  private fun bindServices() {
    nearbyServiceConnection.bind(this)
    dataSyncServiceConnection.bind(this)
  }

}