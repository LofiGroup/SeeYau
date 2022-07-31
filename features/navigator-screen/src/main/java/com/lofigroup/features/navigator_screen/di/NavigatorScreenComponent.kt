package com.lofigroup.features.navigator_screen.di

import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.navigator_screen.ui.NavigatorScreenViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.Component

@ScreenScope
@Component(
  modules = [],
  dependencies = [NavigatorComponent::class]
)
interface NavigatorScreenComponent {

  fun getViewModel(): NavigatorScreenViewModel

  @Component.Builder
  interface Builder {

    fun navigatorComponent(component: NavigatorComponent): Builder

    fun build(): NavigatorScreenComponent

  }

}