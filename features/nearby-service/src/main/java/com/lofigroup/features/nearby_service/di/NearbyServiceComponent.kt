package com.lofigroup.features.nearby_service.di

import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(dependencies = [NavigatorComponent::class])
interface NearbyServiceComponent {

  fun inject(nearbyServiceImpl: NearbyServiceImpl)

  @Component.Builder
  interface Builder {
    fun navigatorComponent(component: NavigatorComponent): Builder

    fun build(): NearbyServiceComponent
  }

}