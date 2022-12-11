package com.lofigroup.backend_api.di

import android.content.SharedPreferences
import com.lofigroup.backend_api.TokenStore
import com.lofigroup.backend_api.websocket.WebSocketChannel
import com.lofigroup.core.util.ResourceStateHolder
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier

@AppScope
@Component(modules = [ApiModule::class, TokenStoreModule::class])
interface BaseDataComponent {

  fun getRetrofit(): Retrofit
  fun getHttpClient(): OkHttpClient

  fun getWebSocketChannel(): WebSocketChannel
  fun tokenStore(): TokenStore
  @DataSyncStateHolder fun syncStateHolder(): ResourceStateHolder

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    @BindsInstance
    fun moduleStateHolder(@DataSyncStateHolder moduleStateHolder: ResourceStateHolder): Builder

    fun build(): BaseDataComponent
  }

}

@Qualifier
annotation class DataSyncStateHolder