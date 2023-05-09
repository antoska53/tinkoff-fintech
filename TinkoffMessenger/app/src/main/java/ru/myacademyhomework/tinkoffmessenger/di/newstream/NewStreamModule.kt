package ru.myacademyhomework.tinkoffmessenger.di.newstream

import dagger.Binds
import dagger.Module
import ru.myacademyhomework.tinkoffmessenger.data.newstream.NewStreamRepositoryImpl
import ru.myacademyhomework.tinkoffmessenger.domain.newstream.NewStreamRepository

@Module
interface NewStreamModule {
    @Binds
    fun bindNewStreamRepository(repositoryImpl: NewStreamRepositoryImpl): NewStreamRepository
}