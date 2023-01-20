package com.lofigroup.seeyau.common.ui.main_screen_event_channel

import android.content.Intent
import com.lofigroup.seeyau.common.ui.main_screen_event_channel.model.MainScreenEvent

interface MainScreenEventChannel {

  fun getEventIntent(event: MainScreenEvent): Intent

}