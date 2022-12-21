package com.lofigroup.seeyau.domain.base.di

import com.lofigroup.seeyau.domain.base.user_notification_channel.UserNotificationChannel
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object UserNotificationModule {
  @AppScope
  @Provides
  fun provideUserNotificationChannel(): UserNotificationChannel {
    return UserNotificationChannel()
  }
}