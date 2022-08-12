package com.lofigroup.seeyau.data.auth

import android.content.SharedPreferences
import com.lofigroup.seeyau.data.auth.model.TokenDataModel
import com.lofigroup.seeyau.domain.auth.model.Token
import com.squareup.moshi.Moshi
import javax.inject.Inject

class TokenStoreImpl @Inject constructor(
  private val sharedPref: SharedPreferences
): TokenStore {
  private val TOKEN_TAG = "ijsjkjkds"

  private val adapter = Moshi.Builder().build().adapter(TokenDataModel::class.java)
  private var cachedValue: TokenDataModel? = null

  private fun getTokenFromSharedPref(): TokenDataModel? {
    val json = sharedPref.getString(TOKEN_TAG, null)

    if (json.isNullOrBlank()) {
      return null
    }
    return adapter.fromJson(json)
  }

  override fun saveToken(token: TokenDataModel) {
    val json = adapter.toJson(token)

    sharedPref.edit().putString(TOKEN_TAG, json).apply()
    cachedValue = token
  }

  override fun getToken(): TokenDataModel? {
    if (cachedValue != null)
      return cachedValue

    return getTokenFromSharedPref()
  }

  override fun forgetToken() {
    cachedValue = null
    sharedPref.edit().remove(TOKEN_TAG).apply()
  }

}