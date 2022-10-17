package com.lofigroup.seeyau.data.chat.local.models

sealed class MessageContent(
) {

  object Photo: MessageContent()

}

enum class ContentType {
  PLAIN, LIKE
}