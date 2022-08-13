package com.lofigroup.data.navigator.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.FeatureScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@FeatureScope
@Component(
  modules = [ApiModule::class, RepositoryModule::class, UserDataModule::class, IOModule::class],
  dependencies = [AuthComponent::class]
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

    @BindsInstance
    fun appScope(appScope: CoroutineScope): Builder

    fun authComponent(authComponent: AuthComponent): Builder

    fun build(): NavigatorDataComponent
  }

}