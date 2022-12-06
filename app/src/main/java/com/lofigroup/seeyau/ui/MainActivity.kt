package com.lofigroup.seeyau.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.lofigroup.features.nearby_service.NearbyService
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seeyau.common.ui.components.specific.ExplainPermissionDialog
import com.lofigroup.seeyau.common.ui.permissions.model.PermissionRationale
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl

class MainActivity : ComponentActivity() {

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
    permissionChannel.registerForPermissions(target = this)

    startServices()
    bindServices()

    val app = (application as App)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      var rationale by remember {
        mutableStateOf<PermissionRationale?>(null)
      }

      permissionChannel.registerRationaleCallback { rationale = it }

      AppTheme() {
        RootContainer(
          appModules = app.appModules,
          onStartNearbyService = {
            nearbyService?.start()
          }
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