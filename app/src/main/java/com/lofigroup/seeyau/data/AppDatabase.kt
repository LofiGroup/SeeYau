package com.lofigroup.seeyau.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.lofigroup.backend_api.data.DatabaseHandler
import com.lofigroup.core.util.toIntArray
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.migrations.Migration21To22
import com.lofigroup.seeyau.data.migrations.Migration22To23
import com.lofigroup.seeyau.data.profile.local.BlacklistDao
import com.lofigroup.seeyau.data.profile.local.LikeDao
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.profile.local.model.BlacklistEntity
import com.lofigroup.seeyau.data.profile.local.model.LikeEntity
import com.lofigroup.seeyau.data.profile.local.model.UserEntity

@Database(
  entities = [
    UserEntity::class, MessageEntity::class, ChatEntity::class, LikeEntity::class, BlacklistEntity::class
  ],
  version = 23,
  exportSchema = true,
  autoMigrations = [
//    AutoMigration(from = 20, to = 21, spec = AppDatabase.Migration20To21::class)
  ]
)
abstract class AppDatabase : RoomDatabase(), DatabaseHandler {

  abstract val userDao: UserDao
  abstract val chatDao: ChatDao
  abstract val likeDao: LikeDao
  abstract val blacklistDao: BlacklistDao

//  @RenameColumn(tableName = "messages", fromColumnName = "mediaUri", toColumnName = "extra")
//  class Migration20To21 : AutoMigrationSpec

  override fun clearTables() {
    clearAllTables()
  }

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
            "seeyau_database"
          )
//            .addMigrations(Migration21To22, Migration22To23)
            .fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigrationFrom(*(1..23).toIntArray())
            .build()

          INSTANCE = instance
        }
        return instance
      }
    }
  }

}