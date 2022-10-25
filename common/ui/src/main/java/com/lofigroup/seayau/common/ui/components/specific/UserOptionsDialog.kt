package com.lofigroup.seayau.common.ui.components.specific

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seayau.common.ui.components.ChoiceDialog
import com.lofigroup.seayau.common.ui.components.ChoiceDialogItem
import com.lofigroup.seayau.common.ui.components.OptionsDialog
import com.lofigroup.seayau.common.ui.components.OptionsDialogItem
import com.sillyapps.core.ui.theme.LocalExtendedColors

@Composable
fun UserOptionsDialog(
  isVisible: Boolean,
  setVisible: (Boolean) -> Unit,
  onWriteMessage: () -> Unit,
  onIgnoreUser: () -> Unit,
  onNavigateToChatOptionEnabled: Boolean = true
) {
  var confirmationDialogVisible by remember {
    mutableStateOf(false)
  }

  OptionsDialog(
    visible = isVisible,
    onDismiss = { setVisible(false) },
    caption = stringResource(id = R.string.what_do_you_want_to_do)
  ) {
    if (onNavigateToChatOptionEnabled)
      OptionsDialogItem(
        text = stringResource(id = R.string.write_message),
        onClick = onWriteMessage
      )
    OptionsDialogItem(
      text = stringResource(id = R.string.ignore_user),
      onClick = { confirmationDialogVisible = true }
    )
  }

  ChoiceDialog(
    visible = confirmationDialogVisible,
    onDismiss = { confirmationDialogVisible = false },
    title = stringResource(id = R.string.are_you_sure),
    details = stringResource(id = R.string.ignore_user_detail)
  ) {
    ChoiceDialogItem(
      text = stringResource(id = R.string.cancel),
      onClick = { confirmationDialogVisible = false },
      color = LocalExtendedColors.current.disabled
    )
    ChoiceDialogItem(
      text = stringResource(id = R.string.confirm),
      onClick = {
        confirmationDialogVisible = false
        onIgnoreUser()
      },
      color = MaterialTheme.colors.error
    )
  }
}