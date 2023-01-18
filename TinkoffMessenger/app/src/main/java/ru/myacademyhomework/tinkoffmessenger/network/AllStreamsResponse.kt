package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.*

@Serializable
data class AllStreamsResponse (
    val msg: String,
    val result: String,
    val streams: List<StreamResponse>
)

@Serializable
data class StreamResponse (

    @SerialName("description")
    val description: String,

    @SerialName("invite_only")
    val inviteOnly: Boolean,

    @SerialName("name")
    val name: String,

    @SerialName("stream_id")
    val streamID: Long
)