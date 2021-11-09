package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.*

@Serializable
data class User(
    @SerialName("avatar_url")
    val avatarURL: String,

    @SerialName("email")
    val email: String,

    @SerialName("full_name")
    val fullName: String,

    @SerialName("user_id")
    val userID: Int
)


