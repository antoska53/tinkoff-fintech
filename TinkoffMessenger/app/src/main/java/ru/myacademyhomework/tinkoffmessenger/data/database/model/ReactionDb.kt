package ru.myacademyhomework.tinkoffmessenger.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "reaction_table", primaryKeys = ["emoji_code", "user_id", "message_id"])
class ReactionDb (

    @ColumnInfo(name = "emoji_code")
    val emojiCode: String,

    @ColumnInfo(name = "emoji_name")
    val emojiName: String,

    @ColumnInfo(name = "reaction_type")
    val reactionType: String,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "message_id")
    val messageId: Long
)