package com.lofigroup.domain.navigator.usecases

import com.lofigroup.domain.navigator.NavigatorRepository
import javax.inject.Inject

class ConnectToWebsocketUseCase @Inject constructor(
  private val repository: NavigatorRepository
) {

  operator fun invoke() {
    repository.connectToWebsocket()
  }

}