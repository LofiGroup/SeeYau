package com.lofigroup.seeyau.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lofigroup.seayau.common.ui.theme.AppTheme
import com.lofigroup.seeyau.App

class MainActivity: ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val app = (application as App)

    setContent {
      AppTheme() {
        RootContainer(app.navigatorComponent)
      }
    }
  }

}