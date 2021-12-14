package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reaction_table", primaryKeys = ["emoji_code", "user_id"])
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