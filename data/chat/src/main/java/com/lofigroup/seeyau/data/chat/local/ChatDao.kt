package com.lofigroup.seeyau.data.chat.local

import androidx.room.*
import com.lofigroup.data.navigator.local.model.UserEntity
import com.lofigroup.seeyau.data.chat.local.models.ChatAssembled
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import kotlinx.coroutines.flow.*

@Dao
interface ChatDao {

  @Transaction
  @Query("select * from chats")
  fun getChats(): Flow<List<ChatAssembled>>

  @Query("select * from chats where id = :id")
  fun getChat(id: Long): Flow<ChatEntity>

  @Query("select * from users where id = :id")
  fun getUser(id: Long): Flow<UserEntity>

  @Query("select * from messages where chatId = :chatId order by createdIn desc")
  fun getChatMessages(chatId: Long): Flow<List<MessageEntity>>

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

  fun getAssembledChat(id: Long): Flow<ChatAssembled> {
    return getChat(id).flatMapLatest { chat ->
      getUser(chat.partnerId).combine(getChatMessages(chat.id)) { user, messages ->
        ChatAssembled(
          chat = chat,
          partner = user,
          messages = messages
        )
      }
    }
  }

}