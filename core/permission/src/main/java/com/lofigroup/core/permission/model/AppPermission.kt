package com.lofigroup.core.permission.model

abstract class AppPermission(
  val permissions: Array<String>,
  val rationale: PermissionRationale? = null
)