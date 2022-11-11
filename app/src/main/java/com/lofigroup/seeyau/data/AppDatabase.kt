package com.lofigroup.seeyau.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.BlacklistEntity
import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import timber.log.Timber

@Database(
  entities = [
    UserEntity::class, MessageEntity::class, ChatEntity::class, LikeEntity::class, BlacklistEntity::class
  ],
  version = 19,
  exportSchema = true,
  autoMigrations = [
  ]
)
abstract class AppDatabase : RoomDatabase() {

  abstract val userDao: UserDao
  abstract val chatDao: ChatDao
  abstract val likeDao: LikeDao
  abstract val blacklistDao: BlacklistDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      synchronized(this) {
        var instance = INSTANCE

        if (instance == null) {
          instance = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "seayau_database"
          )
            .fallbackToDestructiveMigration()
            .build()

          INSTANCE = instance
        }
        return instance
      }
    }
  }

}