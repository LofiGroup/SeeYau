package com.lofigroup.seeyau

import android.content.Context
import com.lofigroup.backend_api.di.DaggerBackendApiComponent
import com.lofigroup.data.navigator.api.NavigatorModule
import com.lofigroup.seeyau.data.auth.api.AuthModule
import com.lofigroup.seeyau.data.chat.api.ChatModule
import com.lofigroup.seeyau.data.profile.api.ProfileModule
import com.lofigroup.seeyau.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope

class AppModules(
  private val appScope: CoroutineScope,
  private val appContext: Context,
) {

  val appComponent by lazy {
    DaggerAppComponent.builder()
      .context(appContext)
      .build()
  }

  private val backend by lazy {
    DaggerBackendApiComponent.builder()
      .sharedPref(appComponent.getSharedPref())
      .build()
  }

  val navigatorModule by lazy {
    NavigatorModule(
      context = appContext,
      userDao = appComponent.getDatabase().userDao,
      sharedPreferences = appComponent.getSharedPref(),
      appScope = appScope,
      baseRetrofit = backend.getRetrofit()
    )
  }

  val authModule by lazy {
    AuthModule(
      baseRetrofit = backend.getRetrofit(),
      tokenStore = backend.tokenStore()
    )
  }

  val profileModule by lazy {
    ProfileModule(
      appScope = appScope,
      baseRetrofit = backend.getRetrofit(),
      userDao = appComponent.getDatabase().userDao,
      sharedPref = appComponent.getSharedPref(),
      contentResolver = appComponent.getContentResolver()
    )
  }

  val chatModule by lazy {
    ChatModule(
      baseRetrofit = backend.getRetrofit(),
      userDao = appComponent.getDatabase().userDao,
      chatDao = appComponent.getDatabase().chatDao,
      sharedPreferences = appComponent.getSharedPref(),
      profileDataSource = profileModule.dataComponent.getProfileDataSource(),
      httpclient = backend.getHttpClient(),
      ioScope = appScope
    )
  }



}