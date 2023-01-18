package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
class MessageDb(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "avatar_url")
    val avatarURL: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "is_me_message")
    val isMeMessage: Boolean,

    @ColumnInfo(name = "sender_full_name")
    val senderFullName: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "stream_id")
    val streamID: Long?,

    @ColumnInfo(name = "name_topic")
    val nameTopic: String,

    @ColumnInfo(name = "name_stream")
    val nameStream: String
)