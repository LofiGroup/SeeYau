package com.lofigroup.backend_api

import com.lofigroup.backend_api.models.TokenDataModel

interface TokenStore {

  fun saveToken(token: TokenDataModel)

  fun getToken(): TokenDataModel?

  fun forgetToken()

}