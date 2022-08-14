package com.lofigroup.seeyau.data.profile.di

import com.lofigroup.seeyau.data.profile.ProfileDataSource
import com.lofigroup.seeyau.data.profile.ProfileDataSourceImpl
import com.lofigroup.seeyau.data.profile.ProfileRepositoryImpl
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

  @AppScope
  @Binds
  fun bindRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

  @AppScope
  @Binds
  fun bindProfileDataSource(profileDataSourceImpl: ProfileDataSourceImpl): ProfileDataSource

}