package com.lofigroup.seeyau.data.settings.api

import android.content.SharedPreferences
import com.lofigroup.seeyau.data.settings.di.DaggerSettingsDataComponent
import com.lofigroup.seeyau.domain.settings.di.DaggerSettingsComponent

class SettingsModule(
  sharedPreferences: SharedPreferences
) {

  private val dataComponent = DaggerSettingsDataComponent.builder()
    .getSharedPreferences(sharedPreferences)
    .build()

  val domainComponent = DaggerSettingsComponent.builder()
    .repository(dataComponent.getRepository())
    .build()

}