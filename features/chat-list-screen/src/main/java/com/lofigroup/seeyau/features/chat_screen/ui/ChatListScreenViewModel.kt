package com.lofigroup.seeyau.features.chat_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lofigroup.core.util.indexOfFirstFrom
import com.lofigroup.core.util.set
import com.lofigroup.seeyau.domain.chat.models.ChatBrief
import com.lofigroup.seeyau.domain.chat.usecases.ObserveChatsUseCase
import com.lofigroup.seeyau.domain.profile.usecases.BlacklistUserUseCase
import com.lofigroup.seeyau.domain.profile.usecases.GetProfileUseCase
import com.lofigroup.seeyau.domain.profile.usecases.LikeUserUseCase
import com.lofigroup.seeyau.domain.profile.usecases.UnLikeUserUseCase
import com.lofigroup.seeyau.domain.settings.usecases.GetVisibilityUseCase
import com.lofigroup.seeyau.features.chat_screen.model.*
import com.sillyapps.core_time.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListScreenViewModel @Inject constructor(
  private val observeChatsUseCase: ObserveChatsUseCase,
  private val getProfileUseCase: GetProfileUseCase,
  private val blacklistUserUseCase: BlacklistUserUseCase,
  private val likeUserUseCase: LikeUserUseCase,
  private val unLikeUserUseCase: UnLikeUserUseCase,
  private val getVisibilityUseCase: GetVisibilityUseCase
): ViewModel(), ChatListScreenStateHolder {

  private val state = MutableStateFlow(ChatListScreenState())

  init {
    viewModelScope.launch {
      launch {
        observeChatList()
      }
      launch {
        observeProfile()
      }
      launch {
        observeVisibility()
      }
    }
  }

  private suspend fun observeChatList() {
    observeChatsUseCase().collect() { chats ->
      val orderedByLastMessage = chats.sortedByDescending { it.lastMessage?.createdIn ?: it.createdIn }
      val orderedByLastContact = chats.sortedByDescending { it.partner.lastContact }

      val metNotLongAgoIndex = orderedByLastContact.indexOfFirstFrom(0) { System.currentTimeMillis() - it.partner.lastContact > Time.m }
      val metTodayIndex = orderedByLastContact.indexOfFirstFrom(metNotLongAgoIndex) { System.currentTimeMillis() - it.partner.lastContact > Time.d }

      val currentItem = orderedByLastMessage.firstOrNull() { it.id == state.value.currentItem?.id }

      state.set { oldState ->
        oldState.copy(
          nearbyFolder = orderedByLastContact.subList(0, metNotLongAgoIndex),
          metFolder = orderedByLastContact.subList(metNotLongAgoIndex, metTodayIndex),
          interactionFolder = orderedByLastMessage,
          newMessagesCount = orderedByLastMessage.sumOf { it.newMessagesCount },
          currentItem = currentItem
        )
      }
    }
  }

  private suspend fun observeProfile() {
    getProfileUseCase().collect() { profile ->
      state.set { it.copy(profile = profile) }
    }
  }

  private suspend fun observeVisibility() {
    getVisibilityUseCase().collect() { visibility ->
      state.set { it.copy(isVisible = visibility.isVisible) }
    }
  }

  override fun getState(): Flow<ChatListScreenState> = state

  override fun blacklistUser(userId: Long) {
    if (userId == 0L) return
    viewModelScope.launch { blacklistUserUseCase(userId) }
  }

  override fun setCurrentChat(chatBrief: ChatBrief?) {
    state.set { it.copy(currentItem = chatBrief) }
  }

  override fun likeUser(isLiked: Boolean, userId: Long) {
    viewModelScope.launch {
      if (isLiked) likeUserUseCase(userId)
      else unLikeUserUseCase(userId)
    }
  }

}