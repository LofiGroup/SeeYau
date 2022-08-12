package com.lofigroup.features.nearby_service

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.messages.*
import com.lofigroup.domain.navigator.model.User
import com.lofigroup.domain.navigator.usecases.GetMyProfileUseCase
import com.lofigroup.domain.navigator.usecases.NotifyDeviceIsLostUseCase
import com.lofigroup.domain.navigator.usecases.NotifyUserIsNearbyUseCase
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class NearbyClient @Inject constructor(
  private val context: Context,
  private val notifyUserIsNearbyUseCase: NotifyUserIsNearbyUseCase,
  private val notifyDeviceIsLostUseCase: NotifyDeviceIsLostUseCase,
  private val getMyProfileUseCase: GetMyProfileUseCase,
  private val scope: CoroutineScope
) {

  private var isSearching = false

  private val btAdapter: BluetoothAdapter =
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

  private val advertiseSettings = AdvertiseSettings.Builder()
    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
    .setConnectable(false)
    .build()

  private val pUuid = ParcelUuid(UUID.fromString("000043ef-0000-1000-8000-00805F9B34FB"))

  private val advertiseData = run {
    val profile = getMyProfileUseCase()
    val data = profile.id.toByteArray()

    AdvertiseData.Builder()
      .addServiceUuid(pUuid)
      .addServiceData(pUuid, data)
      .build()
  }

  private val scanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
      super.onScanResult(callbackType, result)
      val data = result?.scanRecord?.getServiceData(pUuid)

      if (data == null) {
        Timber.d("BLE received incorrect data")
        return
      }

      val id = data.toLong()

      scope.launch {
        notifyUserIsNearbyUseCase(User(
          id, "", true, "", System.currentTimeMillis()
        ))
      }
    }

    override fun onScanFailed(errorCode: Int) {
      super.onScanFailed(errorCode)
      Timber.d("BLE scan failed. Error code: $errorCode")
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
      super.onBatchScanResults(results)
    }
  }

  private val advertisingCallback = object : AdvertiseCallback() {
    override fun onStartFailure(errorCode: Int) {
      super.onStartFailure(errorCode)
      Timber.d("BLE advertising failed. Error code: $errorCode")
    }

    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
      super.onStartSuccess(settingsInEffect)
      Timber.d("BLE advertising is successful")
    }
  }

  private fun advertise() {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
      != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Timber.e("Bluetooth advertise permission is not granted!")
      return
    }

    btAdapter.bluetoothLeAdvertiser.startAdvertising(advertiseSettings, advertiseData, advertisingCallback)
  }

  private fun scan() {
    val filter = ScanFilter.Builder()
      .setServiceUuid(pUuid)
      .build()

    val scanSettings = ScanSettings.Builder()
      .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
      .build()

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
      != PackageManager.PERMISSION_GRANTED  && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Timber.e("Bluetooth scan permission is not granted!")
      return
    }

    btAdapter.bluetoothLeScanner.startScan(listOf(filter), scanSettings, scanCallback)
  }

  private fun stopAdvertise() {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
      != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Timber.e("Bluetooth advertise permission is not granted!")
      return
    }
    btAdapter.bluetoothLeAdvertiser.stopAdvertising(advertisingCallback)
  }

  private fun stopScan() {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
      != PackageManager.PERMISSION_GRANTED  && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Timber.e("Bluetooth scan permission is not granted!")
      return
    }
    btAdapter.bluetoothLeScanner.stopScan(scanCallback)
  }

  fun startBroadcast() {
    if (isSearching) return

    scan()
    advertise()

    isSearching = true
  }

  fun stopBroadcast() {
    stopScan()
    stopAdvertise()

    isSearching = false
  }

}