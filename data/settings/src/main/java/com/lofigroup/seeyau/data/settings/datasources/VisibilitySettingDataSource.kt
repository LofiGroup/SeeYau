package com.lofigroup.seeyau.data.settings.datasources

import android.content.SharedPreferences
import com.lofigroup.seeyau.data.settings.model.VisibilityDataModel
import com.sillyapps.core.di.AppScope
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@AppScope
class VisibilitySettingDataSource @Inject constructor(
  private val sharedPref: SharedPreferences
) {

  private val adapter = Moshi.Builder().build().adapter(VisibilityDataModel::class.java)
  private val state = MutableStateFlow(loadFromSharedPref())


  private fun loadFromSharedPref(): VisibilityDataModel {
    val json = sharedPref.getString(VISIBILITY_SETTING, "")

    if (json.isNullOrBlank()) {
      return defaultState
    }

    return adapter.fromJson(json) ?: defaultState
  }

  fun get(): Flow<VisibilityDataModel> = state

  fun set(visibility: VisibilityDataModel) {
    val json = adapter.toJson(visibility) ?: return

    sharedPref.edit().putString(VISIBILITY_SETTING, json).apply()
    state.value = visibility
  }

  companion object {
    const val VISIBILITY_SETTING = "VISIBILITY_SETTING"
    val defaultState = VisibilityDataModel(isVisible = true)
  }

}