package com.sillyapps.core.ui.util

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.lastVisibleItemKey(): Any? {
  return layoutInfo.visibleItemsInfo.lastOrNull()?.key
}