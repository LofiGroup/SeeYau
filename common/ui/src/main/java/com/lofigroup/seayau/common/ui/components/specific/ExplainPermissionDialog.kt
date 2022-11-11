package com.lofigroup.seayau.common.ui.components.specific

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lofigroup.seayau.common.ui.R
import com.lofigroup.seayau.common.ui.components.ChoiceDialog
import com.lofigroup.seayau.common.ui.components.ChoiceDialogItem
import com.lofigroup.seayau.common.ui.permissions.model.PermissionRationale

@Composable
fun ExplainPermissionDialog(
  rationale: PermissionRationale?,
  onDismiss: () -> Unit
) {
  if (rationale != null) {
    ChoiceDialog(
      visible = true,
      onDismiss = onDismiss,
      title = stringResource(id = rationale.titleResId),
      details = stringResource(id = rationale.descriptionResId)
    ) {
      ChoiceDialogItem(text = stringResource(id = R.string.ok), onClick = onDismiss, color = MaterialTheme.colors.primary)
    }
  }
}