package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.domain.auth.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(): Result {
    return repository.check()
  }

}