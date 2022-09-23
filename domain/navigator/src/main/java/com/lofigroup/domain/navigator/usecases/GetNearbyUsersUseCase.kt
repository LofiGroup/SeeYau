package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import com.lofigroup.domain.navigator.model.NearbyUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearbyUsersUseCase @Inject constructor(
  private val repo: NavigatorRepository
) {

  operator fun invoke(): Flow<List<NearbyUser>> {
    return repo.getNearbyUsers()
  }

}