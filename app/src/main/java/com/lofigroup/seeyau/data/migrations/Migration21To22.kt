package com.lofigroup.seeyau.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lofigroup.seeyau.data.chat.local.models.MediaExtra
import com.lofigroup.seeyau.data.chat.local.models.MessageTypeEntity
import com.lofigroup.seeyau.data.chat.local.models.VideoExtra

val migration21To22 = object : Migration(21, 22) {
  override fun migrate(database: SupportSQLiteDatabase) {
    val cursor = database.query("select * from messages")
    while (cursor.moveToNext()) {
      val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
      val extra: String? = cursor.getString(cursor.getColumnIndexOrThrow("extra"))
      val type = MessageTypeEntity.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("type")))

      val newExtra = when (type) {
        MessageTypeEntity.PLAIN, MessageTypeEntity.CONTACT -> {
          null
        }
        MessageTypeEntity.VIDEO -> {
          if (extra == null) null
          else
            VideoExtra.adapter.toJson(VideoExtra(extra, ""))
        }
        else -> {
          if (extra == null) null
          else
            MediaExtra.adapter.toJson(MediaExtra(extra))
        }
      }

      database.execSQL("update messages set extra = ? where id = ?", arrayOf(newExtra, id))
    }
  }
}