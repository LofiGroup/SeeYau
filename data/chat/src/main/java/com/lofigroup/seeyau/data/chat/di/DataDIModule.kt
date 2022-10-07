package com.lofigroup.seeyau.data.chat.di

import com.lofigroup.seeyau.data.chat.local.EventsDataSource
import com.lofigroup.seeyau.data.chat.local.EventsDataSourceImpl
import com.sillyapps.core.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface DataDIModule {

  @AppScope
  @Binds
  fun bindEventDataSource(eventsDataSourceImpl: EventsDataSourceImpl): EventsDataSource

}