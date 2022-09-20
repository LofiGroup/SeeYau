package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.StartAuth
import javax.inject.Inject

class StartAuthUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(startAuth: StartAuth): Resource<Unit> {
    return repository.startAuth(startAuth)
  }

}