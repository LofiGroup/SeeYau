package com.lofigroup.data.navigator

import com.lofigroup.domain.navigator.model.NearbyUser
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.chat.local.models.toDomainModel
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import com.sillyapps.core_time.Time

fun UserEntity.toNearbyUser(newMessages: List<MessageEntity>) = NearbyUser(
  id = id,
  name = name,
  imageUrl = imageUrl,
  lastConnection = lastConnection,
  isNear = System.currentTimeMillis() - lastConnection < 1 * Time.m,
  newMessages = newMessages.map { it.toDomainModel(System.currentTimeMillis()) }
)