package com.lofigroup.seeyau

import android.app.Application
import com.lofigroup.data.navigator.di.DaggerNavigatorDataComponent
import com.lofigroup.domain.navigator.api.NavigatorComponentProvider
import com.lofigroup.domain.navigator.di.DaggerNavigatorComponent
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.seeyau.data.auth.di.DaggerAuthDataComponent
import com.lofigroup.seeyau.di.DaggerAppComponent
import com.lofigroup.seeyau.domain.auth.di.DaggerAuthComponent
import kotlinx.coroutines.MainScope
import timber.log.Timber

class App: Application(), NavigatorComponentProvider {

  private val appScope = MainScope()

  val appComponent by lazy {
    DaggerAppComponent.builder()
      .context(applicationContext)
      .build()
  }

  val navigatorDataComponent by lazy {
    DaggerNavigatorDataComponent.builder()
      .context(applicationContext)
      .userDao(appComponent.getDatabase().userDao)
      .sharedPref(appComponent.getSharedPref())
      .appScope(appScope)
      .authComponent(authComponent)
      .build()
  }

  val navigatorComponent by lazy {
    DaggerNavigatorComponent.builder()
      .repository(navigatorDataComponent.getRepository())
      .build()
  }

  val authDataComponent by lazy {
    DaggerAuthDataComponent.builder()
      .sharedPref(appComponent.getSharedPref())
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

}