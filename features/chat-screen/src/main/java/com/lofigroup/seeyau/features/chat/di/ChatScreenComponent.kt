package com.lofigroup.seeyau.features.chat.di

import android.content.Context
import android.content.res.Resources
import com.lofigroup.core.audio_recorder.di.AudioRecorderModule
import com.lofigroup.core.permission.PermissionRequestChannel
import com.lofigroup.seeyau.domain.chat.di.ChatComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.chat.ui.ChatScreenViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.BindsInstance
import dagger.Component

@ScreenScope
@Component(
  dependencies = [ChatComponent::class, ProfileComponent::class],
  modules = [Media3Module::class, MediaPlayerModule::class, AudioRecorderModule::class]
)
interface ChatScreenComponent {
  fun getViewModel(): ChatScreenViewModel

  @Component.Builder
  interface Builder {
    fun chatComponent(chatComponent: ChatComponent): Builder
    fun profileComponent(profileComponent: ProfileComponent): Builder

    @BindsInstance
    fun resources(resources: Resources): Builder

    @BindsInstance
    fun permissionChannel(permissionRequestChannel: PermissionRequestChannel): Builder

    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun chatId(id: Long): Builder

    fun build(): ChatScreenComponent
  }
}