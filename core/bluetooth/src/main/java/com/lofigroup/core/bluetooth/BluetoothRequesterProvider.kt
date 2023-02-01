package com.lofigroup.core.bluetooth

interface BluetoothRequesterProvider {

  fun provideBluetoothRequester(): BluetoothRequesterChannel

}