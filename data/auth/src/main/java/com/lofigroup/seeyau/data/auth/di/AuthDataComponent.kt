package com.lofigroup.seeyau.data.auth.di

import android.content.SharedPreferences
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
  modules = [ApiModule::class, RepositoryModule::class, IOModule::class]
)
interface AuthDataComponent {

  fun getRepository(): AuthRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    fun build(): AuthDataComponent
  }

}