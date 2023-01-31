package com.lofigroup.seeyau

import android.app.Application
import com.lofigroup.core.bluetooth.BluetoothRequesterChannel
import com.lofigroup.core.bluetooth.BluetoothRequesterProvider
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.core.permission.PermissionRequestChannel
import com.lofigroup.core.permission.PermissionRequestChannelProvider
import com.lofigroup.notifications.NotificationRequester
import com.lofigroup.notifications.NotificationRequesterProvider
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannelProvider
import com.lofigroup.seeyau.domain.auth.api.AuthModule
import com.lofigroup.seeyau.domain.auth.api.AuthModuleProvider
import com.lofigroup.seeyau.domain.base.api.BaseComponentProvider
import com.lofigroup.seeyau.domain.base.di.BaseModuleComponent
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.domain.settings.api.SettingsComponentProvider
import com.lofigroup.seeyau.domain.settings.di.SettingsComponent
import com.sillyapps.core.ui.app_lifecycle.AppLifecycle
import com.sillyapps.core.ui.app_lifecycle.model.AppLifecycleState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class App: Application(), AuthModuleProvider, NavigatorComponentProvider, ProfileComponentProvider, ChatComponentProvider, SettingsComponentProvider,
  PermissionRequestChannelProvider, BaseComponentProvider, BluetoothRequesterProvider, MainScreenEventChannelProvider, NotificationRequesterProvider, AppLifecycle {

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
    appModules.appLifecycle.init()
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

  override fun provideBaseComponent(): BaseModuleComponent {
    return appModules.baseDataModule.domainComponent
  }

  override fun provideBluetoothRequester(): BluetoothRequesterChannel {
    return appModules.appComponent.getBluetoothRequester()
  }

  override fun providerMainScreenEventChannel(): MainScreenEventChannel {
    return appModules.appComponent.getMainScreenEventChannel()
  }

  override fun provideNotificationRequester(): NotificationRequester {
    return appModules.appComponent.getNotificationRequester()
  }

  override fun observeLifecycle() = appModules.appLifecycle.observeLifecycle()

}