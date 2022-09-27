package com.lofigroup.seeyau.data.auth.api

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.core.util.ResourceState
import com.lofigroup.seeyau.data.auth.di.DaggerAuthDataComponent
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.di.DaggerAuthComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit

class AuthModuleImpl(
  baseRetrofit: Retrofit,
  private val tokenStore: TokenStore
): AuthModule {

  override fun observeState(): Flow<ResourceState> = tokenStore.getTokenState()

  private val dataComponent = DaggerAuthDataComponent.builder()
    .baseRetrofit(baseRetrofit)
    .tokenStore(tokenStore)
    .build()

  private val domainComponent = DaggerAuthComponent.builder()
    .authRepository(dataComponent.getRepository())
    .build()

  override fun domainComponent(): AuthComponent {
    return domainComponent
  }

}