package com.lofigroup.seeyau.features.chat.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayer
import com.lofigroup.seeyau.features.chat.media_player.MediaPlayerImpl
import com.sillyapps.core.di.ScreenScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
object Media3Module {

  @ScreenScope
  @Provides
  fun providePlayer(context: Context): Player = ExoPlayer.Builder(context).build()

}

@Module
interface MediaPlayerModule {
  @ScreenScope
  @Binds
  fun bindMediaPlayer(mediaPlayerImpl: MediaPlayerImpl): MediaPlayer
}