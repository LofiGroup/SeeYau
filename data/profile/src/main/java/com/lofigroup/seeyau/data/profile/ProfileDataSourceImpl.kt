package com.lofigroup.seeyau.data.profile

import android.content.SharedPreferences
import com.lofigroup.core.util.Resource
import com.lofigroup.core.util.Result
import com.lofigroup.seeyau.data.profile.model.ProfileDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
  private val sharedPreferences: SharedPreferences
): ProfileDataSource {

  private var cache: Long? = null

  override fun getMyId(): Long {
    if (cache != null) return cache!!

    val id = sharedPreferences.getLong(MY_ID_TAG, 0L)
    cache = id
    return id
  }

  override fun update(id: Long) {
    cache = id
    sharedPreferences.edit().putLong(MY_ID_TAG, id).apply()
  }

  override fun isNotEmpty(): Boolean {
    return true
  }

  companion object {
    const val MY_ID_TAG = "MY_ID_TAG"
  }
}