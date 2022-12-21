package com.lofigroup.seeyau.domain.base.di

import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.seeyau.domain.base.user_notification_channel.UserNotificationChannel
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
  modules = [UserNotificationModule::class]
)
interface BaseModuleComponent {

  fun moduleStateHolder(): ResourceStateHolder
  fun getUserNotificationChannel(): UserNotificationChannel

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun moduleStateHolder(stateHolder: ResourceStateHolder): Builder
    fun build(): BaseModuleComponent
  }

}