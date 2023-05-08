package ru.myacademyhomework.tinkoffmessenger.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionDto(
    @SerialName("emoji_code")
    val emojiCode: String,

    @SerialName("emoji_name")
    val emojiName: String,

    @SerialName("reaction_type")
    val reactionType: String,

    @SerialName("user_id")
    val userId: Int
)