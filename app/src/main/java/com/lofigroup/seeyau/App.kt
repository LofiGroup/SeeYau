package com.lofigroup.seeyau

import android.app.Application
import com.lofigroup.data.navigator.di.DaggerNavigatorDataComponent
import com.lofigroup.domain.navigator.di.DaggerNavigatorComponent
import timber.log.Timber

class App: Application() {

  val navigatorDataComponent by lazy {
    DaggerNavigatorDataComponent.builder()
      .context(applicationContext)
      .build()
  }

  val navigatorComponent by lazy {
    DaggerNavigatorComponent.builder()
      .repository(navigatorDataComponent.getRepository())
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

}