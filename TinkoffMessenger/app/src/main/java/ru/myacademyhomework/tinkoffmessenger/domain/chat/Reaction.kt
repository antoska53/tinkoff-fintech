package ru.myacademyhomework.tinkoffmessenger.domain.chat


data class Reaction(
    val emojiCode: String,
    val emojiName: String,
    val reactionType: String,
    val userId: Int
)
