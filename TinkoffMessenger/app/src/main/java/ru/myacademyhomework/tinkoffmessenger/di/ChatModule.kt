package ru.myacademyhomework.tinkoffmessenger.di

import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatPresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.network.ChatApi
import javax.inject.Singleton

@Module
class ChatModule {

    @Singleton
    @Provides
    fun provideChatPresenter(chatDao: ChatDao, chatApi: ChatApi): ChatPresenter {
        return ChatPresenter(chatDao, chatApi)
    }
}