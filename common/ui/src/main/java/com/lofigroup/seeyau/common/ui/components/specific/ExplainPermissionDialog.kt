package com.lofigroup.seeyau.common.ui.components.specific

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lofigroup.seeyau.common.ui.components.ConfirmDialog
import com.lofigroup.seeyau.common.ui.permissions.model.PermissionRationale

@Composable
fun ExplainPermissionDialog(
  rationale: PermissionRationale?,
  onDismiss: () -> Unit
) {
  if (rationale != null) {
    ConfirmDialog(
      visible = true,
      onDismiss = onDismiss,
      title = stringResource(id = rationale.titleResId),
      details = stringResource(id = rationale.descriptionResId)
    )
  }
}