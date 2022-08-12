package com.lofigroup.seeyau.domain.auth.model

data class Token(
  val value: String,
  val expiresIn: Long
) {
  fun expired(): Boolean {
    return System.currentTimeMillis() >= expiresIn
  }
}