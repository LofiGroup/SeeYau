package com.lofigroup.features.nearby_service.search_mode.di

import com.lofigroup.features.nearby_service.search_mode.BtSettingsDataSource
import com.lofigroup.features.nearby_service.search_mode.BtSettingsDataSourceImpl
import com.sillyapps.core.di.FeatureScope
import dagger.Binds
import dagger.Module

@Module
interface SearchModeModule {

  @FeatureScope
  @Binds
  fun bindSearchModeDataSource(btSettingsDataSourceImpl: BtSettingsDataSourceImpl): BtSettingsDataSource


}