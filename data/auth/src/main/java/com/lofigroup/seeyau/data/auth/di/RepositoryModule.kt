package com.lofigroup.seeyau.data.auth.di

import com.lofigroup.seeyau.data.auth.AuthRepositoryImpl
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

  @AppScope
  @Binds
  fun bindRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

}