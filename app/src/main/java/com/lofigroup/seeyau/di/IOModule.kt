package com.lofigroup.seeyau.di

import android.content.ContentResolver
import android.content.Context
import com.sillyapps.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object IOModule {

  @AppScope
  @Provides
  fun provideContentResolver(context: Context): ContentResolver {
    return context.contentResolver
  }

}