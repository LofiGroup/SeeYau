package com.lofigroup.seeyau.data.chat.local

import androidx.room.*
import com.lofigroup.seeyau.data.chat.local.models.*
import com.lofigroup.seeyau.data.chat.remote.http.models.ChatMessageDto
import com.lofigroup.seeyau.data.chat.remote.http.models.toMessageEntity
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.domain.profile.model.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

  @Query("select * from chats")
  fun observeChats(): Flow<List<ChatEntity>>

  @Query("select * from chats where id = :id")
  fun observeChat(id: Long): Flow<ChatEntity>

  @Query("select * from messages where chatId = :chatId order by createdIn desc")
  fun observeChatMessages(chatId: Long): Flow<List<MessageEntity>>

  @Query("select messages.* from chats, messages where chats.partnerId = :userId and messages.author = :userId and messages.createdIn > chats.lastVisited order by messages.createdIn desc")
  fun observeUserNewMessages(userId: Long): Flow<List<MessageEntity>>

  @Query("select * from messages where chatId = :chatId order by createdIn desc limit 1")
  fun observeLastMessage(chatId: Long): Flow<MessageEntity?>


  @Query("select * from chats where id = :chatId")
  suspend fun getChat(chatId: Long): ChatEntity

  @Query("select partnerLastVisited from chats where id = :chatId")
  suspend fun getPartnerLastVisited(chatId: Long): Long

  @Query("select partnerId from chats where id = :chatId")
  suspend fun getUserIdFromChatId(chatId: Long): Long?

  @Query("select id from chats where partnerId = :userId")
  suspend fun getChatIdFromUserId(userId: Long): Long?

  @Query("select createdIn from messages where id < ${MessageEntity.LOCAL_MESSAGES_ID_OFFSET} order by createdIn desc limit 1")
  suspend fun getLastMessageCreatedIn(): Long?

  @Query("select * from messages where id = :id")
  suspend fun getMessage(id: Long): MessageEntity?

  @Query("select users.* from users, chats where chats.id = :chatId and users.id = chats.partnerId")
  suspend fun getUserFromChatId(chatId: Long): UserEntity

  @Query("select messages.*, chats.lastVisited from chats, messages where chats.id = messages.chatId and messages.createdIn > chats.lastVisited order by messages.createdIn desc")
  suspend fun getNewMessages(): List<MessageEntity>


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

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertChat(chat: ChatEntity): Long

  @Update(entity = ChatEntity::class)
  suspend fun updateChat(chat: ChatUpdate)

  @Update(entity = MessageEntity::class)
  suspend fun updateMessageExtra(messageExtraUpdate: MessageExtraUpdate)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessages(messages: List<MessageEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: MessageEntity)

  @Transaction
  suspend fun popMessage(id: Long): MessageEntity? {
    val message = getMessage(id) ?: return null
    deleteMessage(id)
    return message
  }

  @Query("delete from messages where id = :id")
  suspend fun deleteMessage(id: Long)

  @Transaction
  suspend fun insertNewMessage(message: MessageEntity) {
    val lastVisited = getPartnerLastVisited(message.chatId)
    insertMessage(message.copy(isRead = lastVisited > message.createdIn))
  }

  @Transaction
  suspend fun insertSentMessage(oldId: Long, newId: Long, createdIn: Long): MessageEntity? {
    val message = popMessage(oldId) ?: return null
    val lastVisited = getPartnerLastVisited(message.chatId)
    val modified = message.copy(id = newId, createdIn = createdIn, isRead = lastVisited > message.createdIn)
    insertMessage(modified)
    return modified
  }

  @Transaction
  suspend fun insertAllMessages(chatId: Long, messages: List<ChatMessageDto>, myId: Long) {
    val lastVisited = getPartnerLastVisited(chatId)
    insertMessages(messages.map { it.toMessageEntity(myId, lastVisited) })
  }

  @Update(entity = ChatEntity::class)
  suspend fun insertDraft(update: DraftUpdate)

  @Query("update chats set draft_message = null where id = :chatId")
  suspend fun deleteDraft(chatId: Long)

  @Transaction
  suspend fun upsertChat(chat: ChatEntity) {
    val id = insertChat(chat)

    if (id == -1L)
      updateChat(chat.toChatUpdate())
  }

  @Query("select id from messages where id >= ${MessageEntity.LOCAL_MESSAGES_ID_OFFSET} order by id desc limit 1")
  suspend fun getLastLocalMessageId(): Long?

  @Query("select * from messages where id >= ${MessageEntity.LOCAL_MESSAGES_ID_OFFSET} order by createdIn asc")
  suspend fun getLocalMessages(): List<MessageEntity>

}
