package com.lofigroup.seeyau.common.ui.permissions.model

import android.Manifest
import android.os.Build
import com.lofigroup.seeyau.common.ui.R

object BluetoothPermission : AppPermission(
  permissions = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT
      )
    }
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
      arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
      )
    }
    else -> {
      arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.ACCESS_COARSE_LOCATION,
      )
    }
  },
  rationale = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      PermissionRationale(
        titleResId = R.string.bluetooth_rationale_title,
        descriptionResId = R.string.bluetooth_rationale_description
      )
    }
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
      PermissionRationale(
        titleResId = R.string.bluetooth_rationale_title_api30,
        descriptionResId = R.string.bluetooth_rationale_description_api30
      )
    }
    else -> {
      null
    }
  }
)