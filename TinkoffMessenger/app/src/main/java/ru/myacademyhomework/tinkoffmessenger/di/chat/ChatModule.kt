package ru.myacademyhomework.tinkoffmessenger.di.chat

import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatPresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.network.ChatApi

@Module
class ChatModule {

    @ChatScope
    @Provides
    fun provideChatPresenter(chatDao: ChatDao, chatApi: ChatApi): ChatPresenter {
        return ChatPresenter(chatDao, chatApi)
    }
}