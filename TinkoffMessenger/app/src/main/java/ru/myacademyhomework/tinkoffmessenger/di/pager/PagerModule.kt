package ru.myacademyhomework.tinkoffmessenger.di.pager

import dagger.Binds
import dagger.Module
import ru.myacademyhomework.tinkoffmessenger.data.pager.PagerRepositoryImpl
import ru.myacademyhomework.tinkoffmessenger.domain.pager.PagerRepository

@Module
interface PagerModule {
    @Binds
    fun bindPagerRepository(repositoryImpl: PagerRepositoryImpl): PagerRepository
}