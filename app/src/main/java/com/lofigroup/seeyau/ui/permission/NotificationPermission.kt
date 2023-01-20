package com.lofigroup.seeyau.ui.permission

import android.Manifest
import android.os.Build
import com.lofigroup.core.permission.model.AppPermission

object NotificationPermission : AppPermission(
  permissions =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(
      Manifest.permission.POST_NOTIFICATIONS
    )
    else arrayOf()
)