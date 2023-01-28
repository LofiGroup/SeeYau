package com.lofigroup.features.nearby_service.broadcast_recievers

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import timber.log.Timber

class BluetoothStateBroadcastReceiver: BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context == null || intent == null || intent.action != BluetoothAdapter.ACTION_STATE_CHANGED) return

    val btAdapterState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
    if (btAdapterState == -1) return

    val nearbyServiceIntent = Intent(context, NearbyServiceImpl::class.java)

    val action = when (btAdapterState) {
      BluetoothAdapter.STATE_ON -> {
        NearbyServiceImpl.ACTION_BLUETOOTH_IS_ON
      }
      BluetoothAdapter.STATE_TURNING_OFF -> {
        NearbyServiceImpl.ACTION_BLUETOOTH_IS_OFF
      }
      else -> null
    }

    if (action != null) {
      nearbyServiceIntent.action = action
      context.startService(nearbyServiceIntent)
    }
  }
}