package com.lofigroup.seeyau.data.profile.local

import androidx.room.*
import com.lofigroup.seeyau.data.profile.local.model.*
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

  @Query("select * from users where id > 0 limit 1")
  suspend fun getSomeone(): UserEntity?

  @Query("select * from users where id in (:userIds)")
  suspend fun getUsersIn(userIds: List<Long>): List<UserEntity>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(entities: List<UserEntity>): List<Long>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(userEntity: UserEntity): Long

  @Update(entity = UserEntity::class)
  suspend fun update(entities: List<UpdateUserExcludingImage>)

  @Update(entity = UserEntity::class)
  suspend fun update(entity: UpdateUserExcludingImage)

  @Update(entity = UserEntity::class)
  suspend fun updateImageUrl(update: UpdateUserImage)

  @Query("delete from users where id = :userId")
  suspend fun delete(userId: Long)

  @Query("delete from users where id in (:idList)")
  suspend fun deleteMultiple(idList: List<Long>)

  @Query("update users set lastConnection = 0, lastContact = 0 where id = :userId")
  suspend fun resetUser(userId: Long)

  @Transaction
  suspend fun upsert(users: List<UserEntity>) {
    val ids = insert(users)

    val usersToUpdate = users.filterIndexed { index, userEntity -> ids[index] == -1L }.map { it.toUpdateUserExcludingImage() }
    update(usersToUpdate)
  }

  // return true if user exists
  @Transaction
  suspend fun upsert(user: UserEntity): Boolean {
    val id = insert(user)

    if (id == -1L) {
      update(user.toUpdateUserExcludingImage())
      return true
    }

    return false
  }

}