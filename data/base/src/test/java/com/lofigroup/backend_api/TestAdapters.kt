package com.lofigroup.backend_api

import com.lofigroup.backend_api.websocket.models.WebSocketResponse
import org.junit.Test

class TestAdapters {

  @Test
  fun testWebSocketResponseAdapter() {
    val json = "{\"type\": \"chat\", \"data\": {\"type\": \"response\", \"response_type\": \"online_status_changed\", \"user_id\": 27}}"

    val response = WebSocketResponse.fromJson(json)

    assert(response != null)
    assert(response!!.type == "chat")
    assert(response.data == "{\"type\": \"response\", \"response_type\": \"online_status_changed\", \"user_id\": 27}")
  }

}