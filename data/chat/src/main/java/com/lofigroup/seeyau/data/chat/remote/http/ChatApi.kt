package com.lofigroup.seeyau.data.chat.remote.http

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatUpdatesDto
import com.lofigroup.seeyau.data.chat.remote.websocket.models.responses.MessageIsReceivedResponse
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {

  @GET("/api/chat/get-all-updated")
  suspend fun getChatUpdates(@Query("from_date") fromDate: Long): Response<List<ChatUpdatesDto>>

  @GET("/api/chat/get-chat-updates/{chat_id}")
  suspend fun getChatData(@Path("chat_id") chatId: Long, @Query("from_date") fromDate: Long): Response<ChatUpdatesDto>

  @Multipart
  @POST("/api/chat/send-chat-media")
  suspend fun sendChatMedia(
    @PartMap form: MutableMap<String, RequestBody>,
    @Part media: MultipartBody.Part
  ): Response<MessageIsReceivedResponse>

}