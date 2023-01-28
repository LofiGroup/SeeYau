package com.lofigroup.features.nearby_service.search_mode.di

import com.lofigroup.features.nearby_service.search_mode.SearchModeDataSource
import com.lofigroup.features.nearby_service.search_mode.SearchModeDataSourceImpl
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.FeatureScope
import dagger.Binds
import dagger.Module

@Module
interface SearchModeModule {

  @FeatureScope
  @Binds
  fun bindSearchModeDataSource(searchModeDataSourceImpl: SearchModeDataSourceImpl): SearchModeDataSource


}