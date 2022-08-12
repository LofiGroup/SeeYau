package com.lofigroup.seeyau.domain.auth.di

import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.usecases.IsLoggedInUseCase
import com.lofigroup.seeyau.domain.auth.usecases.LoginUseCase
import com.lofigroup.seeyau.domain.auth.usecases.LogoutUseCase
import com.lofigroup.seeyau.domain.auth.usecases.RegisterUseCase
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface AuthComponent {

  fun isLoggedInUseCase(): IsLoggedInUseCase
  fun loginUseCase(): LoginUseCase
  fun logoutUseCase(): LogoutUseCase
  fun registerUseCase(): RegisterUseCase

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun authRepository(authRepository: AuthRepository): Builder

    fun build(): AuthComponent

  }

}