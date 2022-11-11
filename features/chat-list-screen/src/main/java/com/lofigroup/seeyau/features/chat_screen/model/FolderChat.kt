package com.lofigroup.seeyau.features.chat_screen.model

import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.models.ChatMessage
import com.lofigroup.seeyau.domain.profile.model.User

sealed class FolderChat(
  val id: Long,
  val partner: User
) {

  class LikeChat(
    id: Long, partner: User,
    val likedAt: Long
  ) : FolderChat(id, partner)

  class MemoryChat(
    id: Long, partner: User,
    val message: String,
    val createdIn: Long
  ) : FolderChat(id, partner)

  class DefaultChat(
    id: Long,
    partner: User,
    val lastMessage: ChatMessage?,
    val newMessagesCount: Int,
    val createdIn: Long
  ) : FolderChat(id, partner)

}

fun ChatBrief.toLikeChat() = FolderChat.LikeChat(
  id = id,
  partner = partner,
  likedAt = partner.likedYouAt ?: 0L
)

fun ChatBrief.toMemoryChat() = FolderChat.MemoryChat(
  id = id,
  partner = partner,
  message = draft.message,
  createdIn = draft.createdIn
)

fun ChatBrief.toDefaultChat() = FolderChat.DefaultChat(
  id = id,
  partner = partner,
  lastMessage = lastMessage,
  newMessagesCount = newMessagesCount,
  createdIn = createdIn
)
