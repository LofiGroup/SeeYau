package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.Access
import javax.inject.Inject

class LoginUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(access: Access): Resource<Unit> {
    return repository.login(access)
  }

}