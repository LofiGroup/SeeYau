package com.lofigroup.backend_api

import com.lofigroup.backend_api.models.TokenDataModel
import com.lofigroup.core.util.ResourceState
import kotlinx.coroutines.flow.Flow

interface TokenStore {

  fun getTokenState(): Flow<ResourceState>

  fun saveToken(token: TokenDataModel)

  fun getToken(): TokenDataModel?

  fun forgetToken()

}
