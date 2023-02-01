package com.lofigroup.core.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class BluetoothRequesterChannel {
  private var mTarget: ComponentActivity? = null

  private var requestToTurnBluetoothOnLauncher: ActivityResultLauncher<Intent>? = null

  private val resultChannel = MutableSharedFlow<Boolean>(replay = 1)

  fun registerActivity(target: ComponentActivity) {
    mTarget = target

    requestToTurnBluetoothOnLauncher = target.registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) {
      resultChannel.tryEmit(it.resultCode == Activity.RESULT_OK)
    }
  }

  suspend fun requestToTurnBluetoothOn(): Boolean {
    requestToTurnBluetoothOnLauncher?.launch(turnBluetoothOnIntent)

    return resultChannel.first()
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