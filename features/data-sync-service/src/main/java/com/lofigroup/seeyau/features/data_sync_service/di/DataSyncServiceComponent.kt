package com.lofigroup.seeyau.features.data_sync_service.di

import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.domain.base.di.BaseModuleComponent
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl
import com.sillyapps.core.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(dependencies = [ChatComponent::class, NavigatorComponent::class, ProfileComponent::class, BaseModuleComponent::class])
interface DataSyncServiceComponent {

  fun inject(service: DataSyncServiceImpl)

  @Component.Builder
  interface Builder {
    fun chatComponent(chatComponent: ChatComponent): Builder
    fun navigatorComponent(navigatorComponent: NavigatorComponent): Builder
    fun profileComponent(profileComponent: ProfileComponent): Builder
    fun baseComponent(component: BaseModuleComponent): Builder

    fun build(): DataSyncServiceComponent
  }

}