package com.lofigroup.features.nearby_service.search_mode.model

import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanSettings

data class BtSettings(
  val advertisingSetting: AdvertiseSettings,
  val scanSetting: ScanSettings
) {
  companion object {
    val DefaultSetting = BtSettings(
      advertisingSetting = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setConnectable(false)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setTimeout(0)
        .build(),
      scanSetting = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//        .setReportDelay(5000L)
        .build()
    )

    val LowConsumptionSetting = BtSettings(
      advertisingSetting = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
        .setConnectable(false)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW)
        .setTimeout(0)
        .build(),
      scanSetting = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
//        .setReportDelay(10000L)
        .build()
    )
  }
}