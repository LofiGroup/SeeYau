package com.lofigroup.seeyau.features.auth_screen.model

data class AuthScreenState(
  val authMode: AuthMode = AuthMode.SIGN_IN,
  val error: String? = null,
  val isSigned: Boolean = false
)

enum class AuthMode {
  SIGN_IN, SIGN_UP
}