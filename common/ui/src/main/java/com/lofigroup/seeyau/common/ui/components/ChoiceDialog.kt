package com.lofigroup.seeyau.common.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lofigroup.seeyau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.theme.LocalSpacing
import com.lofigroup.seeyau.common.ui.R
import com.sillyapps.core.ui.theme.LocalExtendedColors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YesNoChoiceDialog(
  visible: Boolean,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  title: String,
  details: String = ""
) {
  ChoiceDialog(
    title = title,
    details = details,
    onDismiss = onDismiss,
    visible = visible
  ) {
    ChoiceTextButton(
      text = stringResource(id = R.string.yes),
      color = LocalExtendedColors.current.darkBackground,
      onClick = {
        onConfirm()
        onDismiss()
      }
    )
    ChoiceTextButton(
      text = stringResource(id = R.string.no),
      onClick = onDismiss
    )
  }
}

@Composable
fun ConfirmDialog(
  title: String,
  details: String,
  visible: Boolean,
  onDismiss: () -> Unit
) {
  ChoiceDialog(
    title = title,
    details = details,
    onDismiss = onDismiss,
    visible = visible
  ) {
    ChoiceTextButton(
      text = stringResource(id = R.string.ok),
      onClick = onDismiss
    )
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChoiceDialog(
  title: String,
  details: String,
  onDismiss: () -> Unit,
  visible: Boolean,
  content: @Composable RowScope.() -> Unit
) {
  if (visible) {
    Dialog(
      properties = DialogProperties(
        usePlatformDefaultWidth = false
      ),
      onDismissRequest = onDismiss
    ) {
      Surface(
        modifier = Modifier
          .clip(MaterialTheme.shapes.medium)
          .width(300.dp)
      ) {
        Column(
          modifier = Modifier
            .padding(horizontal = LocalSpacing.current.medium)
        ) {

          if (details.isNotBlank()) {
            Text(
              text = title,
              style = MaterialTheme.typography.h4,
              modifier = Modifier.padding(
                top = LocalSpacing.current.medium,
                bottom = LocalSpacing.current.small
              )
            )

            Text(
              text = details,
              style = MaterialTheme.typography.body1,
            )
          } else {
            Text(
              text = title,
              style = MaterialTheme.typography.h4,
              textAlign = TextAlign.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(
                  top = LocalSpacing.current.medium,
                  bottom = LocalSpacing.current.small
                )
            )
          }

          Spacer(modifier = Modifier.height(height = LocalSpacing.current.small))

          Row(
            modifier = Modifier.fillMaxWidth()
          ) {
            content()
          }
        }
      }
    }
  }
}

@Composable
fun RowScope.ChoiceTextButton(
  text: String,
  onClick: () -> Unit,
  color: Color = MaterialTheme.colors.onSurface
) {
  TextButton(
    onClick = onClick,
    modifier = Modifier.weight(1f)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.h4,
      color = color
    )
  }
}

@Preview()
@Composable
fun PreviewChoiceDialog() {
  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      YesNoChoiceDialog(
        visible = true,
        onDismiss = { },
        onConfirm = {},
        title = "Something is up!",
        details = "Something is up, what do you want to do?"
      )
    }
  }
}

@Preview()
@Composable
fun PreviewChoiceWithoutDetailsDialog() {
  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      YesNoChoiceDialog(
        visible = true,
        onDismiss = { },
        onConfirm = {},
        title = "Something is up!"
      )
    }
  }
}