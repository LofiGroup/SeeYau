package com.lofigroup.data.navigator.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.backend_api.models.UserDto
import com.lofigroup.data.navigator.remote.NavigatorApi
import com.lofigroup.domain.navigator.model.User
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Response
import retrofit2.Retrofit

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideNavigatorApi(retrofit: Retrofit): NavigatorApi = retrofit.create(NavigatorApi::class.java)

}