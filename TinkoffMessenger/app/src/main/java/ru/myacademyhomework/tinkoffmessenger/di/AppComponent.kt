package ru.myacademyhomework.tinkoffmessenger.di

import dagger.Component
import ru.myacademyhomework.tinkoffmessenger.chatFragment.ChatFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class, ChatModule::class])
interface AppComponent {

//    fun getChatApi(): ChatApi

    fun inject(chatFragment: ChatFragment)
}