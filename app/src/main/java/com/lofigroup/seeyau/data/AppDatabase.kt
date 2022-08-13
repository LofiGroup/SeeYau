package com.lofigroup.seeyau.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

  abstract val userDao: UserDao

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