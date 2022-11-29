package com.lofigroup.seeyau.data.chat.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.lofigroup.seeyau.domain.chat.models.MessageType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@Entity(
  tableName = "messages",
  foreignKeys = [
    ForeignKey(
      onDelete = ForeignKey.CASCADE,
      entity = ChatEntity::class,
      childColumns = ["chatId"],
      parentColumns = ["id"]
    ),
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
  val isRead: Boolean,

  @ColumnInfo(defaultValue = "null")
  val extra: String?,
  @ColumnInfo(defaultValue = "PLAIN")
  val type: MessageTypeEntity,
) {
  companion object {

    /**
     * Id offset of messages which are not yet received by the backend.
     * It's used to distinguish between messages created locally and by the server.
     *   */
    const val LOCAL_MESSAGES_ID_OFFSET = 0x7_000_000_000_000_000
  }
}

@JsonClass(generateAdapter = true)
data class MediaExtra(
  val uri: String
) {
  companion object {
    val adapter: JsonAdapter<MediaExtra> = Moshi.Builder().build().adapter(MediaExtra::class.java)
  }
}

@JsonClass(generateAdapter = true)
data class VideoExtra(
  val uri: String,
  @Json(name = "thumbnail_uri")
  val thumbnailUri: String
) {
  companion object {
    val adapter: JsonAdapter<VideoExtra> = Moshi.Builder().build().adapter(VideoExtra::class.java)
  }
}

fun MessageEntity.toDomainModel(): ChatMessage {
  return ChatMessage(
    id = id,
    message = message,
    author = author,
    createdIn = createdIn,
    status = getStatus(),
    type = type.toMessageType(extra)
  )
}

fun MessageEntity.getStatus(): MessageStatus {
  return if (id >= MessageEntity.LOCAL_MESSAGES_ID_OFFSET) MessageStatus.SENDING
    else if (isRead) MessageStatus.READ
    else MessageStatus.SENT
}