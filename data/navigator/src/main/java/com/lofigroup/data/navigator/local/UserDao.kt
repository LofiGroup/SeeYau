package com.lofigroup.data.navigator.local

import androidx.room.*
import com.lofigroup.data.navigator.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

  @Query("select * from users where id > 0")
  fun observeUsers(): Flow<List<UserEntity>>

  @Query("select * from users where id = 0")
  fun observeMe(): Flow<UserEntity>

  @Query("select * from users where id = :id")
  suspend fun getUser(id: Long): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entities: List<UserEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(userEntity: UserEntity): Long

  @Delete
  suspend fun delete(userEntity: UserEntity)

}