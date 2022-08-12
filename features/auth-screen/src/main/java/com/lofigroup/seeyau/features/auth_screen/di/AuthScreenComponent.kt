package com.lofigroup.seeyau.features.auth_screen.di

import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.features.auth_screen.AuthScreenViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.Component

@ScreenScope
@Component(
  dependencies = [AuthComponent::class]
)
interface AuthScreenComponent {

  fun getAuthScreenViewModel(): AuthScreenViewModel

  @Component.Builder
  interface Builder {
    fun authComponent(authComponent: AuthComponent): Builder

    fun build(): AuthScreenComponent
  }

}