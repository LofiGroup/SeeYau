package com.lofigroup.seeyau.domain.auth.api

interface AuthModuleProvider {

  fun provideAuthModule(): AuthModule

}