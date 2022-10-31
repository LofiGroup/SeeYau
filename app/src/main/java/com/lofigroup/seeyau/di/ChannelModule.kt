package com.lofigroup.seeyau.di

import com.lofigroup.seayau.common.ui.permissions.PermissionRequestChannel
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object ChannelModule {

  @AppScope
  @Provides
  fun providePermissionRequestChannel(): PermissionRequestChannel {
    return PermissionRequestChannel()
  }

}