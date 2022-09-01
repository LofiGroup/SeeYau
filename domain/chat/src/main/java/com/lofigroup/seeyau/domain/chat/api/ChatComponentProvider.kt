package com.lofigroup.seeyau.domain.chat.api

import com.lofigroup.seeyau.domain.chat.di.ChatComponent

interface ChatComponentProvider {
  fun provideChatComponent(): ChatComponent
}