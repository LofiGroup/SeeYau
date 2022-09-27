package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.backend_api.models.TokenDataModel

fun TokenResponse.toTokenDataModel() =
  TokenDataModel(
    token = token
  )