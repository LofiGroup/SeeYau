package com.lofigroup.backend_api.websocket

interface WebSocketChannelListener {

  fun onMessage(message: String)

}