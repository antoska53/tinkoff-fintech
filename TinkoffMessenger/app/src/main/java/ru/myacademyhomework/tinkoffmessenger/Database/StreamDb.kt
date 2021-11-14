package ru.myacademyhomework.tinkoffmessenger.Database

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

//    @ColumnInfo(name = "topics")
//    val topics: List<TopicDb>
)