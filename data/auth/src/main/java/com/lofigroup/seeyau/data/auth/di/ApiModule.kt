package com.lofigroup.seeyau.data.auth.di

import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.lofigroup.seeyau.data.auth.AuthApi
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
  fun provideSeeYauApi(client: OkHttpClient): AuthApi {
    return Retrofit.Builder()
      .baseUrl(SeeYauApiConstants.baseUrl)
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(AuthApi::class.java)
  }

  @AppScope
  @Provides
  fun provideHttpClient(
  ): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

    return OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build()
  }

}