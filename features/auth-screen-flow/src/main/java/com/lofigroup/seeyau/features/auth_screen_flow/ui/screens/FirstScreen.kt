package com.lofigroup.seeyau.features.auth_screen_flow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.lofigroup.seeyau.common.ui.components.ButtonWithText
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.features.auth_screen_flow.R
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R as CommonR


@Composable
fun FirstScreen(
  onNextButtonClick: () -> Unit,
) {
  val annotatedText = buildAnnotatedString {
    withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
      append(stringResource(id = R.string.first_screen_text_part_1))
    }
    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
      append(stringResource(id = R.string.first_screen_text_part_2))
    }
    withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
      append(stringResource(id = R.string.first_screen_text_part_3))
    }
  }

  Column() {
    Column(
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = LocalSpacing.current.large)
    ) {
      Text(
        text = stringResource(id = R.string.first_screen_title),
        style = MaterialTheme.typography.h1
      )

      Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

      Text(
        text = annotatedText,
      )
    }

    ButtonWithText(
      text = stringResource(id = CommonR.string.next),
      onClick = onNextButtonClick,
      modifier = Modifier
        .padding(bottom = LocalSpacing.current.large)
        .padding(horizontal = LocalSpacing.current.medium)
        .navigationBarsPadding()
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