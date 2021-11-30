package ru.myacademyhomework.tinkoffmessenger.di.profile

import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.profilefragment.ProfilePresenter

@Module
class ProfileModule {

    @ProfileScope
    @Provides
    fun provideProfilePresenter(chatDao: ChatDao): ProfilePresenter {
        return ProfilePresenter(chatDao)
    }
}