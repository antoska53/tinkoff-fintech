package ru.myacademyhomework.tinkoffmessenger.data.network.model

import kotlinx.serialization.*

@Serializable
data class AllStreamsResponse (
    val msg: String,
    val result: String,
    val streams: List<StreamDto>
)

@Serializable
data class StreamDto (

    @SerialName("description")
    val description: String,

    @SerialName("invite_only")
    val inviteOnly: Boolean,

    @SerialName("name")
    val name: String,

    @SerialName("stream_id")
    val streamID: Long
)