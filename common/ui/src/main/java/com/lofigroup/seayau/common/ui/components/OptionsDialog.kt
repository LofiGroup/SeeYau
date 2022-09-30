package com.lofigroup.seayau.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seayau.common.ui.theme.*
import com.sillyapps.core.ui.theme.LocalExtendedColors
import com.sillyapps.core.ui.theme.LocalSpacing
import com.sillyapps.core.ui.theme.applyActivityBarPaddings

@Composable
fun OptionsDialog(
  visible: Boolean,
  onDismiss: () -> Unit,
  caption: String = "",
  options: @Composable ColumnScope.() -> Unit,
) {
  if (visible) {
    Dialog(
      onDismissRequest = onDismiss
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.applyActivityBarPaddings()
      ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = caption,
          style = MaterialTheme.typography.subtitle2,
          modifier = Modifier.padding(vertical = LocalSpacing.current.medium)
        )
        Column(
          modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(LocalExtendedColors.current.lightBackground)
        ) {
          options()
        }
        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(
            backgroundColor = LocalExtendedColors.current.darkBackground,
            contentColor = MaterialTheme.colors.onBackground
          ),
          elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = LocalSpacing.current.medium)
        ) {
          Text(
            text = stringResource(id = R.string.cancel),
            style = MaterialTheme.typography.h2,

          )
        }
      }
    }
  }
}

@Composable
fun OptionsDialogItem(
  text: String,
  textColor: Color = MaterialTheme.colors.onBackground
) {
  Text(
    text = text,
    color = textColor,
    textAlign = TextAlign.Center,
    style = MaterialTheme.typography.h2,
    modifier = Modifier
      .fillMaxWidth()
      .padding(LocalSpacing.current.medium)
  )
}

@Preview
@Composable
fun PreviewOptionsDialog() {
  AppTheme() {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      OptionsDialog(
        onDismiss = {},
        visible = true,
        caption = "What do you want to do?"
      ) {
        OptionsDialogItem(
          text = "Write message",
          textColor = MaterialTheme.colors.secondary
        )
        OptionsDialogItem(
          text = "Don't show this user again",
          textColor = LocalExtendedColors.current.disabled
        )
      }
    }
  }
}