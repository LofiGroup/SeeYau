package com.lofigroup.seeyau.features.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.profile_screen.model.ProfileScreenState
import com.sillyapps.core.ui.components.ShowToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ProfileScreen(
  stateHolder: ProfileScreenStateHolder,
  onExit: () -> Unit
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = ProfileScreenState())

  if (state.navigateOut) {
    LaunchedEffect(state) {
      onExit()
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .padding(top = 16.dp)
          .fillMaxWidth(0.4f)
          .aspectRatio(1f)
          .background(color = MaterialTheme.colors.error, shape = CircleShape)
      ) {
        Icon(
          imageVector = Icons.Filled.Person,
          contentDescription = null,
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
        )
      }

      TextField(
        value = state.name,
        onValueChange = { stateHolder.setName(it) },
        label = { Text(text = "Name") },
        enabled = !state.isLoading,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 16.dp)
      )

      if (state.isLoading) {
        CircularProgressIndicator(
          modifier = Modifier.padding(16.dp)
        )
      }
    }

    FloatingActionButton(
      onClick = { stateHolder.saveProfile() },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      Icon(imageVector = Icons.Filled.Check, contentDescription = null)
    }
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }

}

@Preview
@Composable
fun ProfileScreenPreview() {
  val state = MutableStateFlow(ProfileScreenState())

  val stateHolder = object : ProfileScreenStateHolder {
    override fun getState(): Flow<ProfileScreenState> {
      return state
    }

    override fun setName(name: String) {
      state.value = state.value.copy(name = name)
    }

    override fun saveProfile() {

    }

  }

  AppTheme() {
    Surface() {
      ProfileScreen(
        stateHolder = stateHolder,
        onExit = {}
      )
    }
  }

}