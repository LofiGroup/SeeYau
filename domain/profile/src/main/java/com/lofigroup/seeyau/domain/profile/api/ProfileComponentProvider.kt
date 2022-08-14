package com.lofigroup.seeyau.domain.profile.api

import com.lofigroup.seeyau.domain.profile.di.ProfileComponent

interface ProfileComponentProvider {

  fun provideProfileComponent(): ProfileComponent

}