package com.lofigroup.features.nearby_service.di

import android.content.Context
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seayau.common.ui.permissions.PermissionRequestChannel
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.sillyapps.core.di.FeatureScope
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@FeatureScope
@Component(dependencies = [NavigatorComponent::class, ProfileComponent::class, SettingsComponent::class])
interface NearbyServiceComponent {

  fun inject(nearbyServiceImpl: NearbyServiceImpl)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun coroutineScope(scope: CoroutineScope): Builder

    fun settingsComponent(component: SettingsComponent): Builder

    fun navigatorComponent(component: NavigatorComponent): Builder

    fun profileComponent(component: ProfileComponent): Builder

    fun build(): NearbyServiceComponent
  }

}