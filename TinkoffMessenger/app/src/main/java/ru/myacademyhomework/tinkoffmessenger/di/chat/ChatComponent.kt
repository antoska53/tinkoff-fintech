package ru.myacademyhomework.tinkoffmessenger.di.chat

import dagger.Subcomponent
import ru.myacademyhomework.tinkoffmessenger.presentation.chatfragment.ChatFragment


@ChatScope
@Subcomponent(modules = [ChatModule::class])
interface ChatComponent {
    fun inject(chatFragment: ChatFragment)
}