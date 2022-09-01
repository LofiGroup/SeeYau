package com.lofigroup.seeyau

import android.app.Application
import com.lofigroup.backend_api.di.DaggerBackendApiComponent
import com.lofigroup.data.navigator.di.DaggerNavigatorDataComponent
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.di.DaggerNavigatorComponent
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.data.auth.di.DaggerAuthDataComponent
import com.lofigroup.seeyau.data.profile.di.DaggerProfileDataComponent
import com.lofigroup.seeyau.di.DaggerAppComponent
import com.lofigroup.seeyau.domain.auth.di.DaggerAuthComponent
import com.lofigroup.seeyau.domain.chat.api.ChatComponentProvider
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.di.DaggerProfileComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class App: Application(), NavigatorComponentProvider, ProfileComponentProvider, ChatComponentProvider {

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

}