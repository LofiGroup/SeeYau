package com.lofigroup.seeyau.data.profile.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blacklist")
data class BlacklistEntity(
  @PrimaryKey val id: Long,
  val byWho: Long,
  val toWhom: Long,
  val createdIn: Long
)
