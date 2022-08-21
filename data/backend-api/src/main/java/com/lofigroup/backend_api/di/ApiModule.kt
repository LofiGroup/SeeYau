package com.lofigroup.backend_api.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.backend_api.TokenStore
import com.lofigroup.backend_api.interceptors.AddAccessTokenInterceptor
import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideSeeYauApi(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl(SeeYauApiConstants.baseUrl)
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  @AppScope
  @Provides
  fun provideHttpClient(addAccessTokenInterceptor: AddAccessTokenInterceptor): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

    return OkHttpClient.Builder()
      .addInterceptor(addAccessTokenInterceptor)
      .addInterceptor(loggingInterceptor)
      .build()
  }

}