package com.lofigroup.seeyau.data.auth.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.backend_api.TokenStore
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
    fun baseApi(api: SeeYauApi): Builder

    @BindsInstance
    fun tokenStore(tokenStore: TokenStore): Builder

    fun build(): AuthDataComponent
  }

}