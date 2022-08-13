package com.lofigroup.seeyau.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lofigroup.features.nearby_service.NearbyService
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App
import com.sillyapps.core.ui.util.hasPermissions

class MainActivity : ComponentActivity() {

  private lateinit var nearbyService: NearbyService
  private var nearbyServiceIsBound: Boolean = false

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
      val binder = service as NearbyServiceImpl.LocalBinder
      nearbyService = binder.getService()
      nearbyServiceIsBound = true
      nearbyService.startDiscoveringNearbyDevices()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
      nearbyServiceIsBound = false
    }

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val app = (application as App)

    setContent {
      AppTheme() {
        RootContainer(
          navigatorComponent = app.navigatorComponent,
          authComponent = app.authComponent,
          startNearbyService = {
            Intent(this, NearbyServiceImpl::class.java).also {
              bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
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
    if (nearbyServiceIsBound) {
      unbindService(connection)
      nearbyServiceIsBound = false
    }
  }

}