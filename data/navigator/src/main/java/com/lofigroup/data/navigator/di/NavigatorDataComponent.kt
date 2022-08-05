package com.lofigroup.data.navigator.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.domain.navigator.NavigatorRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
  modules = [RepositoryModule::class, UserDataModule::class, IOModule::class],
)
interface NavigatorDataComponent {

  fun getRepository(): NavigatorRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun userDao(userDao: UserDao): Builder

    @BindsInstance
    fun sharedPref(sharedPref: SharedPreferences): Builder

    fun build(): NavigatorDataComponent
  }

}