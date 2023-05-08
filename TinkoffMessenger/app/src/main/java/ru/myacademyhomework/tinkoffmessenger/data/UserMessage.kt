package ru.myacademyhomework.tinkoffmessenger.data


data class UserMessage (
    val avatarURL: String,
    val content: String,
    val id: Long,
    val isMeMessage: Boolean,
    val reactions: List<Reaction>,
    val senderFullName: String,
    val timestamp: Long,
    val streamID: Long?,
    var nameTopic: String = ""
): ChatMessage