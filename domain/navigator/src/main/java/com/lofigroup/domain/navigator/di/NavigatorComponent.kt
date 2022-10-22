package com.lofigroup.domain.navigator.di

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.usecases.*
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component
interface NavigatorComponent {

  fun getRepository(): NavigatorRepository

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun repository(navigatorRepository: NavigatorRepository): Builder

    @BindsInstance
    fun profileRepository(profileRepository: ProfileRepository): Builder

    fun build(): NavigatorComponent

  }

}