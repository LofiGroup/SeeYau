package com.lofigroup.seeyau.domain.base.di

import com.lofigroup.core.util.ResourceStateHolder
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface BaseModuleComponent {

  fun moduleStateHolder(): ResourceStateHolder

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun moduleStateHolder(stateHolder: ResourceStateHolder): Builder
    fun build(): BaseModuleComponent
  }

}