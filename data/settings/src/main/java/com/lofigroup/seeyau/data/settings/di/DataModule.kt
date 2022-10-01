package com.lofigroup.seeyau.data.settings.di

import com.lofigroup.seeyau.data.settings.SettingsRepositoryImpl
import com.lofigroup.seeyau.domain.settings.SettingsRepository
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

  @AppScope
  @Binds
  fun bindSettingRepository(settingsRepository: SettingsRepositoryImpl): SettingsRepository

}