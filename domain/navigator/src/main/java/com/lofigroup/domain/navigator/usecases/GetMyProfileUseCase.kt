package com.lofigroup.domain.navigator.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  operator fun invoke(): Flow<Resource<User>> {
    return repository.getMyProfile()
  }

}