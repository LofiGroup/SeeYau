package com.lofigroup.seeyau.data.profile.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lofigroup.seeyau.data.profile.local.model.BlacklistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlacklistDao {

  @Query("select * from blacklist where byWho = :userId")
  fun observeIsBlacklistedBy(userId: Long): Flow<BlacklistEntity?>

  @Query("select createdIn from blacklist order by createdIn desc limit 1")
  suspend fun getBlacklistLastCreatedIn(): Long?

  @Query("select exists(select id from blacklist where toWhom = :userId)")
  suspend fun userIsInBlackList(userId: Long): Boolean

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(blacklist: List<BlacklistEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(blacklist: BlacklistEntity)

  @Delete
  suspend fun delete(blacklist: List<BlacklistEntity>)

  @Delete
  suspend fun delete(blacklist: BlacklistEntity)

}