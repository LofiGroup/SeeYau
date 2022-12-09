package com.lofigroup.backend_api.api

import android.content.SharedPreferences
import com.lofigroup.backend_api.di.DaggerBaseDataComponent
import com.lofigroup.seeyau.domain.base.di.DaggerBaseModuleComponent
import com.lofigroup.core.util.ResourceStateHolder

class BaseDataModule(
  sharedPref: SharedPreferences,
) {

  private val moduleStateHolder = ResourceStateHolder()

  val component = DaggerBaseDataComponent.builder()
    .sharedPref(sharedPref)
    .moduleStateHolder(moduleStateHolder)
    .build()

  val domainComponent = DaggerBaseModuleComponent.builder()
    .moduleStateHolder(moduleStateHolder)
    .build()

}