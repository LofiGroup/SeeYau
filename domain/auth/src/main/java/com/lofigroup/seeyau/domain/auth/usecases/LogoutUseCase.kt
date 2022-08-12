package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.seeyau.domain.auth.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke() {
    repository.logout()
  }

}