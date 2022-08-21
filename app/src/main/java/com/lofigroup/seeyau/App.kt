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
import com.lofigroup.seeyau.domain.profile.api.ProfileComponentProvider
import com.lofigroup.seeyau.domain.profile.di.DaggerProfileComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import kotlinx.coroutines.MainScope
import timber.log.Timber

class App: Application(), NavigatorComponentProvider, ProfileComponentProvider {

  private val appScope = MainScope()

  val appComponent by lazy {
    DaggerAppComponent.builder()
      .context(applicationContext)
      .build()
  }

  private val backend by lazy {
    DaggerBackendApiComponent.builder()
      .sharedPref(appComponent.getSharedPref())
      .build()
  }

  private val navigatorDataComponent by lazy {
    DaggerNavigatorDataComponent.builder()
      .context(applicationContext)
      .userDao(appComponent.getDatabase().userDao)
      .sharedPref(appComponent.getSharedPref())
      .appScope(appScope)
      .baseRetrofit(backend.getRetrofit())
      .build()
  }

  private val authDataComponent by lazy {
    DaggerAuthDataComponent.builder()
      .baseRetrofit(backend.getRetrofit())
      .tokenStore(backend.tokenStore())
      .build()
  }

  private val profileDataComponent by lazy {
    DaggerProfileDataComponent.builder()
      .appScope(appScope)
      .baseRetrofit(backend.getRetrofit())
      .build()
  }

  val navigatorComponent by lazy {
    DaggerNavigatorComponent.builder()
      .repository(navigatorDataComponent.getRepository())
      .build()
  }

  val profileComponent by lazy {
    DaggerProfileComponent.builder()
      .profileRepository(profileDataComponent.getRepository())
      .build()
  }

  val authComponent by lazy {
    DaggerAuthComponent.builder()
      .authRepository(authDataComponent.getRepository())
      .build()
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
    return navigatorComponent
  }

  override fun provideProfileComponent(): ProfileComponent {
    return profileComponent
  }

}