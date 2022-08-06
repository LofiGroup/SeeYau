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
  lateinit var notifyUserIsNearbyUseCase: NotifyUserIsNearbyUseCase

  @Inject
  lateinit var getMyProfileUseCase: GetMyProfileUseCase

  @Inject
  lateinit var notifyDeviceIsLostUseCase: NotifyDeviceIsLostUseCase

  private var isSearching = false

  private val serviceJob = Job()
  private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val binder = LocalBinder()

  private val connectionsClient by lazy {
    Nearby.getConnectionsClient(this)
  }

  private val adapter = Moshi.Builder().build().adapter(UserSerializableModel::class.java)

  private val payloadCallback = object : PayloadCallback() {
    override fun onPayloadReceived(endpointId: String, payload: Payload) {
      val json = String(payload.asBytes()!!)

      Timber.d("payload received: $json")
      val data = adapter.fromJson(json)?.toUser(endpointId)

      if (data == null) {
        Timber.e("Payload data is null!")
        return
      }

      scope.launch {
        notifyUserIsNearbyUseCase(data)
      }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

    }

  }

  private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
    override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
      Timber.d("Endpoint found: $endpointId")
      connectionsClient.requestConnection(
        getMyProfileUseCase().id.toString(),
        endpointId,
        connectionLifecycleCallback
      )
    }

    override fun onEndpointLost(endpointId: String) {
      Timber.d("Endpoint lost: $endpointId")
      scope.launch {
        notifyDeviceIsLostUseCase(endpointId)
      }
    }

  }

  private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
      Timber.d("Accepting connection with device with id: $endpointId")
      connectionsClient.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
      if (result.status.isSuccess) {
        Timber.d("Successfully connected to device with id: $endpointId")

        val data = getMyProfileUseCase().toSerializableModel()

        val json = adapter.toJson(data)
        Timber.d("Sending payload: $json")

        connectionsClient.sendPayload(endpointId, Payload.fromBytes(json.toByteArray()))
      }
    }

    override fun onDisconnected(endpointId: String) {
      Timber.d("Disconnecting from device with id: $endpointId")
    }

  }

  override fun onBind(p0: Intent?): IBinder {
    return binder
  }

  override fun onCreate() {
    super.onCreate()

    val app = application as NavigatorComponentProvider

    val component = DaggerNearbyServiceComponent.builder()
      .navigatorComponent(app.provideNavigatorComponent())
      .build()

    component.inject(this)
  }

  inner class LocalBinder : Binder() {
    fun getService(): NearbyService = this@NearbyServiceImpl
  }

  override fun startDiscoveringNearbyDevices() {
    if (isSearching) return

    connectionsClient.startDiscovery(
      packageName,
      endpointDiscoveryCallback,
      DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
    )
    connectionsClient.startAdvertising(
      getMyProfileUseCase().id.toString(),
      packageName,
      connectionLifecycleCallback,
      AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
    )
    isSearching = true
  }

  override fun onDestroy() {
    super.onDestroy()

    connectionsClient.stopDiscovery()
    connectionsClient.stopAdvertising()
    serviceJob.cancel()
    isSearching = false
  }
}