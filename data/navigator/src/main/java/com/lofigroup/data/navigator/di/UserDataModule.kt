package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.local.ProfileDataSource
import com.lofigroup.data.navigator.local.ProfileDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
interface UserDataModule {

  @Binds
  fun bindUserData(profileDataSourceImpl: ProfileDataSourceImpl): ProfileDataSource

}