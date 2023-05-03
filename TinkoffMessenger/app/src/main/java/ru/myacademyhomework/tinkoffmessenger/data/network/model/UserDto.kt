package ru.myacademyhomework.tinkoffmessenger.data.network.model

import kotlinx.serialization.*

@Serializable
data class UserDto(

    @SerialName("avatar_url")
    val avatarURL: String,

    @SerialName("email")
    val email: String,

    @SerialName("full_name")
    val fullName: String,

    @SerialName("user_id")
    val userID: Int
)


