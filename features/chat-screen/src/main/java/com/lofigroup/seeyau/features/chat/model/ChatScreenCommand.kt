package com.lofigroup.seeyau.features.chat.model

sealed interface ChatScreenCommand {

  object ToLatestMessage: ChatScreenCommand

  object Exit: ChatScreenCommand

  class ShowToast(val message: String): ChatScreenCommand

}
