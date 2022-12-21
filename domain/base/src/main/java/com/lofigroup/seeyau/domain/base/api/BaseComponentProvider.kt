package com.lofigroup.seeyau.domain.base.api

import com.lofigroup.seeyau.domain.base.di.BaseModuleComponent

interface BaseComponentProvider {
  fun provideBaseComponent(): BaseModuleComponent
}