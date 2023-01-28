package com.lofigroup.features.nearby_service.search_mode.model

sealed interface SearchMode {
  object Normal: SearchMode
  object BatterySave: SearchMode
}