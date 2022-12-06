package com.lofigroup.seeyau

import android.app.Application
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.common.ui.permissions.PermissionRequestChannel
import com.lofigroup.seeyau.common.ui.permissions.PermissionRequestChannelProvider
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.api.AuthModuleProvider
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.api.SettingsComponentProvider
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import kotlinx.coroutines.MainScope
import timber.log.Timber

class App: Application(), AuthModuleProvider, NavigatorComponentProvider, ProfileComponentProvider, ChatComponentProvider, SettingsComponentProvider, PermissionRequestChannelProvider {

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

  override fun providePermissionChannel(): PermissionRequestChannel {
    return appModules.appComponent.getPermissionChannel()
  }

}