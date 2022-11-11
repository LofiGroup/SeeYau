package com.lofigroup.seeyau.data.chat.local.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lofigroup.seeyau.data.profile.local.model.UserEntity

@Entity(
  tableName = "chats",
  foreignKeys = [
    ForeignKey(
      entity = UserEntity::class,
      parentColumns = arrayOf("id"),
      childColumns = arrayOf("partnerId"),
      onDelete = ForeignKey.CASCADE
    )]
)
data class ChatEntity(
  @PrimaryKey val id: Long,
  @ColumnInfo(index = true) val partnerId: Long,
  val createdIn: Long,
  val lastVisited: Long,
  val partnerLastVisited: Long,
  @Embedded(prefix = "draft_")
  val draft: Draft = Draft(message = "", createdIn = 0L)
)

data class ChatUpdate(
  val id: Long,
  val lastVisited: Long,
  val partnerLastVisited: Long
)

data class DraftUpdate(
  val id: Long,
  @Embedded(prefix = "draft_")
  val draft: Draft
)

fun ChatEntity.toChatUpdate() = ChatUpdate(
  id = id,
  lastVisited = lastVisited,
  partnerLastVisited = partnerLastVisited
)