package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream_table")
class StreamDb(

    @PrimaryKey
    @ColumnInfo(name = "streamId")
    val streamId: Long,

    @ColumnInfo(name = "nameStream")
    val nameChannel: String,
)