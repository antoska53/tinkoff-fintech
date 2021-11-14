package ru.myacademyhomework.tinkoffmessenger.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_table")
class TopicDb (
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
    @PrimaryKey
    @ColumnInfo(name = "name_topic")
    val nameTopic: String,

    @ColumnInfo(name = "name_stream")
    var nameStream: String,

    @ColumnInfo(name = "stream_id")
    var streamId: Long
)