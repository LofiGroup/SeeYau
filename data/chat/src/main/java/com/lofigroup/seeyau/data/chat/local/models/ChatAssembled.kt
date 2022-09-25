package com.lofigroup.seeyau.data.chat.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.lofigroup.seeyau.data.profile.local.model.toDomainModel
import com.lofigroup.seeyau.domain.chat.models.Chat
import com.lofigroup.seeyau.domain.chat.models.ChatMessage

data class ChatAssembled(
  @Embedded val chat: ChatEntity,

  @Relation(parentColumn = "partnerId", entityColumn = "id")
  val partner: UserEntity,

  @Relation(parentColumn = "id", entityColumn = "chatId")
  val messages: List<MessageEntity>
)

data class ChatDetailed(
  val chat: ChatEntity,
  val partner: UserEntity,
  val messages: List<MessageEntity>
)

fun ChatAssembled.toDomainModel(): Chat {
  return Chat(
    id = chat.id,
    partner = partner.toDomainModel(),
    messages = messages.map { it.toDomainModel(chat.partnerLastVisited) }
  )
}

