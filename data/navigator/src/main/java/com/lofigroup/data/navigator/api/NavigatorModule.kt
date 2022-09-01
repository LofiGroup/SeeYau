package com.lofigroup.data.navigator.api

import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.data.navigator.di.DaggerNavigatorDataComponent
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.domain.navigator.di.DaggerNavigatorComponent
import com.sillyapps.core.di.AppScope
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

class NavigatorModule(
  context: Context,
  userDao: UserDao,
  sharedPreferences: SharedPreferences,
  appScope: CoroutineScope,
  baseRetrofit: Retrofit
) {

  private val dataComponent = DaggerNavigatorDataComponent.builder()
    .appScope(appScope)
    .baseRetrofit(baseRetrofit)
    .sharedPref(sharedPreferences)
    .userDao(userDao)
    .context(context)
    .build()

  val domainComponent = DaggerNavigatorComponent.builder()
    .repository(dataComponent.getRepository())
    .build()

}