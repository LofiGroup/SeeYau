package com.lofigroup.domain.navigator

import com.lofigroup.domain.navigator.model.NearbyUser
import kotlinx.coroutines.flow.Flow

interface NavigatorRepository {

  fun getNearbyUsers(): Flow<List<NearbyUser>>

  suspend fun pullData()

  suspend fun notifyUserWithIdWasFound(id: Long)

}