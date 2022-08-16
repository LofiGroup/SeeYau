package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.backend_api.models.AccessResponse
import com.lofigroup.backend_api.models.TokenDataModel
import com.lofigroup.seeyau.domain.auth.model.Token
import com.squareup.moshi.JsonClass
import kotlin.math.exp

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