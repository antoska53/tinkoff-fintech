package ru.myacademyhomework.tinkoffmessenger.network


import kotlinx.serialization.*

@Serializable
data class UserResponse(
    @SerialName("user")
    val user: User
)

