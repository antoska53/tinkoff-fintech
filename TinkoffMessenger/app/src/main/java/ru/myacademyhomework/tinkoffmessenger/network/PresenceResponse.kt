package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.*

@Serializable
data class PresenceResponse(
    @SerialName("presence")
    val presence: Presence
)

@Serializable
data class Presence (
    @SerialName("aggregated")
    val userStatus: UserStatus

)

@Serializable
data class UserStatus(
    @SerialName("status")
    val status: String,

    @SerialName("timestamp")
    val timestamp: Long
)
