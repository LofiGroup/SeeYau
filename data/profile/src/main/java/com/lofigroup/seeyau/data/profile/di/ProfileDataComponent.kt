package com.lofigroup.seeyau.data.profile.di

import android.content.ContentResolver
import android.content.SharedPreferences
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.ProfileDataSource
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

@AppScope
@Component(modules = [ApiModule::class, RepositoryModule::class, IOModule::class])
interface ProfileDataComponent {

  fun getRepository(): ProfileRepository
  fun getProfileDataSource(): ProfileDataSource

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun userDao(userDao: UserDao): Builder

    @BindsInstance
    fun sharedPref(sharedPreferences: SharedPreferences): Builder

    @BindsInstance
    fun appScope(appScope: CoroutineScope): Builder

    @BindsInstance
    fun contentResolver(contentResolver: ContentResolver): Builder

    fun build(): ProfileDataComponent
  }

}