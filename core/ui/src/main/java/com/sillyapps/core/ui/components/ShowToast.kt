package com.sillyapps.core.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToast(
  message: String,
  duration: Int = Toast.LENGTH_LONG
) {
  val context = LocalContext.current

  Toast.makeText(context, message, duration).show()
}

fun showToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}