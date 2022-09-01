package com.lofigroup.seeyau.data.chat.local

import android.content.SharedPreferences
import javax.inject.Inject

class LastChatUpdateDataSource @Inject constructor(
  private val sharedPref: SharedPreferences
) {

  private var mCache: Long? = null

  fun get(): Long {
    var cache = mCache
    if (cache == null) {
      cache = sharedPref.getLong(tag, 0L)
    }
    mCache = cache
    return cache
  }

  fun update(updateTime: Long = System.currentTimeMillis()) {
    mCache = updateTime
    sharedPref.edit().putLong(tag, updateTime).apply()
  }

  companion object {
    const val tag = "LAST_CHAT_UPDATE"
  }

}