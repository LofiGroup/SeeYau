package com.lofigroup.domain.navigator

import com.lofigroup.domain.navigator.model.NearbyUser
import kotlinx.coroutines.flow.Flow

interface NavigatorRepository {

  suspend fun pullData()

  suspend fun notifyUserWithIdWasFound(id: Long)

  fun getNearbyUsers(): Flow<List<NearbyUser>>

}