package ru.myacademyhomework.tinkoffmessenger.data


data class Message(
    val avatar: String,
    val name: String,
    val message: String,
    val listEmoji: MutableList<String>
)