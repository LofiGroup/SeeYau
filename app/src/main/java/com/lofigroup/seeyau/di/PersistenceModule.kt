package com.lofigroup.seeyau.di

import android.content.Context
import com.lofigroup.seeyau.data.AppDatabase
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object PersistenceModule {

  private const val SHARED_PREF_TAG = "SeaYauSharedPreferences"

  @AppScope
  @Provides
  fun provideDataBase(context: Context) = AppDatabase.getInstance(context)

  @AppScope
  @Provides
  fun provideSharedPref(context: Context) = context.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE)

}