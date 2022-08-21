package com.lofigroup.seeyau.data.profile.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.seeyau.data.profile.model.UpdateProfileRequest
import com.lofigroup.seeyau.data.profile.ProfileApi
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create

@Module
object ApiModule {

  @AppScope
  @Provides
  fun provideApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

}