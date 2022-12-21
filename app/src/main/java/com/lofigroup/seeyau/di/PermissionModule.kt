package com.lofigroup.seeyau.di

import com.lofigroup.core.permission.PermissionRequestChannel
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object PermissionModule {

  @AppScope
  @Provides
  fun providePermissionRequestChannel(): com.lofigroup.core.permission.PermissionRequestChannel {
    return com.lofigroup.core.permission.PermissionRequestChannel()
  }

}