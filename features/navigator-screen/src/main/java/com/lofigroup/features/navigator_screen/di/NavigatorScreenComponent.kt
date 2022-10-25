package com.lofigroup.features.navigator_screen.di

import android.content.res.Resources
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.ui.NavigatorScreenViewModel
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.sillyapps.core.di.ScreenScope
import dagger.BindsInstance
import dagger.Component

@ScreenScope
@Component(
  modules = [],
  dependencies = [NavigatorComponent::class, ChatComponent::class, ProfileComponent::class, SettingsComponent::class]
)
interface NavigatorScreenComponent {

  fun getViewModel(): NavigatorScreenViewModel

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun resources(resources: Resources): Builder

    fun profileComponent(component: ProfileComponent): Builder

    fun chatComponent(component: ChatComponent): Builder

    fun navigatorComponent(component: NavigatorComponent): Builder

    fun settingsComponent(component: SettingsComponent): Builder

    fun build(): NavigatorScreenComponent

  }

}