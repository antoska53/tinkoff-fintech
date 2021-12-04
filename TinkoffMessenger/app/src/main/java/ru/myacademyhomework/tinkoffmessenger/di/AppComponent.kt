package ru.myacademyhomework.tinkoffmessenger.di

import dagger.Component
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatComponent
import ru.myacademyhomework.tinkoffmessenger.di.chat.ChatModule
import ru.myacademyhomework.tinkoffmessenger.di.pager.PagerComponent
import ru.myacademyhomework.tinkoffmessenger.di.pager.PagerModule
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleComponent
import ru.myacademyhomework.tinkoffmessenger.di.people.PeopleModule
import ru.myacademyhomework.tinkoffmessenger.di.profile.ProfileComponent
import ru.myacademyhomework.tinkoffmessenger.di.profile.ProfileModule
import ru.myacademyhomework.tinkoffmessenger.di.stream.StreamComponent
import ru.myacademyhomework.tinkoffmessenger.di.stream.StreamModule
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface AppComponent {

    fun getChatComponent(chatModule: ChatModule): ChatComponent
    fun getPeopleComponent(peopleModule: PeopleModule): PeopleComponent
    fun getProfileComponent(profileModule: ProfileModule): ProfileComponent
    fun getStreamComponent(streamModule: StreamModule): StreamComponent
    fun getPagerComponent(pagerModule: PagerModule): PagerComponent
}