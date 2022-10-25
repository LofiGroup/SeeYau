package com.lofigroup.seeyau.data.profile.local

import androidx.room.*
import com.lofigroup.seeyau.data.profile.local.model.UserAssembled
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
  companion object {
    const val selectAssembledUserQuery =
      "select users.*, likes.updatedIn as likedYouAt, myLikes.updatedIn as likedAt from users " +
          "left join likes on likes.byWho = users.id and likes.isLiked = 1 " +
          "left join likes as myLikes on myLikes.toWhom = users.id and myLikes.isLiked = 1"
  }

  @Transaction
  @Query("$selectAssembledUserQuery where users.id > 0")
  fun observeAssembledUsers(): Flow<List<UserAssembled>>

  @Transaction
  @Query("$selectAssembledUserQuery where users.id = :userId")
  fun observeAssembledUser(userId: Long): Flow<UserAssembled>

  @Query("select * from users where id > 0")
  fun observeUsers(): Flow<List<UserEntity>>

  @Query("select * from users where id = :id")
  fun observeUser(id: Long): Flow<UserEntity>

  @Query("select * from users where id = 0")
  fun observeMe(): Flow<UserEntity>

  @Query("select * from users where id = :id")
  suspend fun getUser(id: Long): UserEntity?

  @Query("select lastContact from users where id = :id")
  suspend fun getLastContact(id: Long): Long?

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<UserEntity>): List<Long>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(userEntity: UserEntity): Long

  @Update
  suspend fun update(entities: List<UserEntity>)

  @Update
  suspend fun update(entity: UserEntity)

  @Query("delete from users where id = :userId")
  suspend fun delete(userId: Long)

  @Query("delete from users where id in (:idList)")
  suspend fun deleteMultiple(idList: List<Long>)

  @Query("update users set lastConnection = 0, lastContact = 0 where id = :userId")
  suspend fun resetUser(userId: Long)

  @Transaction
  suspend fun upsert(users: List<UserEntity>) {
    val ids = insert(users)

    val usersToUpdate = users.filterIndexed { index, userEntity -> ids[index] == -1L }
    update(usersToUpdate)
  }

  @Transaction
  suspend fun upsert(user: UserEntity) {
    val id = insert(user)

    if (id == -1L)
      update(user)
  }

}