package com.lofigroup.data.navigator.local

import android.content.SharedPreferences
import com.lofigroup.data.navigator.local.model.ProfileDataModel
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlin.random.Random

class ProfileDataSourceImpl @Inject constructor(
  private val sharedPref: SharedPreferences
): ProfileDataSource {

  private var cache: ProfileDataModel? = null

  private val adapter = Moshi.Builder().build().adapter(ProfileDataModel::class.java)

  override fun getProfile(): ProfileDataModel {
    if (cache != null) return cache!!

    return loadProfile()
  }

  private fun loadProfile(): ProfileDataModel {
    val json = sharedPref.getString(ID, "") ?: ""

    if (json.isBlank()) return generateNewProfile()

    return adapter.fromJson(json) ?: generateNewProfile()
  }

  private fun generateNewProfile(): ProfileDataModel {
    val id = Random.nextLong()

    val profile = ProfileDataModel(id = id, name = "No name")
    val json = adapter.toJson(profile)

    sharedPref.edit().putString(ID, json).apply()
    return profile
  }

  companion object {
    private const val ID = "USER_PROFILE"
  }
}