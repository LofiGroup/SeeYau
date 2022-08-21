package com.lofigroup.seeyau.data.auth.di

import com.lofigroup.seeyau.data.auth.AuthApi
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

}