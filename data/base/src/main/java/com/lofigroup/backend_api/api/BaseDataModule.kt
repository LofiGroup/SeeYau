package com.lofigroup.backend_api.api

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.backend_api.di.DaggerBaseDataComponent
import com.lofigroup.seeyau.domain.base.di.DaggerBaseModuleComponent
import com.lofigroup.core.util.ResourceStateHolder
import kotlinx.coroutines.CoroutineScope

class BaseDataModule(
  sharedPref: SharedPreferences,
  context: Context,
  appScope: CoroutineScope
) {

  private val moduleStateHolder = ResourceStateHolder()

  val component = DaggerBaseDataComponent.builder()
    .sharedPref(sharedPref)
    .moduleStateHolder(moduleStateHolder)
    .context(context)
    .appCoroutineScope(appScope)
    .build()

  val domainComponent = DaggerBaseModuleComponent.builder()
    .moduleStateHolder(moduleStateHolder)
    .build()

}