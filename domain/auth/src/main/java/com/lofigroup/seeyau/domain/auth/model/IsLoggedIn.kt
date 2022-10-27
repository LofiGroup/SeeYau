package com.lofigroup.seeyau.domain.auth.model

sealed interface LoggedInStatus {
  object LoggedIn: LoggedInStatus
  object CantAccessServer: LoggedInStatus
  object InvalidToken: LoggedInStatus
  class UnknownError(val errorMessage: String): LoggedInStatus
}
