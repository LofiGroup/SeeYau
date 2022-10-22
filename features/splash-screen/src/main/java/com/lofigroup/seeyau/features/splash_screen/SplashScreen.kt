package com.lofigroup.seeyau.features.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.splash_screen.model.SplashScreenState
import com.sillyapps.core.ui.components.ShowToast
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class)
@Composable
fun SplashScreen(
  stateHolder: SplashScreenStateHolder,
  isLoggedIn: (Boolean) -> Unit
) {

  val state by remember(stateHolder) {
    stateHolder.getState()
  }.collectAsState(initial = SplashScreenState())

  if (state.isReady) {
    LaunchedEffect(state) {
      isLoggedIn(state.isLoggedIn)
    }
  }

  Surface {
    Box(
      modifier = Modifier
        .fillMaxSize()
    ) {

      Image(
        painter = painterResource(id = R.drawable.ic_intro_logo),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.Center)
          .size(64.dp)
      )

      Text(
        text = stringResource(id = R.string.plum),
        style = MaterialTheme.typography.h1.copy(
          fontFamily = FontFamily(Font(R.font.swis721blkbtrusbyme_black)),
          brush = LocalExtendedColors.current.primaryGradient
        ),
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .navigationBarsPadding()
          .padding(bottom = LocalSpacing.current.extraLarge)
      )

    }
  }

  val errorMessage = state.errorMessage
  if (errorMessage != null) {
    ShowToast(message = errorMessage)
  }

}

@Preview
@Composable
fun PreviewSplashScreen() {
  val scope = rememberCoroutineScope()

  val stateHolder = object : SplashScreenStateHolder {
    private val state = MutableStateFlow(SplashScreenState())

    init {
      scope.launch {
        delay(2500L)
        state.value = state.value.copy(isLoggedIn = false, isReady = true)
      }
    }

    override fun getState(): Flow<SplashScreenState> {
      return state
    }
  }

  AppTheme {
    SplashScreen(
      stateHolder = stateHolder,
      isLoggedIn = {}
    )
  }
}