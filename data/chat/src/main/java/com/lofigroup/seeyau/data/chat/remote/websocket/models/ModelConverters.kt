package com.lofigroup.seeyau.data.chat.remote.websocket.models

import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.SendMessageWsRequest
import com.lofigroup.seeyau.domain.chat.models.ChatMessageRequest

fun ChatMessageRequest.toWebSocketRequest(localId: Long) =
  SendMessageWsRequest(
    chatId = chatId,
    message = message,
    localId = localId
  )