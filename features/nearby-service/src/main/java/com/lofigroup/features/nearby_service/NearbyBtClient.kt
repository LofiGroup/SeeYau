package com.lofigroup.features.nearby_service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import com.lofigroup.domain.navigator.usecases.NotifyDeviceIsLostUseCase
import com.lofigroup.domain.navigator.usecases.NotifyUserIsNearbyUseCase
import com.lofigroup.features.nearby_service.search_mode.BtSettingsDataSource
import com.lofigroup.seeyau.domain.profile.usecases.GetMyIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
  private val scope: CoroutineScope,
  private val btSettingsDataSource: BtSettingsDataSource
) {

  private var isDiscovering = false

  private val btAdapter: BluetoothAdapter =
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

  private val pUuid = ParcelUuid(UUID.fromString("000043ef-0000-1000-8000-00805F9B34FB"))

  private var advertiseData: AdvertiseData? = null

  private val scanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
      super.onScanResult(callbackType, result)
      Timber.e("Scan result")

      if (result != null)
        notifyUserIsFound(result)
    }

    override fun onScanFailed(errorCode: Int) {
      super.onScanFailed(errorCode)
      Timber.e("BLE scan failed. Error code: $errorCode")
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
      super.onBatchScanResults(results)
      if (results == null) return

      for (result in results)
        notifyUserIsFound(result)
    }

    private fun notifyUserIsFound(scanResult: ScanResult) {
      val data = scanResult.scanRecord?.getServiceData(pUuid)

      if (data == null) {
        Timber.e("BLE received incorrect data")
        return
      }

      val id = data.toLong()
      Timber.e("Found user with id: $id")

      scope.launch {
        notifyUserIsNearbyUseCase(id)
      }
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
    scope.launch {
      val id = getMyIdUseCase()
      Timber.e("My id is $id")
      val idBytes = id.toByteArray()
      val data = AdvertiseData.Builder()
        .addServiceUuid(pUuid)
        .addServiceData(pUuid, idBytes)
        .build()

      advertiseData = data

      btSettingsDataSource.getState().collect {
        scan(it.scanSetting)
        advertise(it.advertisingSetting)
      }
    }
  }

  private fun advertise(settings: AdvertiseSettings) {
    val data = advertiseData ?: return

    stopAdvertise()
    bleAdvertiser?.startAdvertising(settings, data, advertisingCallback)
  }

  private fun scan(settings: ScanSettings) {
    stopScan()

    val filter = ScanFilter.Builder()
      .setServiceUuid(pUuid)
      .build()

    bleScanner?.startScan(listOf(filter), settings, scanCallback)
  }

  private fun stopAdvertise() {
    bleAdvertiser?.stopAdvertising(advertisingCallback)
  }

  private fun stopScan() {
    bleScanner?.stopScan(scanCallback)
  }

  fun startDiscovery() {
    if (isDiscovering) return

    bleAdvertiser = btAdapter.bluetoothLeAdvertiser
    bleScanner = btAdapter.bluetoothLeScanner

    val settings = btSettingsDataSource.getState().value
    scan(settings.scanSetting)
    advertise(settings.advertisingSetting)

    isDiscovering = true
  }

  fun stopDiscovery() {
    if (!isDiscovering) return

    Timber.e("Stopping discovery")

    stopScan()
    stopAdvertise()

    bleAdvertiser = null
    bleScanner = null

    isDiscovering = false
  }

  fun bluetoothIsOn(): Boolean {
    return btAdapter.isEnabled
  }

  fun destroy() {
    stopDiscovery()
  }

}