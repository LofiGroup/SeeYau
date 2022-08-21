package com.lofigroup.data.navigator.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.domain.navigator.NavigatorRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

@AppScope
@Component(
  modules = [ApiModule::class, RepositoryModule::class, IOModule::class]
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

    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    fun build(): NavigatorDataComponent
  }

}