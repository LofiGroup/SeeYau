package com.lofigroup.seeyau.common.ui.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.lofigroup.seeyau.common.ui.permissions.model.AppPermission
import com.lofigroup.seeyau.common.ui.permissions.model.PermissionRationale
import com.sillyapps.core.ui.util.hasPermissions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalCoroutinesApi::class)
class PermissionRequestChannel() {

  private var mTarget: ComponentActivity? = null
  private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

  private var showRationaleToUserCallback: ((PermissionRationale) -> Unit)? = null

  private val permissionResultChannel = MutableSharedFlow<Boolean>(replay = 1)
  private val rationaleResultChannel = MutableSharedFlow<Unit>(replay = 1)

  private suspend fun showRationaleToUser(rationale: PermissionRationale) {
    if (showRationaleToUserCallback == null) return

    rationaleResultChannel.resetReplayCache()
    showRationaleToUserCallback?.invoke(rationale)
    return rationaleResultChannel.first()
  }

  suspend fun requestPermission(permission: AppPermission): Boolean {
    val target = mTarget ?: return false

    if (!hasPermissions(target, permission.permissions)) {
      if (permission.rationale != null) {
        showRationaleToUser(permission.rationale)
      }

      permissionResultChannel.resetReplayCache()
      requestPermissionLauncher?.launch(permission.permissions)
    } else {
      return true
    }
    return permissionResultChannel.first()
  }

  fun registerForPermissions(target: ComponentActivity) {
    mTarget = target

    requestPermissionLauncher = target.registerForActivityResult(
      ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
      val allIsGranted = isGranted.all { it.value }
      permissionResultChannel.tryEmit(allIsGranted)
    }
  }

  fun registerRationaleCallback(onShowRationaleToUser: (PermissionRationale) -> Unit) {
    showRationaleToUserCallback = onShowRationaleToUser
  }

  fun unregister() {
    mTarget = null
    requestPermissionLauncher = null
    showRationaleToUserCallback = null
  }

  fun notifyRationaleIsClosed() {
    rationaleResultChannel.tryEmit(Unit)
  }

}