package ru.myacademyhomework.tinkoffmessenger.data


data class Reaction(
    val emojiCode: String,
    val emojiName: String,
    val reactionType: String,
    val userId: Int
)
