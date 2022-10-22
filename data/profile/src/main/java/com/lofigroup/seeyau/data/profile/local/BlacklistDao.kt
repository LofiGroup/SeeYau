package com.lofigroup.seeyau.data.profile.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lofigroup.seeyau.data.profile.local.model.BlacklistEntity

@Dao
interface BlacklistDao {

  @Query("select createdIn from blacklist order by createdIn desc limit 1")
  suspend fun getBlacklistLastCreatedIn(): Long?

  @Query("select id from blacklist where byWho = 0 and toWhom = :userId")
  suspend fun userIsInBlackList(userId: Long): Long?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(blacklist: List<BlacklistEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(blacklist: BlacklistEntity)

}