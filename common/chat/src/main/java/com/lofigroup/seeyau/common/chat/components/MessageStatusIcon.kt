package com.lofigroup.seeyau.common.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lofigroup.seeyau.common.chat.R
import com.lofigroup.seeyau.domain.chat.models.MessageStatus
import com.sillyapps.core.ui.theme.LocalSize

@Composable
fun MessageStatusIcon(
  messageStatus: MessageStatus
) {
  val resId = when (messageStatus) {
    MessageStatus.SENDING -> R.drawable.ic_baseline_history_24
    MessageStatus.SENT -> R.drawable.ic_check_mark_sent
    MessageStatus.READ -> R.drawable.ic_check_mark_read
    MessageStatus.RECEIVED -> R.drawable.ic_check_mark_received
  }

  Icon(
    painter = painterResource(id = resId),
    contentDescription = null,
    modifier = Modifier.size(LocalSize.current.verySmall)
  )
}