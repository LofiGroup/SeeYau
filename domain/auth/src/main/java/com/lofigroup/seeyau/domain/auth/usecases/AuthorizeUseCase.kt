package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.auth.AuthRepository
import com.lofigroup.seeyau.domain.auth.model.Access
import com.lofigroup.seeyau.domain.auth.model.AuthResponse
import javax.inject.Inject

class AuthorizeUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(access: Access): Resource<AuthResponse> {
    return repository.authorize(access)
  }

}