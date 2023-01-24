package com.lofigroup.seeyau

import android.content.Context
import com.lofigroup.backend_api.api.BaseDataModule
import com.lofigroup.data.navigator.api.NavigatorModule
import com.lofigroup.seeyau.data.auth.api.AuthModuleImpl
import com.lofigroup.seeyau.data.chat.api.ChatModule
import com.lofigroup.seeyau.data.profile.api.ProfileModule
import com.lofigroup.seeyau.data.settings.api.SettingsModule
import com.lofigroup.seeyau.di.DaggerAppComponent
import com.lofigroup.seeyau.features.data_sync_service.di.buildDataSyncer
import kotlinx.coroutines.CoroutineScope
import com.lofigroup.seeyau.di.DaggerMainActivityComponent

class AppModules(
  private val appScope: CoroutineScope,
  private val appContext: Context,
) {

  val appComponent by lazy {
    DaggerAppComponent.builder()
      .context(appContext)
      .build()
  }

  val mainActivityComponent by lazy {
    DaggerMainActivityComponent.builder()
      .dataSyncer(dataSyncer)
      .build()
  }

  val dataSyncer by lazy {
    buildDataSyncer(
      chatComponent = chatModule.domainComponent,
      navigatorComponent = navigatorModule.domainComponent,
      profileComponent = profileModule.domainComponent,
      baseComponent = baseDataModule.domainComponent,
      authModule = authModuleImpl,
      chatNotificationBuilder = appComponent.getChatNotificationBuilder(),
      context = appContext,
      mainScreenEventChannel = appComponent.getMainScreenEventChannel()
    )
  }

  val baseDataModule by lazy {
    BaseDataModule(
      sharedPref = appComponent.getSharedPref(),
      appScope = appScope,
      context = appContext
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
      contentResolver = appComponent.getContentResolver(),
      fileDownloader = baseDataModule.component.getFileDownloader()
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
      userNotificationChannel = baseDataModule.domainComponent.getUserNotificationChannel(),
      context = appContext,
      chatNotificationBuilder = appComponent.getChatNotificationBuilder()
    )
  }

  val settingsModule by lazy {
    SettingsModule(
      sharedPreferences = appComponent.getSharedPref()
    )
  }

}