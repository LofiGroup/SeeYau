package com.lofigroup.seeyau.domain.auth.di

import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.usecases.AuthorizeUseCase
import com.lofigroup.seeyau.domain.auth.usecases.IsLoggedInUseCase
import com.lofigroup.seeyau.domain.auth.usecases.LogoutUseCase
import com.lofigroup.seeyau.domain.auth.usecases.StartAuthUseCase
import com.sillyapps.core.di.AppScope
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component()
interface AuthComponent {

  fun getRepository(): AuthRepository

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun authRepository(authRepository: AuthRepository): Builder

    fun build(): AuthComponent

  }

}