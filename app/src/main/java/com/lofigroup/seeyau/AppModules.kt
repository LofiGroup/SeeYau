package com.lofigroup.seeyau

import android.content.Context
import com.lofigroup.backend_api.api.BaseDataModule
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

  val baseDataModule by lazy {
    BaseDataModule(
      sharedPref = appComponent.getSharedPref()
    )
  }

  val navigatorModule by lazy {
    NavigatorModule(
      context = appContext,
      chatDataHandler = chatModule.dataComponent.chatDataHandler(),
      profileDataHandler = profileModule.dataComponent.getProfileDataHandler(),
      sharedPreferences = appComponent.getSharedPref(),
      appScope = appScope,
      baseRetrofit = baseDataModule.component.getRetrofit(),
      profileRepository = profileModule.dataComponent.getRepository(),
      webSocketChannel = baseDataModule.component.getWebSocketChannel()
    )
  }

  val authModuleImpl by lazy {
    AuthModuleImpl(
      baseRetrofit = baseDataModule.component.getRetrofit(),
      tokenStore = baseDataModule.component.tokenStore(),
      context = appContext,
      userDao = appComponent.getDatabase().userDao,
      databaseHandler = appComponent.getDatabase(),
      dataSyncStateHolder = baseDataModule.component.syncStateHolder()
    )
  }

  val profileModule by lazy {
    ProfileModule(
      appScope = appScope,
      baseRetrofit = baseDataModule.component.getRetrofit(),
      webSocketChannel = baseDataModule.component.getWebSocketChannel(),
      userDao = appComponent.getDatabase().userDao,
      likeDao = appComponent.getDatabase().likeDao,
      blacklistDao = appComponent.getDatabase().blacklistDao,
      sharedPref = appComponent.getSharedPref(),
      contentResolver = appComponent.getContentResolver()
    )
  }

  val chatModule by lazy {
    ChatModule(
      baseRetrofit = baseDataModule.component.getRetrofit(),
      webSocketChannel = baseDataModule.component.getWebSocketChannel(),
      chatDao = appComponent.getDatabase().chatDao,
      sharedPreferences = appComponent.getSharedPref(),
      ioScope = appScope,
      profileDataHandler = profileModule.dataComponent.getProfileDataHandler(),
      context = appContext
    )
  }

  val settingsModule by lazy {
    SettingsModule(
      sharedPreferences = appComponent.getSharedPref()
    )
  }

}