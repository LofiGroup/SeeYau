package com.lofigroup.seayau.common.ui.permissions

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lofigroup.seayau.common.ui.permissions.model.AppPermission
import com.sillyapps.core.ui.util.hasPermissions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import timber.log.Timber

class PermissionRequestChannel() {

  private var mTarget: ComponentActivity? = null
  private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null
  private var permissionResultChannel = MutableSharedFlow<Boolean>(replay = 1)

  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun requestPermission(permission: AppPermission): Boolean {
    val target = mTarget ?: return false
    permissionResultChannel.resetReplayCache()

    if (!hasPermissions(target, permission.permissions)) {
      requestPermissionLauncher?.launch(permission.permissions)
    } else {
      return true
    }
    val result = permissionResultChannel.first()
    Timber.e("Received result: $result")
    return result
  }

  fun register(target: ComponentActivity) {
    mTarget = target
    requestPermissionLauncher = target.registerForActivityResult(
      ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
      Timber.e("Result is $isGranted")
      val allIsGranted = isGranted.all { it.value }
      Timber.e("Emitting: $allIsGranted")
      permissionResultChannel.tryEmit(allIsGranted)
    }
  }

  fun unregister() {
    mTarget = null
    requestPermissionLauncher = null
  }

}