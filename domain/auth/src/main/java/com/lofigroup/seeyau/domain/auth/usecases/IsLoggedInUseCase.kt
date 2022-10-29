package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.LoggedInStatus
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(): LoggedInStatus {
    return repository.check()
  }

}