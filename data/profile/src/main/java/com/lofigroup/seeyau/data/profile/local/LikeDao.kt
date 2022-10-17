package com.lofigroup.seeyau.data.profile.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {

  @Query("select * from likes")
  fun observeAllLikes(): Flow<List<LikeEntity>>

  @Query("select * from likes where byWho = :userId and isLiked = 1")
  fun observeUserLike(userId: Long): Flow<LikeEntity?>

  @Query("select isLiked from likes where toWhom = :userId")
  fun observeLikedState(userId: Long): Flow<Boolean?>

  @Query("select updatedIn from likes order by updatedIn desc limit 1")
  suspend fun getLastLikeUpdatedIn(): Long?

  @Query("update likes set isLiked = 0 where byWho = 0 and toWhom = :userId")
  suspend fun unlike(userId: Long)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLikes(likes: List<LikeEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(like: LikeEntity)

}