package com.lofigroup.seeyau.data.chat.local.models

import androidx.room.ColumnInfo
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
      onDelete = ForeignKey.NO_ACTION
    )]
)
data class ChatEntity(
  @PrimaryKey val id: Long,
  @ColumnInfo(index = true) val partnerId: Long,
  val lastVisited: Long,
  val partnerLastVisited: Long
)