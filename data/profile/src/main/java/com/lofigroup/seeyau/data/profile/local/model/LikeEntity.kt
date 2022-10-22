package com.lofigroup.seeyau.data.profile.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lofigroup.seeyau.data.profile.remote.http.model.LikeDto
import com.lofigroup.seeyau.domain.profile.model.Like

@Entity(
  tableName = "likes",
  foreignKeys = [
    ForeignKey(onDelete = ForeignKey.CASCADE, entity = UserEntity::class, childColumns = ["byWho"], parentColumns = ["id"]),
    ForeignKey(onDelete = ForeignKey.CASCADE, entity = UserEntity::class, childColumns = ["toWhom"], parentColumns = ["id"]),
  ]
)
data class LikeEntity(
  @PrimaryKey val id: Long,
  @ColumnInfo(index = true) val byWho: Long,
  @ColumnInfo(index = true) val toWhom: Long,
  val updatedIn: Long,
  val isLiked: Boolean,
)

fun LikeDto.toLikeEntity(myId: Long) = run {
  var byWho = byWho
  var toWhom = toWhom

  if (byWho == myId) byWho = 0
  else toWhom = 0

  LikeEntity(
    id = id,
    byWho = byWho,
    toWhom = toWhom,
    updatedIn = updatedIn,
    isLiked = isLiked
  )
}

fun LikeEntity.toDomainModel() = Like(
  id = id,
  userId = byWho,
  createdIn = updatedIn
)