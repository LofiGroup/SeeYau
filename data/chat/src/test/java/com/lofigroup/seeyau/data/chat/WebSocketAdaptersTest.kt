package com.lofigroup.seeyau.data.chat

import com.lofigroup.seeyau.data.chat.remote.websocket.models.WebSocketRequest
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WebSocketAdaptersTest {

  @Test
  fun testMoshiCodeGen() {
    val testObject = WebSocketRequest.SendMessage(message = "", chatId = 0L)
    val adapter = Moshi.Builder().build().adapter(WebSocketRequest.SendMessage::class.java)
    val json = adapter.toJson(testObject)
    println(json)
    assert(!json.contains("\\"))
  }
}