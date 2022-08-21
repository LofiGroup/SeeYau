package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.remote.NavigatorApi
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideNavigatorApi(retrofit: Retrofit): NavigatorApi = retrofit.create(NavigatorApi::class.java)

}