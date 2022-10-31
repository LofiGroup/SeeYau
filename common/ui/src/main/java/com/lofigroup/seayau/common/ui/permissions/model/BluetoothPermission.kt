package com.lofigroup.seayau.common.ui.permissions.model

import android.Manifest
import android.os.Build

object BluetoothPermission: AppPermission(
  permissions = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT
      )
    }
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
      arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
      )
    }
    else -> {
      arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
      )
    }
  }
)