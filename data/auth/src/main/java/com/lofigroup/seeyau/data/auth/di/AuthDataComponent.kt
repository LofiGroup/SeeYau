package com.lofigroup.seeyau.data.auth.di

import android.content.Context
import com.lofigroup.backend_api.TokenStore
import com.lofigroup.backend_api.data.DatabaseHandler
import com.lofigroup.backend_api.di.DataSyncStateHolder
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Qualifier

@AppScope
@Component(
  modules = [ApiModule::class, RepositoryModule::class, IOModule::class]
)
interface AuthDataComponent {

  fun getRepository(): AuthRepository

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun moduleStateHolder(@AuthDataStateHolder stateHolder: ResourceStateHolder): Builder

    @BindsInstance
    fun dataSyncStateHolder(@DataSyncStateHolder stateHolder: ResourceStateHolder): Builder

    @BindsInstance
    fun databaseHandler(databaseHandler: DatabaseHandler): Builder

    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun tokenStore(tokenStore: TokenStore): Builder

    @BindsInstance
    fun userDao(userDao: UserDao): Builder

    @BindsInstance
    fun context(context: Context): Builder

    fun build(): AuthDataComponent
  }

}

@Qualifier
annotation class AuthDataStateHolder