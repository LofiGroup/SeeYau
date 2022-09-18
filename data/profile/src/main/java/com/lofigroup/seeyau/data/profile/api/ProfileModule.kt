package com.lofigroup.seeyau.data.profile.api

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.seeyau.data.profile.di.DaggerProfileDataComponent
import com.lofigroup.seeyau.domain.profile.di.DaggerProfileComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

class ProfileModule(
  appScope: CoroutineScope,
  baseRetrofit: Retrofit,
  userDao: UserDao,
  sharedPref: SharedPreferences,
  contentResolver: ContentResolver
) {

  val dataComponent = DaggerProfileDataComponent.builder()
    .appScope(appScope)
    .baseRetrofit(baseRetrofit)
    .userDao(userDao)
    .sharedPref(sharedPref)
    .contentResolver(contentResolver)
    .build()

  val domainComponent = DaggerProfileComponent.builder()
    .profileRepository(dataComponent.getRepository())
    .build()

}