package com.lofigroup.seeyau.di

import com.lofigroup.seeyau.features.data_sync_service.DataSyncer
import com.lofigroup.seeyau.ui.MainActivity
import com.lofigroup.seeyau.ui.MainViewModel
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component
interface MainActivityComponent {
  fun inject(mainActivity: MainActivity)

  fun getViewModel(): MainViewModel

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun dataSyncer(dataSyncer: DataSyncer): Builder

    fun build(): MainActivityComponent
  }
}