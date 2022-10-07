package com.lofigroup.seeyau.data.chat.local

import androidx.room.*
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import kotlinx.coroutines.flow.*

@Dao
interface ChatDao {

  @Query("select * from chats")
  fun observeChats(): Flow<List<ChatEntity>>

  @Query("select * from chats where id = :id")
  fun observeChat(id: Long): Flow<ChatEntity>

  @Query("select * from messages where chatId = :chatId order by createdIn desc")
  fun observeChatMessages(chatId: Long): Flow<List<MessageEntity>>

  @Query("select messages.* from chats, messages where chats.partnerId = :userId and messages.author = :userId and createdIn > lastVisited order by createdIn desc")
  fun observeUserNewMessages(userId: Long): Flow<List<MessageEntity>>

  @Query("select * from messages where chatId = :chatId order by createdIn desc limit 1")
  fun observeLastMessage(chatId: Long): Flow<MessageEntity?>


  @Query("select partnerLastVisited from chats where id = :chatId")
  suspend fun getPartnerLastVisited(chatId: Long): Long

  @Query("select partnerId from chats where id = :chatId")
  suspend fun getUserIdFromChatId(chatId: Long): Long?

  @Query("select id from chats where partnerId = :userId")
  suspend fun getChatIdFromUserId(userId: Long): Long?

  @Query("select * from messages order by createdIn desc limit 1")
  suspend fun getLastMessage(): MessageEntity?


  @Query("update chats set lastVisited = :lastVisited where id = :chatId")
  suspend fun updateChatLastVisited(chatId: Long, lastVisited: Long)

  @Query("update chats set partnerLastVisited = :lastVisited where id = :chatId")
  suspend fun updateChatPartnerLastVisited(chatId: Long, lastVisited: Long)

  @Query("update messages set isRead = 1 where chatId = :chatId and createdIn > :fromDate")
  suspend fun markMessagesAsRead(chatId: Long, fromDate: Long)

  @Transaction
  suspend fun markMessages(chatId: Long) {
    val lastVisited = getPartnerLastVisited(chatId)
    markMessagesAsRead(chatId, lastVisited)
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChat(chat: ChatEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessages(messages: List<MessageEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: MessageEntity)

  @Transaction
  suspend fun insertOneMessages(messageDto: ChatMessageDto, myId: Long) {
    val lastVisited = getPartnerLastVisited(messageDto.chatId)
    insertMessage(messageDto.toMessageEntity(myId, lastVisited))
  }

  @Transaction
  suspend fun insertAllMessages(chatId: Long, messages: List<ChatMessageDto>, myId: Long) {
    val lastVisited = getPartnerLastVisited(chatId)
    insertMessages(messages.map { it.toMessageEntity(myId, lastVisited) })
  }

}