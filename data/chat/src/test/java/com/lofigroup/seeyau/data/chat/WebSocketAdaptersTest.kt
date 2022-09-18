package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.SendMessageWsRequest
import com.lofigroup.seeyau.data.chat.remote.websocket.models.requests.WebSocketRequest
import com.squareup.moshi.Moshi
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WebSocketAdaptersTest {

  @Test
  fun testMoshiCodeGen() {
    val testObject = SendMessageWsRequest(message = "", chatId = 0L)
    val adapter = Moshi.Builder().build().adapter(SendMessageWsRequest::class.java)
    val json = adapter.toJson(testObject)
    println(json)
    assert(!json.contains("\\"))
  }
}