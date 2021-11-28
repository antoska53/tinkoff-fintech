package ru.myacademyhomework.tinkoffmessenger.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.AppDelegate
import ru.myacademyhomework.tinkoffmessenger.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import javax.inject.Singleton

@Module
class StorageModule(private val appDelegate: AppDelegate) {

    @Singleton
    @Provides
    fun provideApp(): AppDelegate {
        return appDelegate
    }

    @Singleton
    @Provides
    fun provideChatDao(): ChatDao {
        return Room.databaseBuilder(
            appDelegate.applicationContext,
            ChatDatabase::class.java,
            "chat_database"
        )
            .build()
            .chatDao()
    }
}