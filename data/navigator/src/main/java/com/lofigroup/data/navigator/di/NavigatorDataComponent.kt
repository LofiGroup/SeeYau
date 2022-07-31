package com.lofigroup.data.navigator.di

import android.content.Context
import com.lofigroup.domain.navigator.NavigatorRepository
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [RepositoryModule::class])
interface NavigatorDataComponent {

  fun getRepository(): NavigatorRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    fun build(): NavigatorDataComponent
  }

}