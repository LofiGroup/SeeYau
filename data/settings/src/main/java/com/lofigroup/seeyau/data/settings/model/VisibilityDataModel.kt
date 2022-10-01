package com.lofigroup.seeyau.data.settings.model

import com.lofigroup.seeyau.domain.settings.model.Visibility
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VisibilityDataModel(
  val isVisible: Boolean
)

fun VisibilityDataModel.toDomainModel() =
  Visibility(
    isVisible = isVisible
  )

fun Visibility.toDataModel() =
  VisibilityDataModel(
    isVisible = isVisible
  )