package ru.myacademyhomework.tinkoffmessenger.di.stream

import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.streamfragment.StreamPresenter


@Module
class StreamModule {

    @StreamScope
    @Provides
    fun provideStreamPresenter(chatDao: ChatDao): StreamPresenter{
        return StreamPresenter(chatDao)
    }
}