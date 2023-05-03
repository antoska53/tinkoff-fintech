package ru.myacademyhomework.tinkoffmessenger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.myacademyhomework.tinkoffmessenger.data.database.model.MessageDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.ReactionDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.StreamDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.TopicDb
import ru.myacademyhomework.tinkoffmessenger.data.database.model.UserDb

@Database(
    entities = [StreamDb::class, TopicDb::class, MessageDb::class, ReactionDb::class, UserDb::class], version = 1
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

}