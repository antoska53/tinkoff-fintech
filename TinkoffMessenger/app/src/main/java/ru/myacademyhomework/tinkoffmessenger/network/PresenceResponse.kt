package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.*

@Serializable
data class PresenceResponse(
    @SerialName("presence")
    val userStatus: UserStatus
)

@Serializable
data class UserStatus(
    val status: String,
    val timestamp: Long
)
