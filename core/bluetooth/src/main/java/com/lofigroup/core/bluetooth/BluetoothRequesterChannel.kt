package com.lofigroup.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class BluetoothRequesterChannel {
  private var mTarget: ComponentActivity? = null

  private var requestToTurnBluetoothOnLauncher: ActivityResultLauncher<Intent>? = null

  fun registerActivity(target: ComponentActivity) {
    mTarget = target

    requestToTurnBluetoothOnLauncher = target.registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) {}
  }

  fun requestToTurnBluetoothOn() {
    requestToTurnBluetoothOnLauncher?.launch(turnBluetoothOnIntent)
  }

  fun unregister() {
    mTarget = null
    requestToTurnBluetoothOnLauncher?.unregister()
    requestToTurnBluetoothOnLauncher = null
  }

  companion object {
    private val turnBluetoothOnIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
  }
}