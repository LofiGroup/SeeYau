package com.lofigroup.seeyau.data.chat.local

import androidx.room.*
import com.lofigroup.seeyau.data.chat.local.models.ChatAssembled
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import kotlinx.coroutines.flow.*

@Dao
interface ChatDao {

  @Query("select * from chats")
  fun getChats(): Flow<List<ChatEntity>>

  @Query("select * from chats where id = :id")
  fun getChat(id: Long): Flow<ChatEntity>

  @Query("select * from chats where partnerId = :userId")
  fun getUserChat(userId: Long): Flow<ChatEntity>

  @Query("select * from messages where chatId = :chatId order by createdIn desc")
  fun getChatMessages(chatId: Long): Flow<List<MessageEntity>>

  @Query("select chats.lastVisited as lastVisited, messages.id as id, messages.chatId as chatId, messages.createdIn as createdIn, messages.author as author, messages.message as message " +
      "from chats, messages where author = :userId and createdIn > lastVisited order by createdIn desc")
  fun getNewUserMessages(userId: Long): Flow<List<MessageEntity>>

  @Query("select * from messages where chatId = :chatId order by createdIn desc limit 1")
  fun observeLastMessage(chatId: Long): Flow<MessageEntity?>

  @Query("select * from messages order by createdIn desc limit 1")
  suspend fun getLastMessage(): MessageEntity?

  @Query("update chats set lastVisited = :lastVisited where id = :chatId")
  suspend fun updateChatLastVisited(chatId: Long, lastVisited: Long)

  @Query("update chats set partnerLastVisited = :lastVisited where id = :chatId")
  suspend fun updateChatPartnerLastVisited(chatId: Long, lastVisited: Long)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChat(chat: ChatEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessages(messages: List<MessageEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: MessageEntity)

}