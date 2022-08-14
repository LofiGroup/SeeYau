package com.lofigroup.data.navigator.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.data.navigator.remote.NavigatorApi
import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Response

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideNavigatorApi(baseApi: SeeYauApi): NavigatorApi {
    return object : NavigatorApi {
      override suspend fun getMe(): Response<UserDto> {
        return baseApi.getMe()
      }

      override suspend fun getUser(id: Long): Response<UserDto> {
        return baseApi.getUser(id)
      }

    }
  }

}