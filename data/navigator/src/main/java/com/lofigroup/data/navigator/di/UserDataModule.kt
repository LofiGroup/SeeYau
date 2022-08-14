package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.local.ProfileDataSource
import com.lofigroup.data.navigator.local.ProfileDataSourceImpl
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface UserDataModule {

  @AppScope
  @Binds
  fun bindUserData(profileDataSourceImpl: ProfileDataSourceImpl): ProfileDataSource

}