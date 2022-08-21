package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.backend_api.models.TokenDataModel
import com.lofigroup.seeyau.domain.auth.model.Token

fun AccessResponse.toTokenDataModel() =
  TokenDataModel(
    value = token,
    expiresIn = expiresIn
  )

fun TokenDataModel.toToken() =
  Token(
    value = value,
    expiresIn = expiresIn
  )