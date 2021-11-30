package ru.myacademyhomework.tinkoffmessenger.di.pager

import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.streamfragment.pagerfragments.PagerPresenter

@Module
class PagerModule {

    @PagerScope
    @Provides
    fun providePagerPresenter(chatDao: ChatDao): PagerPresenter{
        return PagerPresenter(chatDao)
    }
}