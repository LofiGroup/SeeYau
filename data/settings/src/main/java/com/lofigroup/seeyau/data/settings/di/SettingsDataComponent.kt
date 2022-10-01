package com.lofigroup.seeyau.data.settings.di

import android.content.SharedPreferences
import com.lofigroup.seeyau.domain.settings.SettingsRepository
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [DataModule::class])
interface SettingsDataComponent {

  fun getRepository(): SettingsRepository

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun getSharedPreferences(sharedPreferences: SharedPreferences): Builder

    fun build(): SettingsDataComponent
  }

}