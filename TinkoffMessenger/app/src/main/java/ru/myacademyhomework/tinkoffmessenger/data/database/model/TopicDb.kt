package ru.myacademyhomework.tinkoffmessenger.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "topic_table",  primaryKeys = ["name_topic", "stream_id"])
class TopicDb (

    @ColumnInfo(name = "name_topic")
    val nameTopic: String,

    @ColumnInfo(name = "name_stream")
    var nameStream: String,

    @ColumnInfo(name = "stream_id")
    var streamId: Long
)