package com.lofigroup.seeyau.features.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.lofigroup.seeyau.features.chat.R
import com.sillyapps.core.ui.theme.LocalSpacing

@Composable
fun MessageInput(
  message: String,
  setMessage: (String) -> Unit,
  sendMessage: () -> Unit
) {
  Row(
    modifier = Modifier.padding(LocalSpacing.current.medium),
    verticalAlignment = Alignment.CenterVertically
  ) {

    TextField(
      value = message,
      onValueChange = setMessage,
      modifier = Modifier.weight(1f),
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text
      ),
      keyboardActions = KeyboardActions(
        onNext = {
          sendMessage()
        }
      ),
      colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
      ),
      placeholder = {
        Text(text = stringResource(id = R.string.message))
      },
      leadingIcon = {
        Image(
          painter = painterResource(id = R.drawable.ic_scab_icon),
          contentDescription = null
        )
      },
      trailingIcon = {
        Icon(
          imageVector = Icons.Filled.Send,
          contentDescription = null,
          modifier = Modifier.clickable {
            sendMessage()
          }
        )
      },
    )

  }
}