package com.lofigroup.seeyau.data.auth.model

import com.lofigroup.backend_api.models.AccessRequest
import com.lofigroup.seeyau.domain.auth.model.Access

fun Access.toAccessRequest(): AccessRequest {
  return AccessRequest(
    email = email,
    password = password
  )
}