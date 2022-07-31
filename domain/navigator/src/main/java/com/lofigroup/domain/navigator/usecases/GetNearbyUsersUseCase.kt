package com.lofigroup.domain.navigator.usecases

import com.lofigroup.core.util.Resource
import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearbyUsersUseCase @Inject constructor(
  private val repo: NavigatorRepository
) {

  operator fun invoke(): Flow<Resource<List<User>>> {
    return repo.getNearbyUsers()
  }

}