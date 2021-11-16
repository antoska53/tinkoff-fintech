package ru.myacademyhomework.tinkoffmessenger.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.myacademyhomework.tinkoffmessenger.network.Reaction

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

//    @ColumnInfo(name = "reaction")
//    val reactions: List<Reaction>,

    @ColumnInfo(name = "sender_full_name")
    val senderFullName: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "stream_id")
    val streamID: Long?,

    @ColumnInfo(name = "name_topic")
    val nameTopic: String
)