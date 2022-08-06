package com.lofigroup.features.nearby_service.di

import android.content.Context
import com.lofigroup.domain.navigator.di.NavigatorComponent
import com.lofigroup.features.nearby_service.NearbyServiceImpl
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.FeatureScope
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@FeatureScope
@Component(dependencies = [NavigatorComponent::class])
interface NearbyServiceComponent {

  fun inject(nearbyServiceImpl: NearbyServiceImpl)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder

    @BindsInstance
    fun coroutineScope(scope: CoroutineScope): Builder

    fun navigatorComponent(component: NavigatorComponent): Builder

    fun build(): NearbyServiceComponent
  }

}