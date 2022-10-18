package com.lofigroup.seeyau.data.chat.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.domain.chat.models.ChatDraft

@Entity(
  tableName = "drafts",
  foreignKeys = [
    ForeignKey(entity = ChatEntity::class, childColumns = ["chatId"], parentColumns = ["id"]),
  ]
)
data class MessageDraftEntity(
  @PrimaryKey @ColumnInfo(index = true) val chatId: Long,
  val message: String,
  val createdIn: Long
)

fun MessageDraftEntity.toChatDraft() = ChatDraft(
  message = message,
  createdIn = createdIn,
  chatId = chatId
)

fun ChatDraft.toMessageDraftEntity() = MessageDraftEntity(
  chatId = chatId,
  createdIn = System.currentTimeMillis(),
  message = message
)