package ru.myacademyhomework.tinkoffmessenger.data

import java.time.LocalDate

sealed class SealedMessage
data class Message(
    val avatar: String,
    val name: String,
    val message: String,
    val listEmoji: MutableList<String>
): SealedMessage()

data class DateMessage(
    val date: LocalDate
):SealedMessage()

