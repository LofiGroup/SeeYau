package com.lofigroup.seeyau.data.chat.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.domain.chat.models.ChatMessage

@Entity(
  tableName = "messages",
  foreignKeys = [
    ForeignKey(entity = ChatEntity::class, childColumns = ["chatId"], parentColumns = ["id"]),
    ForeignKey(entity = UserEntity::class, childColumns = ["author"], parentColumns = ["id"]),
  ]
)
data class MessageEntity(
  @PrimaryKey val id: Long,
  @ColumnInfo(index = true) val chatId: Long,
  val message: String,
  @ColumnInfo(index = true) val author: Long,
  val createdIn: Long,
  @ColumnInfo(defaultValue = "0")
  val isRead: Boolean
)

fun MessageEntity.toDomainModel(): ChatMessage {
  return ChatMessage(
    id = id,
    message = message,
    author = author,
    createdIn = createdIn,
    isRead = isRead
  )
}

