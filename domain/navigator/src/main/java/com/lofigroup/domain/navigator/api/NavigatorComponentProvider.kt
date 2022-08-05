package com.lofigroup.domain.navigator.api

import com.lofigroup.domain.navigator.di.NavigatorComponent

interface NavigatorComponentProvider {
  fun provideNavigatorComponent(): NavigatorComponent
}