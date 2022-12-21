package com.sillyapps.core.ui.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf

@Composable
fun LazyListState.rememberLastItemKey(): State<Any?> {
  return derivedStateOf { layoutInfo.visibleItemsInfo.lastOrNull()?.key }
}