package com.lofigroup.seeyau.domain.auth.api

import com.lofigroup.core.util.ResourceState
import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import kotlinx.coroutines.flow.Flow

interface AuthModule {

  fun observeState(): Flow<ResourceState>

  fun domainComponent(): AuthComponent

}