package com.lofigroup.seeyau.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration22To23: Migration(22, 23) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("alter table users rename column imageUrl to imageContentUri")
    database.execSQL("alter table users add column imageRemoteUrl text")

    val cursor = database.query("select * from users")
    while (cursor.moveToNext()) {
      val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
      val imageUri: String? = cursor.getString(cursor.getColumnIndexOrThrow("imageContentUri"))

      database.execSQL("update users set imageContentUri = ?, imageRemoteUrl = ? where id = ?", arrayOf(imageUri, null, id))
    }
  }
}