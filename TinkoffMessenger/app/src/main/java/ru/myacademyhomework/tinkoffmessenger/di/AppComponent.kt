package ru.myacademyhomework.tinkoffmessenger.di

import dagger.Component
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatComponent
import ru.myacademyhomework.tinkoffmessenger.di.pager.PagerComponent
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleComponent
import ru.myacademyhomework.tinkoffmessenger.di.profile.ProfileComponent
import ru.myacademyhomework.tinkoffmessenger.di.stream.StreamComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface AppComponent {

    fun getChatComponent(): ChatComponent
    fun getPeopleComponent(): PeopleComponent
    fun getProfileComponent(): ProfileComponent
    fun getStreamComponent(): StreamComponent
    fun getPagerComponent(): PagerComponent
}