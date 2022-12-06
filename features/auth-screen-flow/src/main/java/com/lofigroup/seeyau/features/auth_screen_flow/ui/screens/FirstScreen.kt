package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lofigroup.seeyau.common.ui.components.ButtonWithText
import com.lofigroup.seeyau.common.ui.components.FullScreenImage
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R as CommonR


@Composable
fun FirstScreen(
  onNextButtonClick: () -> Unit,
) {
  ConstraintLayout(
    modifier = Modifier.fillMaxSize()
  ) {
    val (text, plumIcon, button) = createRefs()
    
    FullScreenImage(painter = painterResource(id = R.drawable.welcome_screen))

    Image(
      painter = painterResource(id = R.drawable.plum_frame),
      contentDescription = null,
      modifier = Modifier
        .padding(end = LocalSpacing.current.large)
        .constrainAs(plumIcon) {
          bottom.linkTo(text.top, margin = 16.dp)
          end.linkTo(parent.end)
        }
    )

    Column(
      modifier = Modifier
        .padding(horizontal = LocalSpacing.current.large)
        .constrainAs(text) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
        }
    ) {
      Text(
        text = stringResource(id = R.string.first_screen_title),
        style = MaterialTheme.typography.h5
      )

      Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

      Text(
        text = stringResource(id = R.string.first_screen_text),
      )
    }
    ButtonWithText(
      text = stringResource(id = CommonR.string.next),
      onClick = onNextButtonClick,
      modifier = Modifier
        .padding(horizontal = LocalSpacing.current.medium, vertical = LocalSpacing.current.large)
        .navigationBarsPadding()
        .constrainAs(button) {
          bottom.linkTo(parent.bottom)
        }
    )
  }
}

@Preview
@Composable
fun FirstScreenPreview() {
  AppTheme {
    Surface {
      FirstScreen(onNextButtonClick = {})
    }
  }
}