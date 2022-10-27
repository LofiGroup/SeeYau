package com.lofigroup.backend_api

import android.content.SharedPreferences
import com.lofigroup.backend_api.models.TokenDataModel
import com.lofigroup.core.util.ResourceState
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class TokenStoreImpl @Inject constructor(
  private val sharedPref: SharedPreferences
): TokenStore {
  private val TOKEN_TAG = "ijsjkjkds"

  private val adapter = Moshi.Builder().build().adapter(TokenDataModel::class.java)
  private val cachedValue = MutableStateFlow(TokenDataModel(""))

  private fun getTokenFromSharedPref(): TokenDataModel? {
    val json = sharedPref.getString(TOKEN_TAG, null)

    if (json.isNullOrBlank()) {
      return null
    }

    return try {
      adapter.fromJson(json)
    } catch (e: Exception) {
      Timber.e(e)
      null
    }
  }

  override fun getTokenState(): Flow<ResourceState> = cachedValue.map {
    if (it.token.isNotBlank()) ResourceState.IS_READY
    else ResourceState.LOADING
  }

  override fun saveToken(token: TokenDataModel) {
    val json = adapter.toJson(token)

    sharedPref.edit().putString(TOKEN_TAG, json).apply()
    cachedValue.value = token
  }

  override fun getToken(): TokenDataModel? {
    if (cachedValue.value.token.isNotBlank())
      return cachedValue.value

    val token = getTokenFromSharedPref()
    if (token != null) {
      cachedValue.value = token
    }
    return token
  }

  override fun forgetToken() {
    cachedValue.value = TokenDataModel("")
    sharedPref.edit().remove(TOKEN_TAG).apply()
  }

  override fun isEmpty(): Boolean {
    return cachedValue.value.token.isBlank()
  }

}