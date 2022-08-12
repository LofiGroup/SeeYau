package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.seeyau.domain.auth.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(): Boolean {
    val token = repository.getToken()
    if (token == null || token.expired())
      return false

    return true
  }

}