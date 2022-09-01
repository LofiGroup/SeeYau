package com.lofigroup.seeyau.data.chat.local

import androidx.room.*
import com.lofigroup.seeyau.data.chat.local.models.ChatAssembled
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

  @Transaction
  @Query("SELECT * FROM chats")
  fun getChats(): Flow<List<ChatAssembled>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChat(chat: ChatEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessages(messages: List<MessageEntity>)

}