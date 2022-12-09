package com.lofigroup.backend_api.di

import com.lofigroup.backend_api.TokenStore
import com.lofigroup.backend_api.TokenStoreImpl
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface TokenStoreModule {
  @AppScope
  @Binds
  fun bindTokenStore(tokenStoreImpl: TokenStoreImpl): TokenStore
}