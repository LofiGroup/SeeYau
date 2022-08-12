package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.remote.BasicApi
import com.lofigroup.data.navigator.remote.interceptors.AddAccessTokenInterceptor
import com.lofigroup.seeyau.common.network.SeeYauApi
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
  fun provideSeeYauApi(client: OkHttpClient): BasicApi {
    return Retrofit.Builder()
      .baseUrl(SeeYauApi.baseUrl)
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(BasicApi::class.java)
  }

  @AppScope
  @Provides
  fun provideHttpClient(
    addAccessTokenInterceptor: AddAccessTokenInterceptor
  ): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

    return OkHttpClient.Builder()
      .addInterceptor(addAccessTokenInterceptor)
      .addInterceptor(loggingInterceptor)
      .build()
  }

}