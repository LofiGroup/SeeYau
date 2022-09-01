package com.lofigroup.seeyau.data.auth.api

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.seeyau.data.auth.di.DaggerAuthDataComponent
import com.lofigroup.seeyau.domain.auth.di.DaggerAuthComponent
import retrofit2.Retrofit

class AuthModule(
  baseRetrofit: Retrofit,
  tokenStore: TokenStore
) {

  private val dataComponent = DaggerAuthDataComponent.builder()
    .baseRetrofit(baseRetrofit)
    .tokenStore(tokenStore)
    .build()

  val domainComponent = DaggerAuthComponent.builder()
    .authRepository(dataComponent.getRepository())
    .build()

}