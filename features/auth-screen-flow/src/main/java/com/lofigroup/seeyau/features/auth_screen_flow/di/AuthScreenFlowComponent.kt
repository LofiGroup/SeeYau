package com.lofigroup.seeyau.features.auth_screen_flow.di

import com.lofigroup.seeyau.domain.auth.di.AuthComponent
import com.lofigroup.seeyau.domain.profile.di.ProfileComponent
import com.lofigroup.seeyau.features.auth_screen_flow.ui.AuthScreenFlowViewModel
import com.sillyapps.core.di.ScreenScope
import dagger.Component

@ScreenScope
@Component(dependencies = [AuthComponent::class, ProfileComponent::class])
interface AuthScreenFlowComponent {

  fun getViewModel(): AuthScreenFlowViewModel

  interface Builder {
    fun authComponent(authComponent: AuthComponent): Builder

    fun profileComponent(profileComponent: ProfileComponent): Builder

    fun build(): AuthScreenFlowComponent
  }

}