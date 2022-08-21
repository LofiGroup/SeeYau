package com.lofigroup.backend_api.di

import android.content.SharedPreferences
import com.lofigroup.backend_api.TokenStore
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@AppScope
@Component(modules = [ApiModule::class, TokenStoreModule::class])
interface BackendApiComponent {

  fun getRetrofit(): Retrofit

  fun tokenStore(): TokenStore

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    fun build(): BackendApiComponent
  }

}