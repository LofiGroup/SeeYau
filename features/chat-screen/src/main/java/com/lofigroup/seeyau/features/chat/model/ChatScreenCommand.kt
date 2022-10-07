package com.lofigroup.seeyau.features.chat.model

sealed interface ChatScreenCommand {

  object ToLatestMessage: ChatScreenCommand

  class ShowToast(val message: String): ChatScreenCommand

}
