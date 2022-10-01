package com.lofigroup.seeyau

import android.app.Application
import android.content.Intent
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.api.AuthModuleProvider
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.api.SettingsComponentProvider
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.lofigroup.seeyau.features.data_sync_service.DataSyncServiceImpl
import kotlinx.coroutines.MainScope
import timber.log.Timber

class App: Application(), AuthModuleProvider, NavigatorComponentProvider, ProfileComponentProvider, ChatComponentProvider, SettingsComponentProvider {

  private val appScope = MainScope()

  val appModules by lazy {
    AppModules(
      appScope = appScope,
      appContext = applicationContext
    )
  }

  override fun onCreate() {
    super.onCreate()
    initTimber()
  }

  private fun initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun provideNavigatorComponent(): NavigatorComponent {
    return appModules.navigatorModule.domainComponent
  }

  override fun provideProfileComponent(): ProfileComponent {
    return appModules.profileModule.domainComponent
  }

  override fun provideChatComponent(): ChatComponent {
    return appModules.chatModule.domainComponent
  }

  override fun provideAuthModule(): AuthModule {
    return appModules.authModuleImpl
  }

  override fun provideSettingsModule(): SettingsComponent {
    return appModules.settingsModule.domainComponent
  }

}