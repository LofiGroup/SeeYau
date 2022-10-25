package com.lofigroup.seayau.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChoiceDialog(
  visible: Boolean,
  onDismiss: () -> Unit,
  title: String,
  details: String,
  choices: @Composable ColumnScope.() -> Unit
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
            .padding(bottom = LocalSpacing.current.small)
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(top = LocalSpacing.current.medium, bottom = LocalSpacing.current.small)
          )
          Text(
            text = details,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = LocalSpacing.current.bigMedium)
          )
          choices()
        }
      }
    }
  }
}

@Composable
fun ChoiceDialogItem(
  text: String,
  onClick: () -> Unit,
  color: Color
) {
  Box(
    modifier = Modifier
      .padding(bottom = LocalSpacing.current.small)
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.medium)
      .background(color)
      .clickable(onClick = onClick),
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.h4,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(LocalSpacing.current.small)
        .align(Alignment.Center)
    )
  }
}

@Preview
@Composable
fun PreviewChoiceDialog() {
  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      ChoiceDialog(visible = true, onDismiss = {  }, title = "Something is up!", details = "Something is up, what do you want to do?") {
        ChoiceDialogItem(text = "Do nothing", onClick = {  }, color = LocalExtendedColors.current.disabled)
        ChoiceDialogItem(text = "Check what's up!", onClick = {  }, color = MaterialTheme.colors.error)
      }
    }
  }
}