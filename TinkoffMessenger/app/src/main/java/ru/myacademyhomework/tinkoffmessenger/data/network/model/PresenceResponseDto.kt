package ru.myacademyhomework.tinkoffmessenger.data.network.model

import kotlinx.serialization.*

@Serializable
data class PresenceResponseDto(
    @SerialName("presence")
    val presence: PresenceDto
)

@Serializable
data class PresenceDto (
    @SerialName("aggregated")
    val userStatus: UserStatusDto

)

@Serializable
data class UserStatusDto(
    @SerialName("status")
    val status: String,

    @SerialName("timestamp")
    val timestamp: Long
)
