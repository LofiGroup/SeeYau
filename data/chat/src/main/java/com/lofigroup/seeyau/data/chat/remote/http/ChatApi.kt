package com.lofigroup.seeyau.data.chat.remote.http

import com.lofigroup.seeyau.data.chat.remote.http.models.ChatUpdatesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

  @GET("/api/chat/get-all-updated")
  suspend fun getChatUpdates(@Query("from_date") fromDate: Long): Response<List<ChatUpdatesDto>>

  @GET("/api/chat/get-chat-updates/{chat_id}")
  suspend fun getChatData(@Path("chat_id") chatId: Long): Response<ChatUpdatesDto>

  @POST("/api/chat/add-friend/{user_id}")
  suspend fun addToFriends(@Path("user_id") userId: Int): Response<Unit>

  @POST("/api/chat/remove-friend")
  suspend fun removeFromFriends(@Path("user_id") userId: Int): Response<Unit>

}