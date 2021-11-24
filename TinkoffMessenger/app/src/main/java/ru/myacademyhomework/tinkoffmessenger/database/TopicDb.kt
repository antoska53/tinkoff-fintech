package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_table")
class TopicDb (

    @PrimaryKey
    @ColumnInfo(name = "name_topic")
    val nameTopic: String,

    @ColumnInfo(name = "name_stream")
    var nameStream: String,

    @ColumnInfo(name = "stream_id")
    var streamId: Long
)