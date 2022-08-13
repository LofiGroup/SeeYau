package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.remote.SeeYauApi
import com.lofigroup.data.navigator.remote.interceptors.AddAccessTokenInterceptor
import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.FeatureScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object ApiModule {

  @FeatureScope
  @Provides
  fun provideSeeYauApi(client: OkHttpClient): SeeYauApi {
    return Retrofit.Builder()
      .baseUrl(SeeYauApiConstants.baseUrl)
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(SeeYauApi::class.java)
  }

  @FeatureScope
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