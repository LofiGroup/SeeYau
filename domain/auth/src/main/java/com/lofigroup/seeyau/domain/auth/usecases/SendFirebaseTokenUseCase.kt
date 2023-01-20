package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.seeyau.domain.auth.AuthRepository
import javax.inject.Inject

class SendFirebaseTokenUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(token: String? = null) {
    repository.sendFirebaseToken(token)
  }
}