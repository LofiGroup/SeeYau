package com.lofigroup.seeyau.main_screen_event_channel

import android.content.Context
import android.content.Intent
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.MainScreenEventChannel
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent
import com.lofigroup.seeyau.ui.MainActivity
import javax.inject.Inject

class MainScreenEventChannelImpl @Inject constructor(
  private val context: Context
): MainScreenEventChannel {

  override fun getEventIntent(event: MainScreenEvent): Intent {
    return Intent(context, MainActivity::class.java).apply {
      putExtra(INTENT_EXTRA_ID, MainScreenEvent.serialize(event))
    }
  }

  companion object {
    const val INTENT_EXTRA_ID = "mainScreenEvent"
  }

}