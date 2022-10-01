package com.lofigroup.features.nearby_service

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import com.lofigroup.domain.navigator.usecases.NotifyDeviceIsLostUseCase
import com.lofigroup.domain.navigator.usecases.NotifyUserIsNearbyUseCase
import com.lofigroup.seeyau.domain.profile.usecases.GetMyIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
class NearbyBtClient @Inject constructor(
  private val context: Context,
  private val notifyUserIsNearbyUseCase: NotifyUserIsNearbyUseCase,
  private val notifyDeviceIsLostUseCase: NotifyDeviceIsLostUseCase,
  private val getMyIdUseCase: GetMyIdUseCase,
  private val scope: CoroutineScope
) {

  private var isDiscovering = false

  private val btAdapter: BluetoothAdapter =
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

  private val advertiseSettings = AdvertiseSettings.Builder()
    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
    .setConnectable(false)
    .build()

  private val pUuid = ParcelUuid(UUID.fromString("000043ef-0000-1000-8000-00805F9B34FB"))

  private var advertiseData: AdvertiseData? = null

  private val scanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
      super.onScanResult(callbackType, result)
      val data = result?.scanRecord?.getServiceData(pUuid)

      if (data == null) {
        Timber.e("BLE received incorrect data")
        return
      }

      val id = data.toLong()

      scope.launch {
        notifyUserIsNearbyUseCase(id)
      }
    }

    override fun onScanFailed(errorCode: Int) {
      super.onScanFailed(errorCode)
      Timber.e("BLE scan failed. Error code: $errorCode")
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
      super.onBatchScanResults(results)
    }
  }

  private val advertisingCallback = object : AdvertiseCallback() {
    override fun onStartFailure(errorCode: Int) {
      super.onStartFailure(errorCode)
      Timber.e("BLE advertising failed. Error code: $errorCode")
    }

    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
      super.onStartSuccess(settingsInEffect)
      Timber.d("BLE advertising is successful")
    }
  }

  private var bleAdvertiser: BluetoothLeAdvertiser? = null
  private var bleScanner: BluetoothLeScanner? = null

  init {
    turnBluetoothOn()

    scope.launch {
      val id = getMyIdUseCase()
      Timber.e("My id is $id")
      val idBytes = id.toByteArray()
      val data = AdvertiseData.Builder()
        .addServiceUuid(pUuid)
        .addServiceData(pUuid, idBytes)
        .build()

      advertiseData = data
      startDiscovery()
    }
  }

  private fun turnBluetoothOn() {
    if (bluetoothPermissionsNotGranted(Manifest.permission.BLUETOOTH_CONNECT)) return

    btAdapter.enable()
    bleAdvertiser = btAdapter.bluetoothLeAdvertiser
    bleScanner = btAdapter.bluetoothLeScanner
  }

  private fun advertise() {
    val data = advertiseData ?: return

    bleAdvertiser?.startAdvertising(advertiseSettings, data, advertisingCallback)
  }

  private fun scan() {
    val filter = ScanFilter.Builder()
      .setServiceUuid(pUuid)
      .build()

    val scanSettings = ScanSettings.Builder()
      .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
      .build()

    bleScanner?.startScan(listOf(filter), scanSettings, scanCallback)
  }

  private fun stopAdvertise() {
    bleAdvertiser?.stopAdvertising(advertisingCallback)
  }


  private fun stopScan() {
    bleScanner?.stopScan(scanCallback)
  }

  private fun bluetoothPermissionsNotGranted(vararg permissions: String): Boolean {
    for (permission in permissions) {
      if (ActivityCompat.checkSelfPermission(context, permission)
        != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Timber.e("Permission $permission is not granted!")
        return true
      }
    }
    return false
  }

  fun startDiscovery() {
    if (bluetoothPermissionsNotGranted(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_ADVERTISE)) return
    if (isDiscovering) return

    scan()
    advertise()

    isDiscovering = true
  }

  fun stopDiscovery() {
    if (!isDiscovering) return

    if (bluetoothPermissionsNotGranted(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_ADVERTISE)) return
    stopScan()
    stopAdvertise()

    isDiscovering = false
  }

}