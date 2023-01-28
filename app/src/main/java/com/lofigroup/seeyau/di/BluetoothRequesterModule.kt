package com.lofigroup.seeyau.di

import com.lofigroup.core.bluetooth.BluetoothRequesterChannel
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object BluetoothRequesterModule {

  @AppScope
  @Provides
  fun provideBluetoothRequester(): BluetoothRequesterChannel {
    return BluetoothRequesterChannel()
  }

}