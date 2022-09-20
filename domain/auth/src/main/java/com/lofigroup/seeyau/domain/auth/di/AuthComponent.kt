package com.lofigroup.seeyau.domain.auth.di

import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.usecases.*
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface AuthComponent {

  fun isLoggedInUseCase(): IsLoggedInUseCase
  fun logoutUseCase(): LogoutUseCase
  fun authorizeUseCase(): AuthorizeUseCase
  fun startAuthUseCase(): StartAuthUseCase

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun authRepository(authRepository: AuthRepository): Builder

    fun build(): AuthComponent

  }

}