package ru.myacademyhomework.tinkoffmessenger.data.network.model


import kotlinx.serialization.*

@Serializable
data class UsersResponse(
    @SerialName("members")
    val users: List<UserDto>
)

