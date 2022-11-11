package com.lofigroup.seayau.common.ui.permissions.model

sealed class AppPermission(
  val permissions: Array<String>,
  val rationale: PermissionRationale? = null
)