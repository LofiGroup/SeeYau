package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.NavigatorRepositoryImpl
import com.lofigroup.domain.navigator.NavigatorRepository
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

  @AppScope
  @Binds
  fun bindRepository(repositoryImpl: NavigatorRepositoryImpl): NavigatorRepository

}