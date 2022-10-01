package com.lofigroup.seeyau.domain.settings.di

import com.lofigroup.seeyau.domain.settings.SettingsRepository
import com.lofigroup.seeyau.domain.settings.usecases.GetVisibilityUseCase
import com.lofigroup.seeyau.domain.settings.usecases.SetVisibilityUseCase
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface SettingsComponent {

  fun getVisibilityUseCase(): GetVisibilityUseCase
  fun setVisibilityUseCase(): SetVisibilityUseCase

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun repository(settingsRepository: SettingsRepository): Builder

    fun build(): SettingsComponent

  }

}