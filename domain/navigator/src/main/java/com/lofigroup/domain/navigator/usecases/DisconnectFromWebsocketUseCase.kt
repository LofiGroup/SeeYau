package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import javax.inject.Inject

class DisconnectFromWebsocketUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  suspend operator fun invoke() {
    repository.disconnectFromWebsocket()
  }

}