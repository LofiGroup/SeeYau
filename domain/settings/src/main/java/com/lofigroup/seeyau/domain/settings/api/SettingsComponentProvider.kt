package com.lofigroup.seeyau.domain.settings.api

import com.lofigroup.seeyau.domain.settings.di.SettingsComponent

interface SettingsComponentProvider {

  fun provideSettingsModule(): SettingsComponent

}