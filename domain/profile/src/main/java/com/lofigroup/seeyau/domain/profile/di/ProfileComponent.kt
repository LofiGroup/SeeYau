package com.lofigroup.seeyau.domain.profile.di

import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.lofigroup.seeyau.domain.profile.usecases.*
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
  fun updateProfileUseCase(): UpdateProfileUseCase
  fun pullProfileDataUseCase(): PullProfileDataUseCase
  fun getMyIdUseCase(): GetMyIdUseCase
  fun getUserUseCase(): GetUserUseCase
  fun pullUserDataUseCase(): PullUserDataUseCase

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun profileRepository(profileRepository: ProfileRepository): Builder

    fun build(): ProfileComponent
  }

}