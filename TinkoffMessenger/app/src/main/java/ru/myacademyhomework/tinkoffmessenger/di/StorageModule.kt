package ru.myacademyhomework.tinkoffmessenger.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDao
import ru.myacademyhomework.tinkoffmessenger.data.database.ChatDatabase
import javax.inject.Singleton

@Module
class StorageModule(private val appContext: Context) {

    @Singleton
    @Provides
    fun provideChatDao(): ChatDao {
        return Room.databaseBuilder(
            appContext,
            ChatDatabase::class.java,
            "chat_database"
        )
            .build()
            .chatDao()
    }
}