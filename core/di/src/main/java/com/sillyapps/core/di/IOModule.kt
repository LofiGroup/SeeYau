package com.sillyapps.core.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
annotation class IODispatcher

@Qualifier
annotation class IOCoroutineScope

@Module
class IOModule {
  @Provides
  fun provideIODispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
  }
}