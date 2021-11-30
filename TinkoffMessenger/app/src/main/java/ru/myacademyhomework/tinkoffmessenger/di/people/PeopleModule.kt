package ru.myacademyhomework.tinkoffmessenger.di.people

import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.peoplefragment.PeoplePresenter

@Module
class PeopleModule {

    @PeopleScope
    @Provides
    fun providePeoplePresenter(chatDao: ChatDao): PeoplePresenter{
        return PeoplePresenter(chatDao)
    }
}