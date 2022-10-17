package com.lofigroup.seeyau

import android.content.Context
import com.lofigroup.backend_api.di.DaggerBackendApiComponent
import com.lofigroup.data.navigator.api.NavigatorModule
import com.lofigroup.seeyau.data.auth.api.AuthModuleImpl
import com.lofigroup.seeyau.data.chat.api.ChatModule
import com.lofigroup.seeyau.data.profile.api.ProfileModule
import com.lofigroup.seeyau.data.settings.api.SettingsModule
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
      likeDao = appComponent.getDatabase().likeDao,
      sharedPreferences = appComponent.getSharedPref(),
      appScope = appScope,
      baseRetrofit = backend.getRetrofit(),
      chatDao = appComponent.getDatabase().chatDao,
      profileRepository = profileModule.dataComponent.getRepository()
    )
  }

  val authModuleImpl by lazy {
    AuthModuleImpl(
      baseRetrofit = backend.getRetrofit(),
      tokenStore = backend.tokenStore()
    )
  }

  val profileModule by lazy {
    ProfileModule(
      appScope = appScope,
      baseRetrofit = backend.getRetrofit(),
      webSocketChannel = backend.getWebSocketChannel(),
      userDao = appComponent.getDatabase().userDao,
      likeDao = appComponent.getDatabase().likeDao,
      sharedPref = appComponent.getSharedPref(),
      contentResolver = appComponent.getContentResolver()
    )
  }

  val chatModule by lazy {
    ChatModule(
      baseRetrofit = backend.getRetrofit(),
      webSocketChannel = backend.getWebSocketChannel(),
      userDao = appComponent.getDatabase().userDao,
      chatDao = appComponent.getDatabase().chatDao,
      likeDao = appComponent.getDatabase().likeDao,
      sharedPreferences = appComponent.getSharedPref(),
      profileDataSource = profileModule.dataComponent.getProfileDataSource(),
      ioScope = appScope,
      profileRepository = profileModule.dataComponent.getRepository()
    )
  }

  val settingsModule by lazy {
    SettingsModule(
      sharedPreferences = appComponent.getSharedPref()
    )
  }

}