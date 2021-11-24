package ru.myacademyhomework.tinkoffmessenger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class UserDb(

    @ColumnInfo(name = "avatar_url")
    val avatarURL: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userID: Int,

    @ColumnInfo(name = "is_own")
    val isOwn: Boolean
)