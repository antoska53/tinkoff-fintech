package ru.myacademyhomework.tinkoffmessenger.di.profile

import dagger.Binds
import dagger.Module
import ru.myacademyhomework.tinkoffmessenger.data.profile.ProfileRepositoryImpl
import ru.myacademyhomework.tinkoffmessenger.domain.profile.ProfileRepository

@Module
interface ProfileModule {
    @Binds
    fun bindProfileRepository(repositoryImpl: ProfileRepositoryImpl): ProfileRepository
}