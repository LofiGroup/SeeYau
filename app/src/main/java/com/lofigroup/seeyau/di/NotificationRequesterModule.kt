package com.lofigroup.seeyau.di

import android.content.Context
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.notifications.NotificationRequesterImpl
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
object NotificationRequesterModule {

  @AppScope
  @Provides
  fun provide(context: Context): NotificationRequester {
    return NotificationRequesterImpl(context)
  }
}