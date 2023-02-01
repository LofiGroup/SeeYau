package com.lofigroup.domain.navigator

import com.lofigroup.domain.navigator.model.NearbyUser
import kotlinx.coroutines.flow.Flow

interface NavigatorRepository {

  suspend fun notifyUserWithIdWasFound(id: Long)

  fun connectToWebsocket()
  fun disconnectFromWebsocket()

  fun getNearbyUsers(): Flow<List<NearbyUser>>

}