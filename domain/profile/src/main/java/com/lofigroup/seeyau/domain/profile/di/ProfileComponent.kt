package com.lofigroup.seeyau.domain.profile.di

import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.usecases.GetProfileUseCase
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
  modules = [],
  dependencies = []
)
interface ProfileComponent {

  fun getProfileUseCase(): GetProfileUseCase

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun profileRepository(profileRepository: ProfileRepository): Builder

    fun build(): ProfileComponent
  }

}