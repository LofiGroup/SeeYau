package com.lofigroup.seeyau.data.auth.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.backend_api.models.AccessRequest
import com.lofigroup.backend_api.models.AccessResponse
import com.lofigroup.seeyau.common.network.SeeYauApiConstants
import com.lofigroup.seeyau.data.auth.AuthApi
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideAuthApi(baseApi: SeeYauApi): AuthApi = object : AuthApi {
    override suspend fun login(body: AccessRequest): Response<AccessResponse> {
      return baseApi.login(body)
    }

    override suspend fun register(body: AccessRequest): Response<AccessResponse> {
      return baseApi.register(body)
    }

  }
}