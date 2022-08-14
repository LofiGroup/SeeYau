package com.lofigroup.domain.navigator.di

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.usecases.GetNearbyUsersUseCase
import com.lofigroup.domain.navigator.usecases.NotifyDeviceIsLostUseCase
import com.lofigroup.domain.navigator.usecases.NotifyUserIsNearbyUseCase
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component
interface NavigatorComponent {

  fun getNearbyUsersUseCase(): GetNearbyUsersUseCase
  fun notifyUserIsNearby(): NotifyUserIsNearbyUseCase
  fun notifyUserILostUseCase(): NotifyDeviceIsLostUseCase

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun repository(navigatorRepository: NavigatorRepository): Builder

    fun build(): NavigatorComponent

  }

}