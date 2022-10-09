package com.lofigroup.seeyau.data.auth.api

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.core.util.ResourceState
import com.lofigroup.core.util.ResourceStateHolder
import com.lofigroup.seeyau.data.auth.di.DaggerAuthDataComponent
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.di.DaggerAuthComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import retrofit2.Retrofit

class AuthModuleImpl(
  baseRetrofit: Retrofit,
  private val tokenStore: TokenStore
) : AuthModule {

  private val moduleStateHolder = ResourceStateHolder()
  override fun observeState(): Flow<ResourceState> {
    return combine(
      tokenStore.getTokenState(),
      moduleStateHolder.observe()
    ) { tokenStoreState, moduleState ->
      if (tokenStoreState == ResourceState.IS_READY && moduleState == ResourceState.IS_READY)
        ResourceState.IS_READY
      else
        ResourceState.LOADING
    }
  }

  private val dataComponent = DaggerAuthDataComponent.builder()
    .baseRetrofit(baseRetrofit)
    .tokenStore(tokenStore)
    .moduleStateHolder(moduleStateHolder)
    .build()

  private val domainComponent = DaggerAuthComponent.builder()
    .authRepository(dataComponent.getRepository())
    .build()

  override fun domainComponent(): AuthComponent {
    return domainComponent
  }

}