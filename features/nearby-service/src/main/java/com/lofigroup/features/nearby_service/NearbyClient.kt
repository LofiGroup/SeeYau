package com.lofigroup.features.nearby_service

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.lofigroup.domain.navigator.usecases.GetMyProfileUseCase
import com.lofigroup.domain.navigator.usecases.NotifyDeviceIsLostUseCase
import com.lofigroup.domain.navigator.usecases.NotifyUserIsNearbyUseCase
import com.lofigroup.features.nearby_service.model.UserSerializableModel
import com.lofigroup.features.nearby_service.model.toSerializableModel
import com.lofigroup.features.nearby_service.model.toUser
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NearbyClient @Inject constructor(
  private val context: Context,
  private val notifyUserIsNearbyUseCase: NotifyUserIsNearbyUseCase,
  private val notifyDeviceIsLostUseCase: NotifyDeviceIsLostUseCase,
  private val getMyProfileUseCase: GetMyProfileUseCase,
  private val scope: CoroutineScope
) {

  private val connectionsClient = Nearby.getConnectionsClient(context)

  private val adapter = Moshi.Builder().build().adapter(UserSerializableModel::class.java)

  private var isSearching = false

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

        val data = getMyProfileUseCase().toSerializableModel().copy(isNear = true)

        val json = adapter.toJson(data)
        Timber.d("Sending payload: $json")

        connectionsClient.sendPayload(endpointId, Payload.fromBytes(json.toByteArray()))
      }
    }

    override fun onDisconnected(endpointId: String) {
      Timber.d("Disconnecting from device with id: $endpointId")
      scope.launch {
        notifyDeviceIsLostUseCase(endpointId)
      }
    }

  }

  fun startBroadcast() {
    if (isSearching) return

    connectionsClient.startDiscovery(
      context.packageName,
      endpointDiscoveryCallback,
      DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
    )
    connectionsClient.startAdvertising(
      getMyProfileUseCase().id.toString(),
      context.packageName,
      connectionLifecycleCallback,
      AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
    )
    isSearching = true
  }

  fun stopBroadcast() {
    connectionsClient.stopDiscovery()
    connectionsClient.stopAdvertising()
    isSearching = false
  }

}