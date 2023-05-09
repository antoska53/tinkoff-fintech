package ru.myacademyhomework.tinkoffmessenger.di.chat

import dagger.Binds
import dagger.Module
import ru.myacademyhomework.tinkoffmessenger.data.chat.ChatRepositoryImpl
import ru.myacademyhomework.tinkoffmessenger.domain.chat.ChatRepository

@Module
interface ChatModule {
    @Binds
    fun bindChatRepository(repositoryImpl: ChatRepositoryImpl): ChatRepository
}