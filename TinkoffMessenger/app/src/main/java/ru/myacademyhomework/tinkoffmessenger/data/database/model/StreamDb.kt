package ru.myacademyhomework.tinkoffmessenger.data.database.model

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

    @ColumnInfo(name = "subscribed_status")
    var subscribedStatus: Boolean
)