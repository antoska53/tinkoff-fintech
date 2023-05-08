package ru.myacademyhomework.tinkoffmessenger.data.network.model


import kotlinx.serialization.*
import ru.myacademyhomework.tinkoffmessenger.data.ChatMessage

@Serializable
data class MessageResponse (
    @SerialName("anchor")
    val anchor: Long,

    @SerialName("found_anchor")
    val foundAnchor: Boolean,

    @SerialName("found_newest")
    val foundNewest: Boolean,

    @SerialName("found_oldest")
    val foundOldest: Boolean,

    @SerialName("messages")
    val messages: List<UserMessageDto>,

    @SerialName("msg")
    val msg: String,

    @SerialName("result")
    val result: String
)

@Serializable
data class UserMessageDto (
    @SerialName("avatar_url")
    val avatarURL: String,

    @SerialName("content")
    val content: String,

    @SerialName("id")
    val id: Long,

    @SerialName("is_me_message")
    val isMeMessage: Boolean,

    @SerialName("reactions")
    val reactions: List<ReactionDto>,

    @SerialName("sender_full_name")
    val senderFullName: String,

    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("stream_id")
    val streamID: Long?,

    @Transient
    var nameTopic: String = ""
)

