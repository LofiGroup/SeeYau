package com.sillyapps.core.ui.util

import android.bluetooth.BluetoothManager
import android.content.Context

fun isBleSupported(context: Context): Boolean {
  val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
  return bluetoothManager.adapter.isMultipleAdvertisementSupported
}