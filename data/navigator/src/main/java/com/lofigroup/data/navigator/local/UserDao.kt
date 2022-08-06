package com.lofigroup.data.navigator.local

import androidx.room.*
import com.lofigroup.data.navigator.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

  @Query("select * from users")
  fun observeUsers(): Flow<List<UserEntity>>

  @Query("select * from users where endpointId = :endpointId")
  suspend fun getUserWithDeviceId(endpointId: String): UserEntity?

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(userEntity: UserEntity): Long

  @Update
  suspend fun update(userEntity: UserEntity)

  @Delete
  suspend fun delete(userEntity: UserEntity)

  @Transaction
  suspend fun upsert(userEntity: UserEntity) {
    val id = insert(userEntity)

    if (id == -1L) {
      update(userEntity)
    }
  }

}