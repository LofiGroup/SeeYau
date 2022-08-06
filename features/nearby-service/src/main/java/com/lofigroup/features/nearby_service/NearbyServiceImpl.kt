package com.lofigroup.features.nearby_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.usecases.GetMyProfileUseCase
import com.lofigroup.domain.navigator.usecases.NotifyDeviceIsLostUseCase
import com.lofigroup.domain.navigator.usecases.NotifyUserIsNearbyUseCase
import com.lofigroup.features.nearby_service.di.DaggerNearbyServiceComponent
import com.lofigroup.features.nearby_service.model.UserSerializableModel
import com.lofigroup.features.nearby_service.model.toSerializableModel
import com.lofigroup.features.nearby_service.model.toUser
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NearbyServiceImpl : Service(), NearbyService {

  @Inject
  lateinit var nearbyClient: NearbyClient

  private val serviceJob = Job()
  private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val binder = LocalBinder()

  override fun onBind(p0: Intent?): IBinder {
    return binder
  }

  override fun onCreate() {
    super.onCreate()

    val app = application as NavigatorComponentProvider

    val component = DaggerNearbyServiceComponent.builder()
      .navigatorComponent(app.provideNavigatorComponent())
      .context(this)
      .coroutineScope(scope)
      .build()

    component.inject(this)
  }

  inner class LocalBinder : Binder() {
    fun getService(): NearbyService = this@NearbyServiceImpl
  }

  override fun startDiscoveringNearbyDevices() {
    nearbyClient.startBroadcast()
  }

  override fun onDestroy() {
    super.onDestroy()
    nearbyClient.stopBroadcast()
    serviceJob.cancel()
  }
}