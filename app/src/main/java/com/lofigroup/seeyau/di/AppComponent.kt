package com.lofigroup.seeyau.di

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.seeyau.data.AppDatabase
import com.lofigroup.seeyau.ui.MainViewModel
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [PersistenceModule::class])
interface AppComponent {

  fun getSharedPref(): SharedPreferences

  fun getDatabase(): AppDatabase

  fun getViewModel(): MainViewModel

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun context(context: Context): Builder

    fun build(): AppComponent

  }

}