package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  operator fun invoke(): User {
    return repository.getMyProfile()
  }

}