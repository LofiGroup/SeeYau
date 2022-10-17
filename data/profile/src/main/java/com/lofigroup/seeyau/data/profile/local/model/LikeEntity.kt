package com.lofigroup.seeyau.data.profile.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lofigroup.seeyau.data.profile.remote.http.model.LikeDto

@Entity(
  tableName = "likes",
  foreignKeys = [
    ForeignKey(entity = UserEntity::class, childColumns = ["byWho"], parentColumns = ["id"]),
    ForeignKey(entity = UserEntity::class, childColumns = ["toWhom"], parentColumns = ["id"]),
  ]
)
data class LikeEntity(
  @PrimaryKey val id: Long,
  @ColumnInfo(index = true) val byWho: Long,
  @ColumnInfo(index = true) val toWhom: Long,
  val updatedIn: Long,
  val isLiked: Boolean,
)

fun LikeDto.toLikeEntity(myId: Long) =
  LikeEntity(
    id = id,
    byWho = if (myId == byWho) 0 else byWho,
    toWhom = if (myId == toWhom) 0 else toWhom,
    updatedIn = updatedIn,
    isLiked = isLiked
  )