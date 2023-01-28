package com.lofigroup.features.nearby_service.di

import android.content.Context
import com.lofigroup.core.bluetooth.BluetoothRequesterChannel
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.features.nearby_service.search_mode.di.SearchModeModule
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.sillyapps.core.di.FeatureScope
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@FeatureScope
@Component(
  dependencies = [NavigatorComponent::class, ProfileComponent::class, SettingsComponent::class],
  modules = [SearchModeModule::class]
)
interface NearbyServiceComponent {

  fun inject(nearbyServiceImpl: NearbyServiceImpl)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun coroutineScope(scope: CoroutineScope): Builder

    @BindsInstance
    fun notificationRequester(notificationRequester: NotificationRequester): Builder

    @BindsInstance
    fun mainScreenEventChannel(mainScreenEventChannel: MainScreenEventChannel): Builder


    fun settingsComponent(component: SettingsComponent): Builder

    fun navigatorComponent(component: NavigatorComponent): Builder

    fun profileComponent(component: ProfileComponent): Builder


    fun build(): NearbyServiceComponent
  }

}