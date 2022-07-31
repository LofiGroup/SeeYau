package com.lofigroup.features.navigator_screen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.features.navigator_screen.model.NavigatorScreenState
import com.lofigroup.features.navigator_screen.model.UserItemUIModel
import com.lofigroup.features.navigator_screen.ui.components.UserItem
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.components.ShowToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun NavigatorScreen(
  stateHolder: NavigatorScreenStateHolder
) {
  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = NavigatorScreenState(isLoading = true))

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    if (state.isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
      )
    }
    else {
      LazyColumn() {
        items(
          items = state.users
        ) { user ->
          UserItem(user = user)
        }
      }
    }
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }
}

@Preview
@Composable
fun NavigatorScreenPreview() {
  val stateHolder = object : NavigatorScreenStateHolder {
    override fun getState(): Flow<NavigatorScreenState> {
      return flow {
        emit(
          NavigatorScreenState(
          users = listOf(
            UserItemUIModel(
              imageUrl = "",
              isNear = true,
              name = "Random",
              status = "Hello world!"
            ),
            UserItemUIModel(
              imageUrl = "",
              isNear = true,
              name = "Random",
              status = "Hello world!"
            ),
            UserItemUIModel(
              imageUrl = "",
              isNear = true,
              name = "Random",
              status = "Hello world!"
            )
          )
        )
        )
      }
    }
  }

  AppTheme {
    NavigatorScreen(
      stateHolder = stateHolder
    )
  }
}