package ru.myacademyhomework.tinkoffmessenger.di.stream

import dagger.Binds
import dagger.Module
import ru.myacademyhomework.tinkoffmessenger.data.stream.StreamRepositoryImpl
import ru.myacademyhomework.tinkoffmessenger.domain.stream.StreamRepository

@Module
interface StreamModule {
    @Binds
    fun bindStreamRepository(repositoryImpl: StreamRepositoryImpl): StreamRepository
}