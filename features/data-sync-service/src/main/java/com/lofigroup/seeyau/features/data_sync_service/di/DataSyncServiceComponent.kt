package com.lofigroup.seeyau.features.data_sync_service.di

import android.content.Context
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.common.chat.components.notifications.ChatNotificationBuilder
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.base.di.BaseModuleComponent
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.data_sync_service.DataSyncer
import com.sillyapps.core.di.FeatureScope
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(dependencies = [ChatComponent::class, NavigatorComponent::class, ProfileComponent::class, BaseModuleComponent::class, AuthComponent::class])
interface DataSyncServiceComponent {

  fun getDataSyncer(): DataSyncer

  @Component.Builder
  interface Builder {
    fun chatComponent(chatComponent: ChatComponent): Builder
    fun navigatorComponent(navigatorComponent: NavigatorComponent): Builder
    fun profileComponent(profileComponent: ProfileComponent): Builder
    fun baseComponent(component: BaseModuleComponent): Builder
    fun authComponent(component: AuthComponent): Builder

    @BindsInstance
    fun authModule(authModule: AuthModule): Builder

    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun chatNotificationBuilder(notificationRequester: ChatNotificationBuilder): Builder

    @BindsInstance
    fun mainScreenEventChannel(channel: MainScreenEventChannel): Builder

    fun build(): DataSyncServiceComponent
  }

}

fun buildDataSyncer(
  chatComponent: ChatComponent,
  navigatorComponent: NavigatorComponent,
  profileComponent: ProfileComponent,
  baseComponent: BaseModuleComponent,
  authModule: AuthModule,
  chatNotificationBuilder: ChatNotificationBuilder,
  mainScreenEventChannel: MainScreenEventChannel,
  context: Context
): DataSyncer {
  val dataSyncerComponent = DaggerDataSyncServiceComponent.builder()
    .baseComponent(baseComponent)
    .chatComponent(chatComponent)
    .navigatorComponent(navigatorComponent)
    .baseComponent(baseComponent)
    .profileComponent(profileComponent)
    .authModule(authModule)
    .authComponent(authModule.domainComponent())
    .chatNotificationBuilder(chatNotificationBuilder)
    .mainScreenEventChannel(mainScreenEventChannel)
    .context(context)
    .build()

  return dataSyncerComponent.getDataSyncer()
}