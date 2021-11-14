package ru.myacademyhomework.tinkoffmessenger.Database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_table")
class UserDb (
    @ColumnInfo(name = "avatar_url")
    val avatarURL: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "user_id")
    val userID: Long,

    @ColumnInfo(name = "own_user")
    val ownUser: Boolean = false
)