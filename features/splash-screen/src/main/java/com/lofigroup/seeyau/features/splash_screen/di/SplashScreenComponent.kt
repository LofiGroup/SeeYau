package com.lofigroup.seeyau.features.splash_screen.di

import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.features.splash_screen.SplashScreenViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.Component

@ScreenScope
@Component(dependencies = [AuthComponent::class])
interface SplashScreenComponent {

  fun getViewModel(): SplashScreenViewModel

  @Component.Builder
  interface Builder {
    fun authComponent(authComponent: AuthComponent): Builder

    fun build(): SplashScreenComponent
  }

}