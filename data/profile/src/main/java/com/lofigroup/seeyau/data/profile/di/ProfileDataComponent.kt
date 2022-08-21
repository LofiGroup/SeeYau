package com.lofigroup.seeyau.data.profile.di

import com.lofigroup.backend_api.SeeYauApi
import com.lofigroup.seeyau.domain.profile.ProfileRepository
import com.sillyapps.core.di.AppScope
import com.sillyapps.core.di.IOModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

@AppScope
@Component(modules = [ApiModule::class, RepositoryModule::class, IOModule::class])
interface ProfileDataComponent {

  fun getRepository(): ProfileRepository

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun baseRetrofit(retrofit: Retrofit): Builder

    @BindsInstance
    fun appScope(appScope: CoroutineScope): Builder

    fun build(): ProfileDataComponent
  }

}