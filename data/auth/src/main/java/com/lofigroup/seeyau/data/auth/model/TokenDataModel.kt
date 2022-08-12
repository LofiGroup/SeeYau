package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.seeyau.domain.auth.model.Token
import com.squareup.moshi.JsonClass
import kotlin.math.exp

@JsonClass(generateAdapter = true)
data class TokenDataModel(
  val value: String,
  val expiresIn: Long
)

fun AccessResponse.toTokenDataModel() =
  TokenDataModel(
    value = token,
    expiresIn = System.currentTimeMillis() + 24 * 3600000
  )

fun TokenDataModel.toToken() =
  Token(
    value = value,
    expiresIn = expiresIn
  )