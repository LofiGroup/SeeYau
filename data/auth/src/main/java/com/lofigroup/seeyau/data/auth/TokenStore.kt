package com.lofigroup.seeyau.data.auth

import com.lofigroup.seeyau.data.auth.model.TokenDataModel

interface TokenStore {

  fun saveToken(token: TokenDataModel)

  fun getToken(): TokenDataModel?

  fun forgetToken()

}