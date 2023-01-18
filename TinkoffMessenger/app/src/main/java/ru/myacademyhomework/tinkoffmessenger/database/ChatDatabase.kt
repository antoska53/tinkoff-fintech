package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StreamDb::class, TopicDb::class, MessageDb::class, ReactionDb::class, UserDb::class], version = 1
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

}