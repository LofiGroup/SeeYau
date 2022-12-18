package com.lofigroup.core.audio_recorder.permission

import android.Manifest
import com.lofigroup.core.permission.model.AppPermission

object AudioRecorderPermission: AppPermission(
  permissions = arrayOf(Manifest.permission.RECORD_AUDIO),
  rationale = null
)