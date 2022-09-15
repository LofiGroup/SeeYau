package com.lofigroup.seeyau.data.chat.remote.websocket.models

import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest

fun ChatMessageRequest.toWebSocketRequest() =
  WebSocketRequest.SendMessage(
    chatId = chatId,
    message = message
  )