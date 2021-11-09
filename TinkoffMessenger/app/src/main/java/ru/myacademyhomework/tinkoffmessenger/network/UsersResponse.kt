package ru.myacademyhomework.tinkoffmessenger.network


import kotlinx.serialization.*

@Serializable
data class UsersResponse(
    @SerialName("members")
    val users: List<User>
)

