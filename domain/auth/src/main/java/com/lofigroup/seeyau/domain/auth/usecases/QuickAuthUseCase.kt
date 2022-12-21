package com.lofigroup.seeyau.domain.auth.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.seeyau.domain.auth.AuthRepository
import javax.inject.Inject

class QuickAuthUseCase @Inject constructor(
  private val repository: AuthRepository
) {

  suspend operator fun invoke(imageUri: String): Resource<Unit> {
    return repository.quickAuth(imageUri)
  }

}