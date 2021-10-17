package ru.myacademyhomework.tinkoffmessenger.data

import ru.myacademyhomework.tinkoffmessenger.messageview.EmojiView
import java.util.*

data class Message(
    val avatar: String,
    val name: String,
    val message: String,
    val listEmoji: MutableList<String>
)