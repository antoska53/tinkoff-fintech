package ru.myacademyhomework.tinkoffmessenger.di.people

import dagger.Binds
import dagger.Module
import ru.myacademyhomework.tinkoffmessenger.data.people.PeopleRepositoryImpl
import ru.myacademyhomework.tinkoffmessenger.domain.people.PeopleRepository

@Module
interface PeopleModule {
    @Binds
    fun bindPeopleRepository(repositoryImpl: PeopleRepositoryImpl): PeopleRepository
}