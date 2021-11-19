package ru.myacademyhomework.tinkoffmessenger.network


import kotlinx.serialization.*

@Serializable
data class MessageResponse (
    @SerialName("anchor")
    val anchor: Long,

    @SerialName("found_anchor")
    val foundAnchor: Boolean,

    @SerialName("found_newest")
    val foundNewest: Boolean,

    @SerialName("messages")
    val messages: List<UserMessage>,

    @SerialName("msg")
    val msg: String,

    @SerialName("result")
    val result: String
)

@Serializable
data class UserMessage (
    @SerialName("avatar_url")
    val avatarURL: String,

    @SerialName("client")
    val client: String,

    @SerialName("content")
    val content: String,

    @SerialName("content_type")
    val contentType: String,

    @SerialName("id")
    val id: Int,

    @SerialName("is_me_message")
    val isMeMessage: Boolean,

    @SerialName("reactions")
    val reactions: List<Reaction>,

    @SerialName("sender_full_name")
    val senderFullName: String,

    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("type")
    val type: String,

    @SerialName("stream_id")
    val streamID: Long?
)

