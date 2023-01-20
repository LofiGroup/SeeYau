package com.lofigroup.seeyau.main_screen_event_channel

import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface MainScreenEventChannelModule {
  @AppScope
  @Binds
  fun bind(mainScreenEventChannelImpl: MainScreenEventChannelImpl): MainScreenEventChannel
}