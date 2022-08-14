package com.lofigroup.seeyau.features.profile_screen.di

import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.profile_screen.ProfileScreenViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.Component

@ScreenScope
@Component(
  modules = [],
  dependencies = [ProfileComponent::class]
)
interface ProfileScreenComponent {

  fun getViewModel(): ProfileScreenViewModel

  @Component.Builder
  interface Builder {

    fun profileComponent(profileComponent: ProfileComponent): Builder

    fun build(): ProfileScreenComponent

  }

}