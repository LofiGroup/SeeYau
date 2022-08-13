package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.Token
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  operator fun invoke(): Token? {
    return repository.getToken()
  }

}