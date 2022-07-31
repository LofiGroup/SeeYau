package com.lofigroup.data.navigator.di

import com.lofigroup.data.navigator.NavigatorRepositoryImpl
import com.lofigroup.domain.navigator.NavigatorRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

  @Binds
  fun bindRepository(repositoryImpl: NavigatorRepositoryImpl): NavigatorRepository

}