package com.lofigroup.seeyau.data.profile.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.seeyau.data.profile.ProfileApi
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Response

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideApi(baseApi: SeeYauApi): ProfileApi = object : ProfileApi {
    override suspend fun getProfile(): Response<UserDto> {
      return baseApi.getMe()
    }
  }

}